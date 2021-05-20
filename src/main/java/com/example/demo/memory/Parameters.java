package com.example.demo.memory;

import com.example.demo.models.Encrypt;

public class Parameters {
    private String str_1;
    private String str_2;

    public Parameters(String str_1, String str_2) {
        this.str_1 = str_1;
        this.str_2 = str_2;
    }

    public Encrypt createEncrypt() {
        Encrypt encrypt = new Encrypt(str_1, str_2);
        return encrypt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + str_1.length();
        result = prime * result + str_2.length();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Parameters other = (Parameters) obj;
        if (!str_1.equals(other.str_1))
            return false;
        if (!str_2.equals(other.str_2))
            return false;
        return true;
    }
}