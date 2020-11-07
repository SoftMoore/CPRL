package edu.citadel.sel;


import edu.citadel.compiler.Source;
import edu.citadel.sel.ast.Context;
import edu.citadel.sel.ast.Program;

import java.io.Console;
import java.io.FileReader;
import java.io.StringReader;
import java.io.IOException;


public class Interpreter
  {
    private static Context context = new Context();


    public static void main(String[] args) throws IOException, InterpreterException
      {
        if (args.length == 1)
          {
            // run SEL program whose file name is given in the argument
            String filePath = args[0];
            FileReader reader  = new FileReader(filePath);
            Source  source  = new Source(reader);
            Scanner scanner = new Scanner(source);
            Parser  parser  = new Parser(scanner);
            Context context = new Context();
            Program program = parser.parseProgram();
            System.out.println(program.interpret(context));
          }
        else
          {
            // run interactive interpreter
            String exprStr   = "";
            double exprValue = 0.0;
            Console console = System.console();
            
            System.out.println("Starting SEL interpreter.  Enter \":q\" to quit.");

            while (exprStr != null && !exprStr.equals(":q"))
              {
                prompt();
                exprStr = console.readLine();
                if (exprStr != null && !exprStr.equals(":q"))
                  {
                    exprValue = interpret(exprStr);
                    System.out.println(exprValue);
                  }
              }
          }
      }


    /**
     * Print the screen prompt.
     */
    private static void prompt()
      {
        System.out.print("SEL> ");
      }
  

    private static double interpret(String expression)
        throws IOException, InterpreterException
      {
        StringReader reader = new StringReader(expression);
        Source  source  = new Source(reader);
        Scanner scanner = new Scanner(source);
        Parser  parser  = new Parser(scanner);
        Program program = parser.parseProgram();
        return program.interpret(context);
      }
  }
