package com.example.demo.controllers;

import com.example.demo.counter.MetricCounter;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.InternalServerException;
import com.example.demo.memory.Cache;
import com.example.demo.memory.Parameters;
import com.example.demo.models.Encrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EncryptController {
    private static final Logger logger = LogManager.getLogger(EncryptController.class);
    private final MetricCounter metricCounter;
    private final Cache cache;

    public EncryptController(MetricCounter metricCounter, Cache cache) {
        this.metricCounter = metricCounter;
        this.cache = cache;
    }

    @GetMapping("/counter")
    public int counter() {
        return metricCounter.getCounter();
    }

    @GetMapping("/encrypt")
    public List<Encrypt> encrypt(@RequestParam(value = "first", defaultValue = "0") List<String> first,
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

        System.out.printf("%s", popularEncrypt);
        System.out.printf("%s", popularDecoding);

        return ResultList;
    }

    @PostMapping(path = "/encrypts", consumes = "application/json", produces = "application/json")
    public int postCalendars(@RequestBody List<Encrypt> encrypts) {
        List<Encrypt> myList = encrypts
                .stream()
                .parallel()
                .filter(encrypt ->
                        encrypt.getFirstString().length() <= 10 &&
                                encrypt.getSecondString().length() <= 10)
                .peek(Encrypt::Encryption).collect(Collectors.toList());

        int inputCount = encrypts.size();

        System.out.printf("%d%d", inputCount);
        metricCounter.incrementCounter();
        return 5;
    }
}