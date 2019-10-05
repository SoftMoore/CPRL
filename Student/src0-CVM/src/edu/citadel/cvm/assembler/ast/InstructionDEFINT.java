package edu.citadel.cvm.assembler.ast;


import edu.citadel.compiler.ConstraintException;
import edu.citadel.cvm.Constants;
import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;

import java.util.List;
import java.io.IOException;


/**
 * This class implements the abstract syntax tree for
 * the assembly language pseudo instruction DEFINT.
 */
public class InstructionDEFINT extends InstructionOneArg
  {
    public InstructionDEFINT(List<Token> labels, Token opCode, Token arg)
      {
        super(labels, opCode, arg);
      }


    public void assertOpCode()
      {
        assertOpCode(Symbol.DEFINT);
      }


    public void checkArgType() throws ConstraintException
      {
        defineIdAddress(getArg(), Constants.BYTES_PER_INTEGER);
        checkArgType(Symbol.identifier);
      }


    /**
     * Must be overridden for pseudo opcodes since the opcode
     * doesn't actually occupy space in memory
     */
    @Override
    public int getSize()
      {
        return 0;
      }


    /**
     * Abstract method -- must be overridden even though
     * it plays no significant role for pseudo opcodes.
     */
    @Override
    protected int getArgSize()
      {
        return Constants.BYTES_PER_INTEGER;
      }


    @Override
    public void emit() throws IOException
      {
        // nothing to emit for pseudo opcode
      }
  }
