package org.example;

import program.PseudoAssemblyWithStringProgram;

import java.io.*;

/**
 * Main Class
 *
 * @author Zachary Ziegler
 */
public class Main {

    public static void main(String[] args) {
        String codeFilePath = "testHighLevel5.txt";
        StringBuilder codeBuilder = new StringBuilder();

        // Read the high-level language code from file
        try (BufferedReader br = new BufferedReader(new FileReader(new File(codeFilePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                codeBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }

        String highLevelCode = codeBuilder.toString();

        // Parse the code using MyParser to generate an AST
        MyParser parser = new MyParser();
        boolean parseSuccess = parser.parse(highLevelCode);
        if (!parseSuccess) {
            System.out.println("Failed to parse the high-level language code.");
            return;
        }

        // Generate the pseudo assembly code from the AST
        AbstractSyntaxTree ast = parser.getAbstractSyntaxTree();
        String pseudoCode = ast.getCode();
        System.out.println("Pseudo assembly code generated:\n" + pseudoCode);

        // Compile and run the pseudo assembly code
        int numVirtualRegistersInt = 32;
        int numVirtualRegistersString = 32;
        String outputClassName = "MyLabProgram";
        String outputPackageNameDot = "mypackage";
        String classRootDir = System.getProperty("user.dir") + "/target/classes";

        PseudoAssemblyWithStringProgram pseudoAssemblyProgram = new PseudoAssemblyWithStringProgram(
                pseudoCode,
                outputClassName,
                outputPackageNameDot,
                classRootDir,
                numVirtualRegistersInt,
                numVirtualRegistersString
        );

        boolean parseSuccessful = pseudoAssemblyProgram.parse();
        if (parseSuccessful) {
            System.out.println("Pseudo assembly code parsed successfully.");

            // Generate Java bytecode
            pseudoAssemblyProgram.generateBytecode();
            System.out.println("Java bytecode generated.");

            // Run the generated bytecode and show the output
            try (PrintStream outstream = new PrintStream(System.out)) {
                pseudoAssemblyProgram.run(outstream);
            } catch (Exception e) {
                System.err.println("Error while running the generated bytecode: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to parse the pseudo assembly code.");
        }
    }

}