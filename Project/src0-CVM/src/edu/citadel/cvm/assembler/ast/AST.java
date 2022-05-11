package edu.citadel.cvm.assembler.ast;


import java.io.IOException;
import java.io.OutputStream;

import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.Position;
import edu.citadel.compiler.util.ByteUtil;


/**
 * Base class for all abstract syntax trees
 */
public abstract class AST
  {
    private static OutputStream out = null;


    /**
     * Default constructor.
     */
    public AST()
      {
        super();
      }


    /**
     * Set the OutputStream to be used for code generation.
     */
    public static void setOutputStream(OutputStream out)
      {
        AST.out = out;
      }


    /**
     * Creates/returns a new constraint exception with the specified position and message. 
     */
    protected ConstraintException error(Position errorPos, String errorMsg)
      {
        return new ConstraintException(errorPos, errorMsg);
      }


    /**
     * emit the opCode for the instruction
     */
   protected void emit(byte opCode) throws IOException
     {
       out.write(opCode);
     }


    /**
     * emit an integer argument for the instruction
     */
   protected void emit(int arg) throws IOException
     {
       out.write(ByteUtil.intToBytes(arg));
     }


    /**
     * emit a character argument for the instruction
     */
   protected void emit(char arg) throws IOException
     {
       out.write(ByteUtil.charToBytes(arg));
     }


   /**
    * check semantic/contextual constraints
    */
   public abstract void checkConstraints();


   /**
    * emit the object code for the AST
    */
   public abstract void emit() throws IOException;
  }
