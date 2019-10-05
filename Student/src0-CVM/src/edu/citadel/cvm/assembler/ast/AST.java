package edu.citadel.cvm.assembler.ast;


import java.io.IOException;
import java.io.OutputStream;

import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.Position;


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
     * Returns the OutputStream to be used for code generation
     */
    public OutputStream getOutputStream()
      {
        return out;
      }


    /**
     * Creates/returns a new constraint exception with the specified position and message. 
     */
    protected ConstraintException error(Position errorPos, String errorMsg)
      {
        return new ConstraintException(errorPos, errorMsg);
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
