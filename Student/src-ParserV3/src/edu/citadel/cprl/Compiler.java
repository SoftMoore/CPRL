package edu.citadel.cprl;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Source;
import edu.citadel.cprl.ast.AST;
import edu.citadel.cprl.ast.Program;

import java.io.*;


/**
 * Compiler for the CPRL programming language.
 */
public class Compiler
  {
    private static final String SUFFIX  = ".cprl";
    private static final int    FAILURE = -1;
    
    private File sourceFile;


    /**
     * Construct a compiler with the specified source file.
     */
    public Compiler(File sourceFile)
      {
        this.sourceFile = sourceFile;
      }


    /**
     * Compile the source file.  If there are no errors in the source file,
     * the object code is placed in a file with the same base file name as
     * the source file but with a ".obj" suffix.
     * 
     * @throws IOException if there are problems reading the source file
     *                     or writing to the target file.
     */
    public void compile() throws IOException
      {
        FileReader sourceFileReader = new FileReader(sourceFile);

        Source  source  = new Source(sourceFileReader);
        Scanner scanner = new Scanner(source);
        Parser  parser  = new Parser(scanner);

        ErrorHandler errorHandler = ErrorHandler.getInstance();

        printProgressMessage("Starting compilation for " + sourceFile.getName() + "...");

        // parse source file
        Program program = parser.parseProgram();

        // check constraints
        if (!errorHandler.errorsExist())
          {
            printProgressMessage("Checking constraints...");
            program.checkConstraints();
          }

        // generate code
        if (!errorHandler.errorsExist())
          {
            printProgressMessage("Generating code...");

            // no error recovery from errors detected during code generation
            try
              {
                PrintWriter targetPrintWriter = getTargetPrintWriter(sourceFile);
                if (targetPrintWriter != null)
                    AST.setPrintWriter(targetPrintWriter);
                else
                  {
                    String errorMsg = "unable to create target output stream";
                    throw new CodeGenException(errorMsg);
                  }

                program.emit();
              }
            catch (CodeGenException ex)
              {
                ErrorHandler.getInstance().reportError(ex);            
              }
          }

        if (errorHandler.errorsExist())
            printProgressMessage("Errors detected -- compilation terminated.");
        else
            printProgressMessage("Compilation complete.");
      }
    

    /**
     * This method drives the compilation process.
     * 
     * @param args must include the name of the CPRL source file, either the complete
     *             file name or the base file name with suffix ".cprl" omitted.
     */
    public static void main(String args[]) throws Exception
      {
        if (args.length == 0 || args.length > 1)
            printUsageAndExit();

        String fileName = args[0];
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

        Compiler compiler = new Compiler(sourceFile);
        compiler.compile();

        System.out.println();
      }


    /**
     * Returns a PrintWriter used for writing the assembly code.  The target
     * print writer writes to a file with the same base file name as the source
     * file but with a ".asm" suffix. 
     */
    private PrintWriter getTargetPrintWriter(File sourceFile)
      {
        // get source file name minus the suffix
        String baseName = sourceFile.getName();
        int suffixIndex = baseName.lastIndexOf(SUFFIX);
        if (suffixIndex > 0)
            baseName = sourceFile.getName().substring(0, suffixIndex);

        String targetFileName = baseName + ".asm";

        File targetFile = null;
        PrintWriter writer = null;

        try
          {
            targetFile = new File(sourceFile.getParent(), targetFileName);
            writer = new PrintWriter(new FileWriter(targetFile), true);
          }
        catch (IOException e)
          {
            e.printStackTrace();
            System.exit(FAILURE);
          }

        return writer;
      }


    private static void printProgressMessage(String message)
      {
        System.out.println(message);
      }


    private static void printUsageAndExit()
      {
        System.out.println("Usage: java edu.citadel.cprl.Compiler <source file>");
        System.out.println();
        System.exit(0);
      }
  }
