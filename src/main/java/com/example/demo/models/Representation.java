package com.example.demo.models;

import java.util.List;

public class Representation {
    private List<Encrypt> list;
    private String popularEncrypt;
    private String popularDecrypt;

    public Representation(List<Encrypt> list, String popularEncrypt, String popularDecrypt) {
        this.list = list;
        this.popularEncrypt = popularEncrypt;
        this.popularDecrypt = popularDecrypt;
    }

    public List<Encrypt> getList() {
        return list;
    }

    public String getPopularEncrypt() {
        return popularEncrypt;
    }

    public String getPopularDecrypt() {
        return popularDecrypt;
    }
}
