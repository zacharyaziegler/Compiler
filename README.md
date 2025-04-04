# Pseudo Assembly Compiler Project

This project is a compiler developed as part of a compiler construction course. It translates a custom high-level language into pseudo assembly code, then uses a custom obfuscated assembler library to generate Java bytecode and execute the compiled program.

**Important Note:**
This project relies on the custom library PseudoAssemblyObf.jar, which was provided by the course instructor solely for educational use. Redistribution of this library is not permitted. As such, it has not been included in this repository. 
## Project Overview

- **Parsing:**  
  A custom parser (`MyParser`) and scanner (`MyScanner`) tokenize and parse high-level source code to build an Abstract Syntax Tree (AST) defined in `AbstractSyntaxTree.java`.

- **AST & Code Generation:**  
  The AST comprises various node classes (e.g., for integer literals, arithmetic, print statements, etc.) that traverse the tree to generate pseudo assembly code.

- **Compilation & Execution:**  
  The generated pseudo assembly is passed to a custom library (`PseudoAssemblyObf.jar`) that parses the pseudo assembly, produces Java bytecode, and executes the program.

## Pseudo Assembly Language (Integer Subset)

The pseudo assembly language used in this project has two main sections: `.data` for declarations and `.code` for executable instructions. Below is a concise overview of the supported instructions (integers only):

### Sections
- **`.data`**: Declare variables here. Must appear before the code.
- **`.code`**: Contains all executable instructions.

### Comments
- `;` — Anything following a semicolon on a line is ignored.

### Variable Declaration
- `var int <id>` — Declares an integer variable named `<id>` in the `.data` section.

### Load & Store
- `loadintvar <reg>, <id>` — Loads the value of variable `<id>` into register `<reg>`.
- `loadintliteral <reg>, <num>` — Loads an integer literal `<num>` into register `<reg>`.
- `storeintvar <reg>, <id>` — Stores the value in register `<reg>` into variable `<id>`.

### Arithmetic
- `add <r1>, <r2>, <r3>` — Computes `<r3> = <r1> + <r2>`.
- `sub <r1>, <r2>, <r3>` — Computes `<r3> = <r1> - <r2>`.
- `mul <r1>, <r2>, <r3>` — Computes `<r3> = <r1> * <r2>`.
- `dec <r1>` — Decrements the value in `<r1>` by 1.
- `inc <r1?` — Increments the value in `<r1>` by 1.

### Print
- `print <reg>` or `print <id>` — Prints the integer value from the specified register or variable.

### Labels & Branching
- `:label` — Defines a label.
- `branch <reg1>, <reg2>, <label>` — Unconditionally jumps to `<label>`.
- `bne <reg1>, <reg2>, <label>` — Jumps to `<label>` if `<reg1>` is not equal to `<reg2>`.
- `be <reg1>, <reg2>, <label>` — Jumps to `<label>` if `<reg1>` equals `<reg2>`.
- `bgt <reg1>, <reg2>, <label>` — Jumps to `<label>` if `<reg1>` is greater than `<reg2>`.
- `blt <reg1>, <reg2>, <label>` — Jumps to `<label>` if `<reg1>` is less than `<reg2>`.

## Project Structure

- **AbstractSyntaxTree.java:** Defines the AST node classes and implements code generation.
- **Main.java:** The entry point that reads a high-level source file (`testHighLevelX.txt`), parses it, generates pseudo assembly, and triggers bytecode compilation and execution.
- **MyParser.java & MyScanner.java:** Implement the parser and lexical analyzer for the custom high-level language.
- **SymbolTableItem.java:** Represents entries in the symbol table.
- **PseudoAssemblyObf.jar:** A custom obfuscated library (provided for the course) that parses pseudo assembly, generates Java bytecode, and executes the compiled program.
