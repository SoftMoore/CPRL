package edu.citadel.cvm.assembler.ast;


import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.util.ByteUtil;
import edu.citadel.cvm.Constants;
import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;

import java.util.*;
import java.io.*;


/**
 * This abstract class implements common methods for the abstract
 * syntax tree of a single assembly language instruction.
 */
public abstract class Instruction extends AST
  {
    // Maps label text (type String) to an address (type Integer).
    // Note that the label text always includes the colon (:) at the end.
    protected static Map<String, Integer> labelMap = new HashMap<>();

    // Maps identifier text (type String) to a stack address (type Integer).
    protected static Map<String, Integer> idMap = new HashMap<>();

    // Initialize address for identifiers (e.g., used in DEFINT).
    private static int idAddress = Constants.BYTES_PER_FRAME;

    private List<Token> labels;
    private Token opCode;
    protected Token arg;

    private int address;


    public Instruction(List<Token> labels, Token opCode, Token arg)
      {
        this.labels = labels;
        this.opCode = opCode;
        this.arg    = arg;
      }


    public List<Token> getLabels()
      {
        return labels;
      }


    public Token getOpCode()
      {
        return opCode;
      }


    public Token getArg()
      {
        return arg;
      }


    /**
     * Sets the memory address and defines label values for an instruction.
     */
    public void setAddress(int address) throws ConstraintException
      {
        this.address = address;

        // define addresses for labels
        for (Token label : labels)
          {
            if (labelMap.containsKey(label.getText()))
              {
                String errorMsg  = "This label has already been defined.";
                throw error(label.getPosition(), errorMsg);
              }
            else
                labelMap.put(label.getText(), Integer.valueOf(address));
              }
      }


    /**
     * Returns the address of this instruction. 
     */
    public int getAddress()
      {
        return address;
      }


    /**
     * Map the text of the identifier token to an address on the stack.
     */
    public void defineIdAddress(Token identifier, int size) throws ConstraintException
      {
        assert identifier != null : "id can't be null";
        assert identifier.getSymbol() == Symbol.identifier :
            "expecting an identifier but found " + identifier.getSymbol();

        if (idMap.containsKey(identifier.getText()))
          {
            String errorMsg  = "This identifier has already been defined.";
            throw error(identifier.getPosition(), errorMsg);
          }
        else
          {
            idMap.put(identifier.getText(), Integer.valueOf(idAddress));
            idAddress = idAddress + size;
          }
      }


    /**
     * Returns the stack address associated with an identifier.
     */
    protected int getIdAddress(Token identifier)
      {
        assert identifier != null : "id can't be null";
        assert identifier.getSymbol() == Symbol.identifier :
            "expecting an identifier but found " + identifier.getSymbol();
        assert idMap.containsKey(identifier.getText()) :
            "identifier " + identifier.getText() + " not found";

        Integer idAddress = (Integer) idMap.get(identifier.getText());

        return idAddress.intValue();
      }


    /**
     * Returns the number of bytes in memory occupied by the argument.
     */
    protected abstract int getArgSize();


    /**
     * Returns the number of bytes in memory occupied by the instruction,
     * computed as 1 (for the opcode) plus the sizes of the operands.
     */
    public int getSize()
      {
        return Constants.BYTES_PER_OPCODE + getArgSize();
      }


    /**
     * Checks that each label has a value defined in the label map.  This method
     * should not be called for an instruction before method setAddress().
     * @throws ConstraintException if the instruction has a label that
     *         is not defined in the label map.
     */
    protected void checkLabels() throws ConstraintException
      {
        for (Token label : labels)
          {
            if (!labelMap.containsKey(label.getText()))
              {
                String errorMsg = "label \"" + label.getText() + "\" has not been defined.";
                throw error(label.getPosition(), errorMsg);
              }
          }
      }


    /**
     * Calculates the displacement between an instruction's address and
     * a label (computed as label's address - instruction's address).
     * This method is used by branching and call instructions.
     */
    protected int getDisplacement(Token labelArg)
      {
        String labelId = labelArg.getText() + ":";

        assert labelMap.containsKey(labelId) :
            "label " + labelArg.getText() + " not found";

        Integer labelAddress = (Integer) labelMap.get(labelId);

        return (labelAddress.intValue() - address);
      }


    /**
     * Asserts that the opCode token of the instruction has
     * the correct Symbol.  Implemented in each instruction
     * by calling the method assertOpCode(Symbol).
     */
    protected abstract void assertOpCode();


    protected void assertOpCode(Symbol opCode)
      {
        assert this.opCode != null : "null opCode";
        assert this.opCode.getSymbol() == opCode : "wrong opCode";
      }


    public String toString()
      {
        StringBuffer buffer = new StringBuffer(100);

        // for now simply print the instruction
        if (labels != null)
          {
            for (Token label : labels)
               buffer.append(label.getText() + "\n");
          }

        buffer.append("   " + opCode.getText());

        if (arg != null)
            buffer.append(" " + arg.getText());

        return buffer.toString();
      }


     /**
      * emit the opCode for the instruction
      */
    protected void emit(byte opCode) throws IOException
      {
        getOutputStream().write(opCode);
      }


     /**
      * emit an integer argument for the instruction
      */
    protected void emit(int arg) throws IOException
      {
        getOutputStream().write(ByteUtil.intToBytes(arg));
      }


     /**
      * emit a character argument for the instruction
      */
    protected void emit(char arg) throws IOException
      {
        getOutputStream().write(ByteUtil.charToBytes(arg));
      }
  }
