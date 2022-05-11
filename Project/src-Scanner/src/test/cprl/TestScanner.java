package test.cprl;


import edu.citadel.compiler.Source;
import edu.citadel.cprl.Scanner;
import edu.citadel.cprl.Symbol;
import edu.citadel.cprl.Token;

import java.io.*;
import java.nio.charset.StandardCharsets;


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

            System.out.println("starting main loop...");
            System.out.println();

            String     fileName = args[0];
            FileReader reader   = new FileReader(fileName, StandardCharsets.UTF_8);
            Source     source   = new Source(reader);
            Scanner    scanner  = new Scanner(source);
            Token      token;

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
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.printf("line: %2d   char: %2d   token: ",
            token.getPosition().getLineNumber(),
            token.getPosition().getCharNumber());

        Symbol symbol = token.getSymbol();
        if (symbol.isReservedWord())
            out.print("Reserved Word -> ");
        else if (symbol == Symbol.identifier    || symbol == Symbol.intLiteral
              || symbol == Symbol.stringLiteral || symbol == Symbol.charLiteral)
            out.print(token.getSymbol().toString() + " -> ");

        out.println(token.getText());
      }


    private static void printUsageAndExit()
      {
        System.out.println("Usage: java test.cprl.TestScanner <test file>");
        System.out.println();
        System.exit(0);
      }
  }
