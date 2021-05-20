package com.example.demo.memory;

import com.example.demo.models.Encrypt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Cache {
    private Map<Parameters, Encrypt> EncryptHashMap = new HashMap<>();

    public boolean searchEncrypt(Parameters parameters) {
        return EncryptHashMap.containsKey(parameters);
    }

    public Encrypt getEncrypt(Parameters parameters) {
        return EncryptHashMap.get(parameters);
    }

    public void addEncrypt(Parameters parameters, Encrypt encrypt) {
        EncryptHashMap.put(parameters, encrypt);
    }
}