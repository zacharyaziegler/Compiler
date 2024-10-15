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
                buffer.setLength(0);  // Clear the buffer before reading a new token

                // Skip all whitespace
                while (Character.isWhitespace(c)) {
                    c = pbr.read();
                }

                if (Character.isDigit(c)) {
                    buffer.append((char) c);
                    c = pbr.read();
                    while (Character.isDigit(c)) {
                        buffer.append((char) c);
                        c = pbr.read();
                    }
                    pbr.unread(c);
                    return TOKEN.INTLITERAL;
                } else if (c == '+') {
                    return TOKEN.PLUS;
                } else if (c == '=') {
                    return TOKEN.EQUALS;
                } else if (Character.isLetter(c)) {
                    buffer.append((char) c);
                    c = pbr.read();
                    while (Character.isLetter(c)) {
                        buffer.append((char) c);
                        c = pbr.read();
                    }
                    pbr.unread(c);

                    String word = buffer.toString();

                    // Ensure that "int" is always treated as a data type and not an identifier
                    if (word.equalsIgnoreCase("int")) {
                        return TOKEN.INTDATATYPE;  // Correctly handle the "int" type
                    } else if (reservedWords.contains(word.toLowerCase())) {
                        return TOKEN.valueOf(word.toUpperCase());  // Reserved keywords
                    } else {
                        return TOKEN.ID;  // Otherwise, it's an identifier
                    }
                }

                c = pbr.read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return TOKEN.SCANEOF;  // End of file
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

