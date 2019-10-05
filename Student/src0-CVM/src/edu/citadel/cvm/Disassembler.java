package edu.citadel.cvm;


import edu.citadel.compiler.util.ByteUtil;
import edu.citadel.compiler.util.StringUtil;

import java.io.*;


public class Disassembler
  {
    private static final String SUFFIX   = ".obj";
    private static final int FIELD_WIDTH = 4;

    public static final int EOF = -1;


    public static void main(String[] args) throws IOException
      {
        // check args
        if (args.length == 0 || args.length > 1)
            printUsageAndExit();

        String fileName = args[0];
        FileInputStream file = new FileInputStream(fileName);

        // get object code file name minus the suffix
        int suffixIndex = fileName.lastIndexOf(SUFFIX);
        String baseName = fileName.substring(0, suffixIndex);

        String outputFileName = baseName + ".dis.txt";
        FileWriter  fw  = new FileWriter(outputFileName);
        PrintWriter out = new PrintWriter(new BufferedWriter(fw), true);
        
        System.out.println("disassembling " + fileName + " to " + outputFileName);

        int inByte;
        int opCodeAddr = 0;
        int strLength  = 0;

        char c;

        while ((inByte = file.read()) != -1)
          {
            byte opCode = (byte) inByte;

            switch (opCode)
              {
                // opcodes with zero operands
                case OpCode.ADD:
                case OpCode.CMP:
                case OpCode.DEC:
                case OpCode.DIV:
                case OpCode.GETCH:
                case OpCode.GETINT:
                case OpCode.HALT:
                case OpCode.LOADB:
                case OpCode.LOAD2B:
                case OpCode.LOADW:
                case OpCode.LDCB0:
                case OpCode.LDCB1:
                case OpCode.LDCINT0:
                case OpCode.LDCINT1:
                case OpCode.INC:
                case OpCode.MOD:
                case OpCode.MUL:
                case OpCode.NEG:
                case OpCode.NOT:
                case OpCode.PUTBYTE:
                case OpCode.PUTCH:
                case OpCode.PUTINT:
                case OpCode.PUTEOL:
                case OpCode.PUTSTR:
                case OpCode.STOREB:
                case OpCode.STORE2B:
                case OpCode.STOREW:
                case OpCode.SUB:
                    out.println(StringUtil.format(opCodeAddr, FIELD_WIDTH) + ":  "
                        + OpCode.toString(opCode));
                    opCodeAddr = opCodeAddr + 1;
                    break;

                // opcodes with one byte operand
                case OpCode.SHL:
                case OpCode.SHR:
                case OpCode.LDCB:
                    out.print(StringUtil.format(opCodeAddr, FIELD_WIDTH) + ":  "
                        + OpCode.toString(opCode));
                    out.println(" " + readByte(file));
                    opCodeAddr = opCodeAddr + 2;  // one byte for opcode and one byte for shift amount
                  break;
                    
                // opcodes with one int operand
                case OpCode.ALLOC:
                case OpCode.BR:
                case OpCode.BG:
                case OpCode.BGE:
                case OpCode.BL:
                case OpCode.BLE:
                case OpCode.BNZ:
                case OpCode.BZ:
                case OpCode.CALL:
                case OpCode.LOAD:
                case OpCode.LDCINT:
                case OpCode.LDLADDR:
                case OpCode.LDGADDR:
                case OpCode.PROC:
                case OpCode.PROGRAM:
                case OpCode.RET:
                case OpCode.STORE:
                    out.print(StringUtil.format(opCodeAddr, FIELD_WIDTH) + ":  "
                        + OpCode.toString(opCode));
                    out.println(" " + readInt(file));
                    opCodeAddr = opCodeAddr + 1 + Constants.BYTES_PER_INTEGER;
                    break;

                // special case: LDCCH
                case OpCode.LDCCH:
                    out.print(StringUtil.format(opCodeAddr, FIELD_WIDTH) + ":  "
                        + OpCode.toString(opCode));
                    out.print(" \'");
                    
                    c = readChar(file);
                    if (c == '\b' || c == '\t' || c == '\n' || c == '\f' 
                     || c == '\r' || c == '\"' || c == '\'' || c == '\\')
                        out.print(getUnescapedChar(c));
                    else
                        out.print(c);

                    out.println("\'");
                    opCodeAddr = opCodeAddr + 1 + Constants.BYTES_PER_CHAR;
                    break;

                // special case: LDCSTR
                case OpCode.LDCSTR:
                    out.print(StringUtil.format(opCodeAddr, FIELD_WIDTH) + ":  "
                        + OpCode.toString(opCode));
                    // now print the string
                    out.print("  \"");
                    strLength = readInt(file);
                    for (int i = 0;  i < strLength;  ++i)
                      {
                        c = readChar(file);
                        if (c == '\b' || c == '\t' || c == '\n' || c == '\f' 
                         || c == '\r' || c == '\"' || c == '\'' || c == '\\')
                            out.print(getUnescapedChar(c));
                        else
                            out.print(c);
                      }
                    out.println("\"");
                    opCodeAddr = opCodeAddr + 1 + Constants.BYTES_PER_INTEGER
                                 + strLength*Constants.BYTES_PER_CHAR;
                    break;

                default:
                    out.println("*** Unknown opCode ***");
                    break;
              }
          }

        out.close();
      }


    /**
     * Reads an integer argument from the stream.
     */
    private static int readInt(InputStream in) throws IOException
      {
        byte b0 = (byte) in.read();
        byte b1 = (byte) in.read();
        byte b2 = (byte) in.read();
        byte b3 = (byte) in.read();

        return ByteUtil.bytesToInt(b0, b1, b2, b3);
      }


    /**
     * Reads a char argument from the stream.
     */
    private static char readChar(InputStream in) throws IOException
      {
        byte b0 = (byte) in.read();
        byte b1 = (byte) in.read();

        return ByteUtil.bytesToChar(b0, b1);
      }


    /**
     * Reads a byte argument from the stream.
     */
    private static byte readByte(InputStream in) throws IOException
      {
        return (byte) in.read();
      }


    /**
     * Unescapes characters.  For example, if the parameter c is a tab,
     * this method will return "\\t" 
     *
     * @return the string for an escaped character.
     */
    private static String getUnescapedChar(char c)
      {
        switch (c)
          {
            case '\b' : return "\\b";    // backspace
            case '\t' : return "\\t";    // tab
            case '\n' : return "\\n";    // linefeed (a.k.a. newline)
            case '\f' : return "\\f";    // form feed
            case '\r' : return "\\r";    // carriage return
            case '\"' : return "\\\"";   // double quote
            case '\'' : return "\\\'";   // single quote
            case '\\' : return "\\\\";   // backslash
            default   : return Character.toString(c);
          }
      }


    private static void printUsageAndExit()
      {
        System.out.println("Usage: java edu.citadel.cvm.Disassembler filename");
        System.out.println();
        System.exit(0);
      }
  }
