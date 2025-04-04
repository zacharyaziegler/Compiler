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
    Map<String, SymbolTableItem> lookupMap = new HashMap<>();  // Symbol table to track declared variables
    MyScanner scanner;
    MyScanner.TOKEN nextToken;
    private AbstractSyntaxTree abstractSyntaxTree;

    /**
     * Entry point for parsing the program.
     * This method initializes the scanner and begins parsing from <Program>.
     *
     * @param program The input program as a string.
     * @return true if parsing was successful, false otherwise.
     */
    public boolean parse(String program) {
        try {
            // Initialize the AST and scanner
            abstractSyntaxTree = new AbstractSyntaxTree();
            this.scanner = new MyScanner(new PushbackReader(new java.io.StringReader(program)));
            nextToken = scanner.scan();  // Get the first token
            AbstractSyntaxTree.NodeProgram root = parseProgram();  // Parse the entire program

            // If program is successfully parsed and we reached the end of the file
            if (root != null && nextToken == MyScanner.TOKEN.SCANEOF) {
                abstractSyntaxTree.setRoot(root);  // Set the root of the AST
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
     * Getter for the AbstractSyntaxTree
     * @return abstractSyntaxTree
     */
    public AbstractSyntaxTree getAbstractSyntaxTree() {
        return abstractSyntaxTree;
    }

    /**
     * Parses the <Program> non-terminal, which consists of <Decls> and <Stmts>.
     */
    AbstractSyntaxTree.NodeProgram parseProgram() throws Exception {
        AbstractSyntaxTree.NodeDecls decls = parseDecls();
        AbstractSyntaxTree.NodeStmts stmts = parseStmts();
        if (decls != null && stmts != null) {
            return abstractSyntaxTree.new NodeProgram(decls, stmts);  // Return the root NodeProgram
        }
        return null;
    }

    /**
     * Parses the <Decls> non-terminal.
     */
    AbstractSyntaxTree.NodeDecls parseDecls() throws Exception {
        AbstractSyntaxTree.NodeDecls nodeDecls = abstractSyntaxTree.new NodeDecls();
        while (nextToken == MyScanner.TOKEN.DECLARE) {  // While we have declarations
            AbstractSyntaxTree.NodeId id = parseDecl();  // Parse each declaration
            if (id != null) {
                nodeDecls.addDecl(id);  // Add declaration to the node
            } else {
                return null;
            }
        }
        return nodeDecls;  // Return the NodeDecls
    }

    /**
     * Parses a single <Decl>: "declare id".
     */
    AbstractSyntaxTree.NodeId parseDecl() throws Exception {
        if (match(MyScanner.TOKEN.DECLARE)) {
            String id = scanner.getTokenBufferString();  // Capture the variable name
            if (match(MyScanner.TOKEN.ID)) {  // Now match the ID
                if (!lookupMap.containsKey(id)) {  // Check if variable was already declared
                    lookupMap.put(id, new SymbolTableItem(id, TYPE.INTDATATYPE));  // Add variable to symbol table
                    return abstractSyntaxTree.new NodeId(id);
                } else {
                    System.out.println("Parse Error: Variable '" + id + "' already declared.");
                    throw new Exception("Duplicate declaration");
                }
            }
        }
        return null;
    }

    /**
     * Parses the <Stmts> non-terminal, which represents a series of statements.
     *
     */
    AbstractSyntaxTree.NodeStmts parseStmts() throws Exception {
        AbstractSyntaxTree.NodeStmts nodeStmts = abstractSyntaxTree.new NodeStmts();
        // Parse statements as long as the next token is a statement starter
        while (nextToken == MyScanner.TOKEN.PRINT || nextToken == MyScanner.TOKEN.SET ||
                nextToken == MyScanner.TOKEN.IF || nextToken == MyScanner.TOKEN.CALC) {
            AbstractSyntaxTree.NodeStmt stmt = parseStmt();  // Parse individual statement
            if (stmt != null) {
                nodeStmts.addStmt(stmt);  // Add statement to the node
            } else {
                return null;
            }
        }
        return nodeStmts;  // Return the NodeStmts
    }

    /**
     * Parses a single <Stmt> non-terminal, which could be a print, set, if, or calc statement.
     */
    AbstractSyntaxTree.NodeStmt parseStmt() throws Exception {
        if (nextToken == MyScanner.TOKEN.PRINT) {
            return parsePrintStmt();
        } else if (nextToken == MyScanner.TOKEN.SET) {
            return parseSetStmt();
        } else if (nextToken == MyScanner.TOKEN.IF) {
            return parseIfStmt();
        } else if (nextToken == MyScanner.TOKEN.CALC) {
            return parseCalcStmt();
        }
        return null;
    }

    /**
     * Parses a print statement: "print id".
     */
    AbstractSyntaxTree.NodePrint parsePrintStmt() throws Exception {
        if (match(MyScanner.TOKEN.PRINT)) {  // Match "print" keyword
            AbstractSyntaxTree.NodeId id = parseId();  // Parse the ID to print
            return abstractSyntaxTree.new NodePrint(id);  // Return NodePrint
        }
        return null;
    }

    /**
     * Parses a set statement: "set id = intliteral".
     */
    AbstractSyntaxTree.NodeSet parseSetStmt() throws Exception {
        if (match(MyScanner.TOKEN.SET)) {
            AbstractSyntaxTree.NodeId id = parseId();  // Parse the ID
            if (match(MyScanner.TOKEN.EQUALS)) {
                AbstractSyntaxTree.NodeIntLiteral literal = parseIntLiteral();  // Parse the integer literal
                return abstractSyntaxTree.new NodeSet(id, literal);  // Return NodeSet
            }
        }
        return null;
    }

    /**
     * Parses an if statement: "if id = id then <Stmts> endif".
     */
    AbstractSyntaxTree.NodeIf parseIfStmt() throws Exception {
        if (match(MyScanner.TOKEN.IF)) {
            AbstractSyntaxTree.NodeId left = parseId();  // Parse the left-hand side ID
            if (match(MyScanner.TOKEN.EQUALS)) {
                AbstractSyntaxTree.NodeId right = parseId();  // Parse the right-hand side ID
                if (match(MyScanner.TOKEN.THEN)) {
                    AbstractSyntaxTree.NodeStmts stmts = parseStmts();  // Parse statements inside the if block
                    if (stmts != null && match(MyScanner.TOKEN.ENDIF)) {
                        return abstractSyntaxTree.new NodeIf(left, right, stmts);  // Return NodeIf
                    }
                }
            }
        }
        return null;
    }

    /**
     * Parses a calc statement: "calc id = <Sum>".
     */
    AbstractSyntaxTree.NodeCalc parseCalcStmt() throws Exception {
        if (match(MyScanner.TOKEN.CALC)) {
            AbstractSyntaxTree.NodeId id = parseId();  // Parse the ID
            if (match(MyScanner.TOKEN.EQUALS)) {
                AbstractSyntaxTree.NodeExpr expr = parseSum();  // Parse the sum expression
                return abstractSyntaxTree.new NodeCalc(id, expr);  // Return NodeCalc
            }
        }
        return null;
    }

    /**
     * Parses a sum expression: <Value> <SumEnd>.
     */
    AbstractSyntaxTree.NodeExpr parseSum() throws Exception {
        AbstractSyntaxTree.NodeExpr lhs = parseValue();  // Parse the left-hand side value
        AbstractSyntaxTree.NodeExpr rhs = parseSumEnd(lhs);  // Continue parsing the sum
        return rhs;  // Return the final expression
    }

    /**
     * Parses the rest of the sum expression: "+ <Value> <SumEnd>" or epsilon.
     */
    AbstractSyntaxTree.NodeExpr parseSumEnd(AbstractSyntaxTree.NodeExpr lhs) throws Exception {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            match(MyScanner.TOKEN.PLUS);
            AbstractSyntaxTree.NodeExpr rhs = parseValue();  // Parse the right-hand side value
            return abstractSyntaxTree.new NodePlus(lhs, parseSumEnd(rhs));  // Return NodePlus
        }
        return lhs;  // Return the expression if no further sum
    }

    /**
     * Parses a value, which can be either an ID or an integer literal.
     */
    AbstractSyntaxTree.NodeExpr parseValue() throws Exception {
        if (nextToken == MyScanner.TOKEN.ID) {
            return parseId();  // Parse an identifier
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            return parseIntLiteral();  // Parse an int literal
        }
        return null;
    }

    /**
     * Parses an ID.
     */
    AbstractSyntaxTree.NodeId parseId() throws Exception {
        String id = scanner.getTokenBufferString();  // Capture the identifier
        if (match(MyScanner.TOKEN.ID)) {
            return abstractSyntaxTree.new NodeId(id);  // Return NodeId
        }
        return null;
    }

    /**
     * Parses an integer literal.
     */
    AbstractSyntaxTree.NodeIntLiteral parseIntLiteral() throws Exception {
        String intLiteral = scanner.getTokenBufferString();  // Capture the int literal
        if (match(MyScanner.TOKEN.INTLITERAL)) {
            return abstractSyntaxTree.new NodeIntLiteral(Integer.parseInt(intLiteral));  // Return NodeIntLiteral
        }
        return null;
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
        if (nextToken == expectedToken) {  // Check if the token matches
            System.out.println("Matched: " + expectedToken + " (" + scanner.getTokenBufferString() + ")");
            nextToken = scanner.scan();  // Get the next token
            return true;
        } else {
            System.out.println("Parse Error: Expected " + expectedToken + " but got " + nextToken);
            throw new Exception("Unexpected token");
        }
    }
}
