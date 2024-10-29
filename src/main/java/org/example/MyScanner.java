package org.example;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * MyScanner Class
 */
public class MyScanner {
    enum TOKEN {
        SCANEOF, ID, INTLITERAL, INTDATATYPE, DECLARE, PRINT, SET, EQUALS, IF, THEN, ENDIF, CALC, PLUS
    }

    private ArrayList<String> reservedWords;
    private PushbackReader pbr;
    private StringBuilder buffer = new StringBuilder();

    /**
     * Initializes pbr and reservedWords list.
     * @param pbr
     */
    public MyScanner(PushbackReader pbr) {
        reservedWords = new ArrayList<>();
        Collections.addAll(reservedWords, "declare", "int", "print", "set", "if", "then", "endif", "calc");
        this.pbr = pbr;
    }

    /**
     * Scans the next input in line of code, returns TOKEN associated with input.
     * @return TOKEN
     * @throws Exception
     */
    public TOKEN scan() throws Exception {
        buffer.setLength(0);  // Clear the buffer at the start of every token read
        int c = pbr.read();

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
            pbr.unread(c);  // Push the last read character back
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

            // Check if the word matches a reserved keyword
            if (word.equalsIgnoreCase("declare")) {
                return TOKEN.DECLARE;
            } else if (word.equalsIgnoreCase("int")) {
                return TOKEN.INTDATATYPE;
            } else if (reservedWords.contains(word.toLowerCase())) {
                return TOKEN.valueOf(word.toUpperCase());  // Reserved keywords
            } else {
                return TOKEN.ID;  // Otherwise, it's an identifier
            }
        }

        return TOKEN.SCANEOF;
    }

    /**
     * Returns the token buffer as a string.
     * @return String
     */
    public String getTokenBufferString() {
        return buffer.toString();
    }
}
