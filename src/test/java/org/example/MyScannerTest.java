package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PushbackReader;

import static org.junit.jupiter.api.Assertions.*;

class MyScannerTest {

    @Test
    void testOne() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test1.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.DECLARE, token); // test for declare

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.INTDATATYPE, token); // test for intdatatype

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testTwo() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test2.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.SET, token); // test for set

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.EQUALS, token); // test for equals

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.INTLITERAL, token); // test for intliteral

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testThree() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test3.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.CALC, token); // test for calc

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.PLUS, token); // test for plus

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFour() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test4.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.PRINT, token); // test for print

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFive() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test5.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.IF, token); // test for if

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.EQUALS, token); // test for equals

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.THEN, token); // test for then

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ENDIF, token); // test for endif

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSix() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test6.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.IF, token); // test for if

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.EQUALS, token); // test for equals

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.THEN, token); // test for then

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.PRINT, token); // test for print

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ID, token); // test for id

            token = scanner.scan();
            assertEquals(MyScanner.TOKEN.ENDIF, token); // test for endif

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSeven() {
        try {
            PushbackReader pbr = new PushbackReader(new FileReader(new File("test7.txt")));
            MyScanner scanner = new MyScanner(pbr);

            MyScanner.TOKEN token = scanner.scan();
            assertEquals(MyScanner.TOKEN.SCANEOF, token); // test for SCANEOF
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}