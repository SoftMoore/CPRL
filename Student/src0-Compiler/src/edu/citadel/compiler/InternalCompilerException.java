package edu.citadel.compiler;


/**
 * Class for exceptions encountered within the compiler.  These
 * exceptions represent problems with the implementation of the compiler
 * and should never occur if the compiler is implemented correctly.
 */
public class InternalCompilerException extends RuntimeException
  {
    private static final long serialVersionUID = 171627814281066792L;


    /**
     * Construct an InternalCompilerException with the specified error message and position.
     */
    public InternalCompilerException(Position position, String errorMessage)
      {
        super("*** Internal Compiler Error near line " + position + ":\n    " + errorMessage);
      }
  }
