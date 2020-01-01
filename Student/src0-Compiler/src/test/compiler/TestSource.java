package test.compiler;


import java.io.*;

import edu.citadel.compiler.*;


public class TestSource
  {
    public static void main(String[] args)
      {
        try
          {
            String fileName = args[0];
            FileReader fileReader = new FileReader(fileName);
            Source source = new Source(fileReader);

            while (source.getChar() != Source.EOF)
              {
                int c = source.getChar();

                if (c == '\n')
                    System.out.print("\\n");
                else if (c != '\r')
                    System.out.print((char) c);

                System.out.println("\t" + source.getCharPosition());

                source.advance();
              }
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }
  }

