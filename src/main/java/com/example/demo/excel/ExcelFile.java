package com.example.demo.excel;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ExcelFile {
    public static final File sheet = new File("Table.xls");
}
