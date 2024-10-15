package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * MyScanner Class
 * Used to scan lines of code as part of a compiler.
 *
 * @author Zachary Ziegler
 */
public class MyScanner {
    enum TOKEN {
        SCANEOF, ID, INTLITERAL,
        INTDATATYPE, DECLARE, PRINT, SET, EQUALS,
        IF, THEN, ENDIF, CALC, PLUS
    }

    private ArrayList<String> reservedWords;
    private PushbackReader pbr;
    private StringBuilder buffer = new StringBuilder();

    /**
     * Used to initialize pbr member variable and reservedWords list.
     * @param pbr
     */
    public MyScanner(PushbackReader pbr) {
        reservedWords = new ArrayList<>();
        Collections.addAll(reservedWords, "declare", "int", "print", "set", "if",
                "then", "endif", "calc");
        this.pbr = pbr;
    }

    /**
     * Scans the next input in line of code, returns TOKEN associated with input.
     * @return
     * @throws Exception
     */
    public TOKEN scan() throws Exception {
        try {
            int c;
            c = pbr.read();
            while (c != -1) {
                // Skip all whitespace (including spaces, tabs, and newlines)
                while (Character.isWhitespace(c)) {
                    c = pbr.read();
                }
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
                        buffer.append((char) c); // Add char to buffer
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
                } else if (c == '\\') {
                    // Check for literal `\n`
                    c = pbr.read();
                    if (c == 'n') {
                        c = pbr.read();
                        continue; // Treat as whitespace and move on
                    } else {
                        pbr.unread(c); // Not a valid sequence, unread and treat as normal character
                        continue;
                    }
                }

                c = pbr.read();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return TOKEN.SCANEOF; // End of file
    }

    /**
     * returns buffer as a string
     * @return
     */
    public String getTokenBufferString() {
        String bufferString = buffer.toString();
        return bufferString;
    }
}

