package com.suntr.jline;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class JConsoleSample {

    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .appName("abc")
                .build();
        String line;
        while ((line = lineReader.readLine(">")) != null) {
            System.out.println(line);
        }

    }

}
