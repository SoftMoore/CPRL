package test.cprl;


import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Source;
import edu.citadel.cprl.Scanner;
import edu.citadel.cprl.Symbol;
import edu.citadel.cprl.Token;

import java.io.*;


public class TestScanner
  {
    public static void main(String[] args)
      {
        try
          {
            // check arguments
            if (args.length != 1)
                printUsageAndExit();

            System.out.println("initializing...");
            System.out.println();

            String fileName = args[0];
            FileReader fileReader = new FileReader(fileName);
            // write error messages to System.out
            ErrorHandler errorHandler = ErrorHandler.getInstance();
            errorHandler.setPrintWriter(new PrintWriter(System.out, true));

            System.out.println("starting main loop...");
            System.out.println();

            Source  source  = new Source(fileReader);
            Scanner scanner = new Scanner(source);
            Token   token;

            do
              {
                token = scanner.getToken();
                printToken(token);
                scanner.advance();
              }
            while (token.getSymbol() != Symbol.EOF);

            System.out.println();
            System.out.println("...done");
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }


    public static void printToken(Token token)
      {
        System.out.printf("line: %2d   char: %2d   token: ",
            token.getPosition().getLineNumber(),
            token.getPosition().getCharNumber());

        Symbol symbol = token.getSymbol();
        if (symbol.isReservedWord())
            System.out.print("Reserved Word -> ");
        else if (symbol == Symbol.identifier    || symbol == Symbol.intLiteral
              || symbol == Symbol.stringLiteral || symbol == Symbol.charLiteral)
            System.out.print(token.getSymbol().toString() + " -> ");

        System.out.println(token.getText());
      }


    private static void printUsageAndExit()
      {
        System.out.println("Usage: java test.cprl.TestScanner <test file>");
        System.out.println();
        System.exit(0);
      }
  }
