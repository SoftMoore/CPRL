package edu.citadel.cvm.assembler.ast;


import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;

import java.util.List;


/**
 * This class serves as a base class for the abstract syntax
 * tree for an assembly language instruction with one argument.
 */
public abstract class InstructionOneArg extends Instruction
  {
    public InstructionOneArg(List<Token> labels, Token opCode, Token arg)
      {
        super(labels, opCode, arg);
        assert arg != null : "argument can't be null for this instruction";
      }


    /**
     * check semantic/contextual constraints
     */
    public void checkConstraints()
      {
        try
          {
            assertOpCode();
            checkLabels();
            checkArgType();
          }
        catch (ConstraintException e)
          {
            ErrorHandler.getInstance().reportError(e);
          }
      }


    /**
     * This method is called by instructions that have an argument that
     * references a label.  It verifies that the referenced label exists.
     */
    protected void checkLabelArgDefined() throws ConstraintException
      {
        if (arg.getSymbol() != Symbol.identifier)
          {
            String errorMsg = "expecting a label identifier but found " + arg.getSymbol();
            throw error(arg.getPosition(), errorMsg);
          }

        String label = arg.getText() + ":";
        if (!labelMap.containsKey(label))
          {
            String errorMsg = "label \"" + arg.getText() + "\" has not been defined.";
            throw error(arg.getPosition(), errorMsg);
          }
      }


    /**
     * This method is called by instructions that have an argument that references
     * an identifier.  It verifies that the referenced identifier exists.
     */
    protected void checkIdArgDefined() throws ConstraintException
      {
        assert arg.getSymbol() == Symbol.identifier :
            "expecting an identifier but found " + arg.getSymbol();

        if (!idMap.containsKey(arg.getText()))
          {
            String errorMsg = "identifier \"" + arg.getText() + "\" has not been defined.";
            throw error(arg.getPosition(), errorMsg);
          }
      }


    /**
     * This method is called by instructions to verify the type of its argument.  
     */
    protected void checkArgType(Symbol argType) throws ConstraintException
      {
        if (arg.getSymbol() != argType)
          {
            String errorMsg = "Invalid type for argument -- should be " + argType;
            throw error(arg.getPosition(), errorMsg);
          }
      }


    /**
     * Returns the argument as converted to an integer.  Valid
     * only for instructions with arguments of type intLiteral.
     */
    public int argToInt()
      {
        assert getArg().getSymbol() == Symbol.intLiteral :
            "can't convert argument " + getArg() + " to an integer";
        return Integer.parseInt(getArg().getText());
      }


    /**
     * Returns the argument as converted to a byte.  Valid
     * only for instructions with arguments of type intLigeral.
     */
    public byte argToByte()
      {
        assert getArg().getSymbol() == Symbol.intLiteral :
            "can't convert argument " + getArg() + " to a byte";
        return Byte.parseByte(getArg().getText());
      }


    /**
     * Checks that the argument of the instruction has
     * the correct type.  Implemented in each instruction
     * by calling the method checkArgType(Symbol).
     */
    protected abstract void checkArgType() throws ConstraintException;
  }
