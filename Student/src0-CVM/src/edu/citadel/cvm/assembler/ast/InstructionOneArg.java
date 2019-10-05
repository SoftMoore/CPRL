package edu.citadel.cvm.assembler.ast;


import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
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
     * Checks that the argument of the instruction has
     * the correct type.  Implemented in each instruction
     * by calling the method checkArgType(Symbol).
     */
    protected abstract void checkArgType() throws ConstraintException;
  }
