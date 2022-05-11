package test.cprl;


import edu.citadel.compiler.Source;
import edu.citadel.compiler.ErrorHandler;

import edu.citadel.cprl.Scanner;
import edu.citadel.cprl.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * Test the Parser for the CPRL programming language.
 */
public class TestParser
  {
    private static final String SUFFIX  = ".cprl";
    private static final int    FAILURE = -1;


    public static void main(String args[]) throws Exception
      {
        // check arguments
        if (args.length != 1)
            printUsageAndExit();

        String fileName = args[0];
        FileReader fileReader = null;

        try
          {
            fileReader = new FileReader(fileName, StandardCharsets.UTF_8);
          }
        catch (FileNotFoundException e)
          {
            // see if we can find the file by appending the suffix
            int index = fileName.lastIndexOf('.');

            if (index < 0 || !fileName.substring(index).equals(SUFFIX))
              {
                try
                  {
                    fileName += SUFFIX;
                    fileReader = new FileReader(fileName, StandardCharsets.UTF_8);
                  }
                catch (FileNotFoundException ex)
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

        printProgressMessage("Parsing " + fileName + "...");

        Source  source  = new Source(fileReader);
        Scanner scanner = new Scanner(source);
        Parser  parser  = new Parser(scanner);

        ErrorHandler errorHandler = ErrorHandler.getInstance();
        parser.parseProgram();

        if (errorHandler.errorsExist())
          {
            errorHandler.printMessage("Errors detected in " + fileName
                                    + " -- parsing terminated.");
          }
        else
            printProgressMessage("Parsing complete.");

        System.out.println();
      }


    private static void printProgressMessage(String message)
      {
         System.out.println(message);
      }


    private static void printUsageAndExit()
      {
        System.out.println("Usage: java test.cprl.TestParser <CPRL source file>");
        System.out.println();
        System.exit(0);
      }
  }
