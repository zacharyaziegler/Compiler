package org.example;

/**
 * Main Class, unused
 *
 * @author Zachary Ziegler
 */
public class Main {

    public static void main(String[] args) {

        MyParser parser = new MyParser();

        String programNoErrors = "declare w int\n" +
                "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "set z = 20\n" +
                "calc w = x + y + z\n" +
                "if x = y then\n" +
                "print w\n" +
                "print x\n" +
                "endif\n" +
                "print y\n" +
                "print z";

        boolean success = parser.parse(programNoErrors);
        if (success) {
            parser.displaySymbolTable();
        }

//        String programSyntaxError = "declare w int\n" +
//                "declare x int\n" +
//                "set w = 5\n" +
//                "set x = 10\n" +
//                "if w x then\n" +
//                "print w\n" +
//                "endif";
//        parser.parse(programSyntaxError);

//        String programSemanticError = "set w = 5\n" +
//                "set x = 10\n" +
//                "calc y = w + x";
//
//        parser.parse(programSemanticError);

    }
}