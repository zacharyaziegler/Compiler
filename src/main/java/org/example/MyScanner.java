package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Collections;

public class MyScanner {
    enum TOKEN {
        SCANEOF, ID, INTLITERAL,
        INTDATATYPE, DECLARE, PRINT, SET, EQUALS,
        IF, THEN, ENDIF, CALC, PLUS
    }

    private ArrayList<String> reservedWords;
    private PushbackReader pbr;
    private StringBuilder buffer = new StringBuilder();

    public MyScanner(PushbackReader pbr) {
        reservedWords = new ArrayList<>();
        Collections.addAll(reservedWords, "declare", "int", "print", "set", "if",
                "then", "endif", "calc");
        this.pbr = pbr;
    }

    public TOKEN scan() throws Exception {
        try {
            int c;
            c = pbr.read();
            while (c != -1) {
                if (Character.isDigit(c)) {
                    buffer.setLength(0); // Clear the buffer
                    buffer.append((char) c); // Add the first char to buffer
                    c = pbr.read();
                    while (Character.isDigit(c)) {
                        buffer.append(c); // Add char to buffer
                        c = pbr.read();
                    }
                    pbr.unread(c);


                    return TOKEN.INTLITERAL;
                } else if (c == '+') {
                    return TOKEN.PLUS;
                } else if (c == '=') {
                    return TOKEN.EQUALS;
                } else if (Character.isLetter(c)) {
                    buffer.setLength(0); // Clear buffer
                    buffer.append((char) c); // Add the first char to buffer
                    c = pbr.read();
                    while (Character.isLetter(c)) {
                        buffer.append((char)c); // Add char to buffer
                        c = pbr.read();
                    }
                    pbr.unread(c);
                    if (buffer.toString().equals("int")) {
                        return TOKEN.INTDATATYPE; // returns int data type because scanner read "int"
                    } else if (reservedWords.contains(buffer.toString().toLowerCase())) {
                        String reservedWord = buffer.toString().toUpperCase();
                        return TOKEN.valueOf(reservedWord); // if scanner reads reserved word that isn't int, return that word
                    } else {
                        return TOKEN.ID;
                    }
                }


                c = pbr.read();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return TOKEN.SCANEOF;
    }
}

