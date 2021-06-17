package com.example.demo.controllers;

import com.example.demo.counter.MetricCounter;
import com.example.demo.excel.ExcelBuilder;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.InternalServerException;
import com.example.demo.memory.Cache;
import com.example.demo.memory.Parameters;
import com.example.demo.models.Encrypt;
import com.example.demo.models.Representation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EncryptController {
    private static final Logger logger = LogManager.getLogger(EncryptController.class);
    private final MetricCounter metricCounter;
    private final Cache cache;
    private ExcelBuilder excelBuilder;

    public EncryptController(MetricCounter metricCounter, Cache cache, ExcelBuilder excelBuilder) {
        this.metricCounter = metricCounter;
        this.cache = cache;
        this.excelBuilder = excelBuilder;
    }

    @GetMapping("/counter")
    public int counter() {
        return metricCounter.getCounter();
    }

    @GetMapping("/encrypt")
    public Representation encrypt(@RequestParam(value = "first", defaultValue = "0") List<String> first,
                                  @RequestParam(value = "second", defaultValue = "0") List<String> second) {

        metricCounter.incrementCounter();

        if (first.stream().parallel().anyMatch(str -> str.length() > 10)
                || second.stream().parallel().anyMatch(str -> str.length() > 10)) {
            logger.error("Data are not valid");
            throw new BadRequestException();
        }

        List<Parameters> originalList = new ArrayList<>();

        for (int i = 0; i < Math.max(first.size(), second.size()); i++) {
            originalList.add(new Parameters(first.get(i), second.get(i)));
        }

        List<Encrypt> ResultList = new ArrayList<>();

        try {
            ResultList.addAll(originalList
                    .stream()
                    .parallel()
                    .filter(cache::searchEncrypt)
                    .map(parameters -> {
                        logger.info("Data are located in cache!");
                        return cache.getEncrypt(parameters);
                    })
                    .collect(Collectors.toList()));

            ResultList.addAll(originalList
                    .stream()
                    .parallel()
                    .filter(parameters -> !cache.searchEncrypt(parameters))
                    .map(parameters -> {
                        Encrypt encrypt = parameters.createEncrypt();
                        encrypt.Encryption();
                        encrypt.Decoding();
                        cache.addEncrypt(parameters, encrypt);
                        logger.info("Success!");
                        return encrypt;
                    })
                    .collect(Collectors.toList()));

        } catch (InternalServerException ex) {
            logger.error("Server error!");
            throw new InternalServerException();
        }

        String popularEncrypt = ResultList
                .stream()
                .collect(Collectors.groupingBy(Encrypt::getFirstString, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Not defined");

        String popularDecoding = ResultList
                .stream()
                .collect(Collectors.groupingBy(Encrypt::getSecondString, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Not defined");

        Collection<Encrypt> encryptCollection = cache.getCollection();
        excelBuilder.buildExcel(encryptCollection);

        return new Representation(ResultList, popularEncrypt, popularDecoding);
    }

    @PostMapping(path = "/post_encrypt", consumes = "application/json", produces = "application/json")
    public Representation postEncrypt(@RequestBody List<Encrypt> encrypts) {

        metricCounter.incrementCounter();

        if (encrypts.size() == 0) {
            logger.info("List empty!");
            throw new BadRequestException();
        }

        List<Encrypt> myList = encrypts
                .stream()
                .parallel()
                .filter(encrypt ->
                        encrypt.getFirstString().length() <= 10 &&
                                encrypt.getSecondString().length() <= 10)
                .peek(Encrypt::Encryption)
                .peek(Encrypt::Decoding)
                .collect(Collectors.toList());

        String popularEncrypt = encrypts
                .stream()
                .collect(Collectors.groupingBy(Encrypt::getFirstString, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Not defined");

        String popularDecoding = myList
                .stream()
                .collect(Collectors.groupingBy(Encrypt::getSecondString, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Not defined");

        return new Representation(myList, popularEncrypt, popularDecoding);
    }
}