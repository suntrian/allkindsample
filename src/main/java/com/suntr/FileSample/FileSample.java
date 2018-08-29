package com.suntr.FileSample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSample {

    public static void main(String[] args) throws IOException {
        File file = new File("src/main/java/com/aaa.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("fdslfs");
        writer.flush();
        System.out.println(file.exists());
    }
}
