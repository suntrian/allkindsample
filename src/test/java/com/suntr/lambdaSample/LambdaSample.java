package com.suntr.lambdaSample;

import com.suntr.util.PrintUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaSample {

    List<String> sample = new ArrayList<>();

    public String processFile(BufferedReaderProcessor processor) throws IOException {
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(".gitignore"))
        ) {
            return processor.process(bufferedReader);
        }
    }

    @Test
    public void testLambdaReadFile() throws IOException {
        System.out.println(processFile((BufferedReader r) -> r.readLine()+r.readLine()));   //lambda method
        System.out.println(processFile(BufferedReader::readLine));          // method reference
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 10; i++) {
            sample.add(String.valueOf(i));
        }
    }

    @Test
    public void testPredicate() {
        List l = sample.stream().filter((String s) -> Integer.valueOf(s)>0).collect(Collectors.toList());
        PrintUtil.print(l);
        List m = sample.stream().filter((String s)-> Integer.valueOf(s)%2==0).collect(Collectors.toList());
        PrintUtil.print(m);
    }

    @Test
    public void testConsumer() {
        sample.forEach((String s)->s = s+s);
        PrintUtil.print(sample);
    }
}
