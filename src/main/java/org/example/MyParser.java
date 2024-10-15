package org.example;

import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

/**
 * MyParser Class
 *
 * Used to parse lines of a program with help from the MyScanner Class
 */
public class MyParser {
    enum TYPE {INTDATATYPE}
    Map<String, SymbolTableItem> lookupMap = new HashMap<>();
    MyScanner scanner;
    MyScanner.TOKEN nextToken;

    /**
     * Entry point for parsing the program.
     * This method initializes the scanner and begins parsing from <Program>.
     *
     * @param program The input program as a string.
     * @return true if parsing was successful, false otherwise.
     */
    public boolean parse(String program) {
        try {
            // Initialize the scanner
            this.scanner = new MyScanner(new PushbackReader(new java.io.StringReader(program)));
            // Get the first token
            nextToken = scanner.scan();

            // Begin parsing from the top-level non-terminal <Program>
            boolean result = parseProgram();

            // Ensure the last token is the end-of-file symbol $
            if (result && nextToken == MyScanner.TOKEN.SCANEOF) {
                System.out.println("Parse Successful");
                return true;
            } else {
                System.out.println("Parse Error: Expected end of program");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Parse Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Parses the <Program> non-terminal, which consists of <Decls> and <Stmts>.
     *
     * @return true if both <Decls> and <Stmts> are parsed successfully.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseProgram() throws Exception {
        if (parseDecls() && parseStmts()) {
            return true;
        }
        return false;
    }

    /**
     * Parses the <Decls> non-terminal, which represents a series of declarations.
     *
     * @return true if <Decls> is successfully parsed, false if there's an error.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseDecls() throws Exception {
        while (nextToken == MyScanner.TOKEN.DECLARE) {
            if (!parseDecl()) {
                return false;
            }
        }
        return true; // Allow epsilon
    }

    /**
     * Parses a single <Decl> non-terminal: "declare id int".
     *
     * @return true if the declaration is successfully parsed.
     * @throws Exception if an error occurs or a duplicate declaration is found.
     */
    boolean parseDecl() throws Exception {
        if (match(MyScanner.TOKEN.DECLARE)) {
            if (nextToken == MyScanner.TOKEN.ID) { // Match the ID
                String id = scanner.getTokenBufferString();  // Capture ID
                match(MyScanner.TOKEN.ID);  // Move to next token

//                System.out.println("Captured ID: " + id);  // Debugging info

                if (!lookupMap.containsKey(id)) { // Check if the identifier is already in the symbol table
                    if (match(MyScanner.TOKEN.INTDATATYPE)) { // match INTDATATYPE
//                        System.out.println("Adding '" + id + "' to symbol table with type INTDATATYPE"); // Debugging info
                        lookupMap.put(id, new SymbolTableItem(id, TYPE.INTDATATYPE));
                        return true;
                    }
                } else {
                    System.out.println("Parse Error: Variable '" + id + "' already declared.");
                    throw new Exception("Duplicate declaration");
                }
            }
        }
        return false;
    }

    /**
     * Parses the <Stmts> non-terminal, which represents a series of statements.
     *
     * @return true if <Stmts> is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseStmts() throws Exception {
        while (nextToken == MyScanner.TOKEN.PRINT || nextToken == MyScanner.TOKEN.SET ||
                nextToken == MyScanner.TOKEN.IF || nextToken == MyScanner.TOKEN.CALC) {
            if (!parseStmt()) {
                return false;
            }
        }
        return true; // Allow epsilon
    }

    /**
     * Parses a single <Stmt> non-terminal, which could be a print, set, if, or calc statement.
     *
     * @return true if a valid statement is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseStmt() throws Exception {
        if (nextToken == MyScanner.TOKEN.PRINT) {
            return parsePrintStmt();
        } else if (nextToken == MyScanner.TOKEN.SET) {
            return parseSetStmt();
        } else if (nextToken == MyScanner.TOKEN.IF) {
            return parseIfStmt();
        } else if (nextToken == MyScanner.TOKEN.CALC) {
            return parseCalcStmt();
        }
        return false;
    }

    /**
     * Parses a print statement: "print id".
     *
     * @return true if the print statement is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parsePrintStmt() throws Exception {
        if (match(MyScanner.TOKEN.PRINT)) {
            if (match(MyScanner.TOKEN.ID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses a set statement: "set id = intliteral".
     *
     * @return true if the set statement is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseSetStmt() throws Exception {
        if (match(MyScanner.TOKEN.SET)) {
            if (match(MyScanner.TOKEN.ID)) {
                if (match(MyScanner.TOKEN.EQUALS)) {
                    if (match(MyScanner.TOKEN.INTLITERAL)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Parses an if statement: "if id = id then <Stmts> endif".
     *
     * @return true if the if statement is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseIfStmt() throws Exception {
        if (match(MyScanner.TOKEN.IF)) {
            if (match(MyScanner.TOKEN.ID)) {
                if (match(MyScanner.TOKEN.EQUALS)) {
                    if (match(MyScanner.TOKEN.ID)) {
                        if (match(MyScanner.TOKEN.THEN)) {
                            if (parseStmts()) {
                                return match(MyScanner.TOKEN.ENDIF);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Parses a calc statement: "calc id = <Sum>".
     *
     * @return true if the calc statement is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseCalcStmt() throws Exception {
        if (match(MyScanner.TOKEN.CALC)) {
            if (match(MyScanner.TOKEN.ID)) {
                if (match(MyScanner.TOKEN.EQUALS)) {
                    return parseSum();
                }
            }
        }
        return false;
    }

    /**
     * Parses a sum expression: <Value> <SumEnd>.
     *
     * @return true if the sum is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseSum() throws Exception {
        if (parseValue()) {
            return parseSumEnd();
        }
        return false;
    }

    /**
     * Parses the rest of the sum expression: "+ <Value> <SumEnd>" or epsilon.
     *
     * @return true if the sum end is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseSumEnd() throws Exception {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            match(MyScanner.TOKEN.PLUS);
            if (parseValue()) {
                return parseSumEnd();
            }
            return false;
        }
        return true; // Allow epsilon
    }

    /**
     * Parses a value, which can be either an ID or an integer literal.
     *
     * @return true if the value is successfully parsed.
     * @throws Exception if an error occurs during parsing.
     */
    boolean parseValue() throws Exception {
        if (nextToken == MyScanner.TOKEN.ID || nextToken == MyScanner.TOKEN.INTLITERAL) {
            match(nextToken);
            return true;
        }
        return false;
    }

    /**
     * Displays the contents of the symbol table.
     */
    public void displaySymbolTable() {
        System.out.println("Symbol Table:");
        for (Map.Entry<String, SymbolTableItem> entry : lookupMap.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    /**
     * Matches the expected token. If a match occurs, it scans the next token.
     * Otherwise, it throws an exception with a formatted parse error message.
     *
     * @param expectedToken The token that is expected.
     * @return true if the tokens match.
     * @throws Exception if a parse error occurs.
     */
    boolean match(MyScanner.TOKEN expectedToken) throws Exception {
        if (nextToken == expectedToken) {
            System.out.println("Matched: " + expectedToken + " (" + scanner.getTokenBufferString() + ")");
            nextToken = scanner.scan();
            return true;
        } else { // Error message
            System.out.println("Parse Error");
            System.out.println("Received: " + nextToken + " Buffer " + scanner.getTokenBufferString());
            System.out.println("Expected: " + expectedToken);
            throw new Exception("Unexpected token");
        }
    }



}
