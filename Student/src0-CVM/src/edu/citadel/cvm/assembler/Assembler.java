package edu.citadel.cvm.assembler;


import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Source;
import edu.citadel.cvm.assembler.ast.AST;
import edu.citadel.cvm.assembler.ast.Instruction;
import edu.citadel.cvm.assembler.ast.Program;

import java.io.*;
import java.util.List;


/**
 * Assembler for the CPRL Virtual Machine.
 */
public class Assembler
  {
    private static final boolean DEBUG = false;

    private static final String  SUFFIX  = ".asm";
    private static final int     FAILURE = -1;

    private static boolean OPTIMIZE = true;
    
    private File sourceFile;
    

    public static void main(String args[]) throws Exception
      {
        // check args
        if (args.length == 0 || args.length > 2)
            printUsageAndExit();
        
        String fileName = args[0];

        if (args.length == 2)
          {
            fileName = args[1];

            if (args[0].equals("-opt:off"))
                OPTIMIZE = false;
            else if (!args[0].equals("-opt:on"))
                printUsageAndExit();
          }

        File sourceFile = new File(fileName);

        if (!sourceFile.isFile())
          {
            // see if we can find the file by appending the suffix
            int index = fileName.lastIndexOf('.');

            if (index < 0 || !fileName.substring(index).equals(SUFFIX))
              {
                fileName += SUFFIX;
                sourceFile = new File(fileName);
                    
                if (!sourceFile.isFile())
                  {
                    System.err.println("*** File " + fileName + " not found ***");
                    System.exit(FAILURE);
                  }
              }
            else
              {
                // don't try to append the suffix
                System.err.println("*** File " + fileName + " not found ***");
                System.exit(FAILURE);
              }
          }

        Assembler assembler = new Assembler(sourceFile);
        assembler.assemble();

        System.out.println();
      }


    /**
     * Construct an assembler with the specified source file. 
     */
    public Assembler(File sourceFile)
      {
        this.sourceFile = sourceFile;
      }

    
    /**
     * Assembles the source file.  If there are no errors in the source file,
     * the object code is placed in a file with the same base file name as
     * the source file but with a ".obj" suffix.
     * 
     * @throws IOException if there are problems reading the source file
     *                     or writing to the target file.
     */
    public void assemble() throws IOException
      {
        FileReader sourceFileReader = new FileReader(sourceFile);
        
        Source  source  = new Source(sourceFileReader);
        Scanner scanner = new Scanner(source);
        Parser  parser  = new Parser(scanner);

        ErrorHandler errorHandler = ErrorHandler.getInstance();
      
        printProgressMessage("Starting assembly for " + sourceFile.getName() + "...");

        // parse source file
        Program prog = parser.parseProgram();

        // optimize
        if (!errorHandler.errorsExist() && OPTIMIZE)
          {
            printProgressMessage("Performing optimizations...");
            prog.optimize();
          }

        if (DEBUG)
          {
            System.out.println("Program after checking constraints");
            printInstructions(prog.getInstructions());
          }

        // set addresses
        if (!errorHandler.errorsExist())
          {
            printProgressMessage("Setting memory addresses...");
            prog.setAddresses();
          }

        // check constraints
        if (!errorHandler.errorsExist())
          {
            printProgressMessage("Checking constraints...");
            prog.checkConstraints();
          }

        // generate code
        if (!errorHandler.errorsExist())
          {
            printProgressMessage("Generating code...");
            AST.setOutputStream(getTargetOutputStream(sourceFile));

            // no error recovery from errors detected during code generation
            prog.emit();
          }

        if (errorHandler.errorsExist())
            printProgressMessage("*** Errors detected -- assembly terminated. ***");
        else
            printProgressMessage("Compilation complete.");        
      }


    /**
     * This method is useful for debugging.
     *  
     * @param instructions the list of instructions to print
     */
    private static void printInstructions(List<Instruction> instructions)
      {
        if (instructions == null)
            System.out.println("<no instructions>");
        else
          {
            System.out.println("There are " + instructions.size() + " instructions");
            for (Instruction instruction : instructions)
                System.out.println(instruction);
            System.out.println();
          }
      }

    private static void printProgressMessage(String message)
      {
         System.out.println(message);
      }


    private static void printUsageAndExit()
      {
        System.out.println("Usage: java Assembler <option> <source file>");
        System.out.println("where the option is omitted or is one of the following:");
        System.out.println("-opt:off    Turns off all assembler optimizations");
        System.out.println("-opt:on     Turns on all assembler optimizations (default)");
        System.out.println();
        System.exit(0);
      }
    
    private OutputStream getTargetOutputStream(File sourceFile)
      {
        // get source file name minus the suffix
        String baseName = sourceFile.getName();
        int suffixIndex = baseName.lastIndexOf(SUFFIX);
        if (suffixIndex > 0)
            baseName = sourceFile.getName().substring(0, suffixIndex);

        String targetFileName = baseName + ".obj";

        File targetFile = null;
        OutputStream targetStream = null;

        try
          {
            targetFile = new File(sourceFile.getParent(), targetFileName);
            targetStream = new FileOutputStream(targetFile);
          }
        catch (IOException e)
          {
            e.printStackTrace();
            System.exit(FAILURE);
          }

        return targetStream;
      }
  }
