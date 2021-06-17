package com.example.demo.models;

import com.example.demo.memory.Parameters;

import java.util.Arrays;


public class Encrypt {

    private String first;
    private String second;

    public Encrypt(String before_encrypt, String after_encrypt) {
        this.first = before_encrypt;
        this.second = after_encrypt;
    }

    public Encrypt() {
        this.first = null;
        this.second = null;
    }


    public void Encryption() {
        char[] chArray = first.toCharArray();
        for (int i = 0; i < first.length(); i++) {
            chArray[i] = (char) (chArray[i] + 2);
        }
        first = Arrays.toString(chArray);
    }

    public void Decoding() {
        char[] chArray = second.toCharArray();
        for (int i = 0; i < second.length(); i++) {
            chArray[i] = (char) (chArray[i] - 2);
        }
        second = Arrays.toString(chArray);
    }

    public String getFirstString() {
        return first;
    }
    public String getSecondString() {
        return second;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}