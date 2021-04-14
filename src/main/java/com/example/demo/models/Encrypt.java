package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class Encrypt {

    @JsonProperty("Before encrypt")
    private String before_encrypt;
    @JsonProperty("After encrypt")
    private String after_encrypt;

    public Encrypt(String before_encrypt, String after_encrypt) {
        this.before_encrypt = before_encrypt;
        this.after_encrypt = after_encrypt;
    }

    public String stringEncrypt() {
        char[] chArray = before_encrypt.toCharArray();
        for (int i = 0; i < before_encrypt.length(); i++) {
            chArray[i] = (char) (chArray[i] + 2);
        }
        before_encrypt = Arrays.toString(chArray);
        return before_encrypt;
    }

    public String stringUnEncrypt() {
        char[] chArray = after_encrypt.toCharArray();
        for (int i = 0; i < after_encrypt.length(); i++) {
            chArray[i] = (char) (chArray[i] - 2);
        }
        after_encrypt = Arrays.toString(chArray);
        return after_encrypt;
    }

    public String getBefore_encrypt() {
        return before_encrypt;
    }

    public String getAfter_encrypt() {
        return after_encrypt;
    }
}