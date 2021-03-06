package com.example.demo;

import com.example.demo.models.Encrypt;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;


public class EncryptTests {
    Encrypt test = new Encrypt("555", "777");

    @Test
    public void stringUnEncryptTest() {
        String compare = "777";
        char[] chArray = compare.toCharArray();
        compare = Arrays.toString(chArray);

        test.Encryption();
        String res = test.getFirstString();

        Assertions.assertEquals(compare, res);
    }

    @Test
    public void stringEncryptTest() {
        String compare = "555";
        char[] chArray = compare.toCharArray();
        compare = Arrays.toString(chArray);

        test.Decoding();
        String res = test.getSecondString();

        Assertions.assertEquals(compare, res);
    }
}
