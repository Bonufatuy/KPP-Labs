package com.example.demo.controllers;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.InternalServerException;
import com.example.demo.memory.Cache;
import com.example.demo.memory.Parameters;
import com.example.demo.models.Encrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EncryptController {
    private static final Logger logger = LogManager.getLogger(EncryptController.class);

    @Autowired
    private Cache cache;

    @GetMapping("/encrypt")
    public Encrypt encrypt(@RequestParam(value = "str_1", defaultValue = "0") String str_1,
                           @RequestParam(value = "str_2", defaultValue = "0") String str_2) {

        if (str_1.length() > 10 || str_2.length() > 10) {
            logger.error("User error!");
            throw new BadRequestException();
        }

        try {
            Parameters parameters = new Parameters(str_1, str_2);
            if (cache.searchEncrypt(parameters)) {
                logger.info("Parameters located in cache!");
                return cache.getEncrypt(parameters);
            } else {
                Encrypt encrypt = parameters.createEncrypt();
                encrypt.stringEncrypt();
                encrypt.stringUnEncrypt();
                cache.addEncrypt(parameters, encrypt);
                logger.info("Success!");
                return encrypt;
            }
        } catch (NumberFormatException ex) {
            logger.error("User error!");
            throw new BadRequestException();
        } catch (InternalServerException ex) {
            logger.error("Server error!");
            throw new InternalServerException();
        }
    }
}