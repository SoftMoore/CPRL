package edu.citadel.cvm.assembler.ast;


import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cvm.assembler.Token;

import java.util.List;


/**
 * This class serves as a base class for the abstract syntax
 * tree for an assembly language instruction with no arguments.
 */
public abstract class InstructionNoArgs extends Instruction
  {
    public InstructionNoArgs(List<Token> labels, Token opCode)
      {
        super(labels, opCode, null);
      }


    @Override
    public void checkConstraints()
      {
        try
          {
            assertOpCode();
            checkLabels();
          }
        catch (ConstraintException e)
          {
            ErrorHandler.getInstance().reportError(e);
          }
      }


    @Override
    protected int getArgSize()
      {
        return 0;
      }
  }
