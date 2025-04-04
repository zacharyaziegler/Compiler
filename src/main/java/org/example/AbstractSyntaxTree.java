package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for AbstractSyntaxTree, contains inner classes for each Node type
 */
public class AbstractSyntaxTree {


    // Base class for all nodes
    public abstract class NodeBase {
        public abstract void display();

        public abstract String generateCode();
    }

    // Abstract class for expressions
    public abstract class NodeExpr extends NodeBase {

    }

    // Abstract class for statements
    public abstract class NodeStmt extends NodeBase {

    }

    // Class for ID
    public class NodeId extends NodeExpr {
        String name;

        public NodeId(String name) {
            this.name = name;
        }

        @Override
        public void display() {
            System.out.println("AST id " + name);
        }

        @Override
        public String generateCode() {
            if (variableRegisterMap.containsKey(name)) {
                return variableRegisterMap.get(name);
            }
            return ""; // Variable is not in a register yet
        }
    }

    // Class for an intliteral
    public class NodeIntLiteral extends NodeExpr {
        int value;

        public NodeIntLiteral(int value) {
            this.value = value;
        }

        @Override
        public void display() {
            System.out.println("AST int literal " + value);
        }

        @Override
        public String generateCode() {
            String register = "ri" + nextIntRegister++;
            generatedCode.add("loadintliteral " + register + ", " + value);
            return register;
        }
    }

    // Class for addition
    public class NodePlus extends NodeExpr {
        NodeExpr left;  // Left operand
        NodeExpr right; // Right operand

        public NodePlus(NodeExpr left, NodeExpr right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void display() {
            System.out.println("AST sum");
            System.out.print("LHS: ");
            left.display();
            System.out.print("RHS: ");
            right.display();
        }

        @Override
        public String generateCode() {
            String leftRegister = left.generateCode();
            String rightRegister = right.generateCode();
            String resultRegister = "ri" + nextIntRegister++;
            generatedCode.add("add " + leftRegister + ", " + rightRegister + ", " + resultRegister);
            return resultRegister;
        }
    }

    // Class for a print statement
    public class NodePrint extends NodeStmt {
        NodeId id;

        public NodePrint(NodeId id) {
            this.id = id;
        }

        @Override
        public void display() {
            System.out.println("AST print");
            id.display();
            System.out.println();
        }

        @Override
        public String generateCode() {
            String idRegister = id.generateCode();
            generatedCode.add("printi " + idRegister);
            return "";
        }
    }

    // Class for a set statement
    public class NodeSet extends NodeStmt {
        NodeId id;
        NodeIntLiteral literal;

        public NodeSet(NodeId id, NodeIntLiteral literal) {
            this.id = id;
            this.literal = literal;
        }

        @Override
        public void display() {
            System.out.println("AST set");
            id.display();
            literal.display();
            System.out.println();
        }

        @Override
        public String generateCode() {
            String literalRegister = literal.generateCode();
            generatedCode.add("storeintvar " + literalRegister + ", " + id.name);
            variableRegisterMap.put(id.name, literalRegister);
            return "";
        }
    }

    // Class for a calculation (e.g. set w = x + y)
    public class NodeCalc extends NodeStmt {
        NodeId id;
        NodeExpr expr;

        public NodeCalc(NodeId id, NodeExpr expr) {
            this.id = id;
            this.expr = expr;
        }

        @Override
        public void display() {
            System.out.println("AST calc");
            id.display();
            expr.display();
            System.out.println();
        }

        @Override
        public String generateCode() {
            String exprRegister = expr.generateCode();
            generatedCode.add("storeintvar " + exprRegister + ", " + id.name);
            variableRegisterMap.put(id.name, exprRegister);
            return "";
        }
    }

    // Class for multiple statements
    public class NodeStmts extends NodeStmt {
        List<NodeStmt> stmts;

        public NodeStmts() {
            this.stmts = new ArrayList<>();
        }

        public void addStmt(NodeStmt stmt) {
            stmts.add(stmt);
        }

        @Override
        public void display() {
            for (NodeStmt stmt : stmts) {
                stmt.display();
            }
        }

        @Override
        public String generateCode() {
            for (NodeStmt stmt : stmts) {
                stmt.generateCode(); // Generate code for each statement
            }
            return "";
        }
    }

    // Class for an if statement
    public class NodeIf extends NodeStmt {
        NodeId left;
        NodeId right;
        NodeStmts stmts;

        public NodeIf(NodeId left, NodeId right, NodeStmts stmts) {
            this.left = left;
            this.right = right;
            this.stmts = stmts;
        }

        @Override
        public void display() {
            System.out.println("AST if");
            System.out.print("LHS: ");
            left.display();
            System.out.print("RHS: ");
            right.display();
            stmts.display();
            System.out.println("AST endif");
            System.out.println();
        }

        @Override
        public String generateCode() {
            String leftRegister = left.generateCode();
            String rightRegister = right.generateCode();
            String label = "label" + (generatedCode.size() + 1); // Generate label without colon first for syntax, add colon later
            generatedCode.add("bne " + leftRegister + ", " + rightRegister + ", " + label);
            stmts.generateCode();
            generatedCode.add(":" + label);
            return "";
        }
    }

    // Class for multiple declarations
    public class NodeDecls extends NodeBase {
        List<NodeId> decls;

        public NodeDecls() {
            this.decls = new ArrayList<>();
        }

        public void addDecl(NodeId id) {
            decls.add(id);
        }

        @Override
        public void display() {
            System.out.println("AST declarations");
            for (NodeId decl : decls) {
                decl.display();
            }
            System.out.println();
        }

        @Override
        public String generateCode() {
            for (NodeId decl : decls) {
                generatedCode.add("var int " + decl.name); // Generate code for each decl
            }
            return "";
        }
    }

    // Class for the entire program (root node of the AST)
    public class NodeProgram extends NodeBase {
        NodeDecls decls;
        NodeStmts stmts;

        public NodeProgram(NodeDecls decls, NodeStmts stmts) {
            this.decls = decls;
            this.stmts = stmts;
        }

        @Override
        public void display() {
            decls.display();
            System.out.println("AST statements");
            stmts.display();
        }

        @Override
        public String generateCode() {
            generatedCode.add(".data");
            decls.generateCode();
            generatedCode.add("\n.code");
            stmts.generateCode();
            return "";
        }
    }

    // AbstractSyntaxTree member variable to hold the root node
    private NodeProgram root;

    // Member variable to hold generated code lines
    private List<String> generatedCode = new ArrayList<>();

    private int nextIntRegister = 1; // ri1, ri2, etc.
    private Map<String, String> variableRegisterMap = new HashMap<>();

    // Getter and setter for the root NodeProgram
    public NodeProgram getRoot() {
        return root;
    }

    public void setRoot(NodeProgram root) {
        this.root = root;
    }

    // Display the AST starting from the root
    public void display() {
        if (root != null) {
            root.display();
        }
    }
    // Method to generate the full code from the AST
    public String getCode() {
        generatedCode.clear(); // Clear any previously generated code
        if (root != null) {
            root.generateCode(); // Start code generation from the root
        }
        return String.join("\n", generatedCode); // Concatenate all lines
    }
}
