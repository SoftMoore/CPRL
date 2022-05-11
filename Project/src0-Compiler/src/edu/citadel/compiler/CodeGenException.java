package edu.citadel.compiler;


/**
 * Class for exceptions encountered during code generation.
 */
public class CodeGenException extends CompilerException
  {
    private static final long serialVersionUID = 5227233416865236056L;


    /**
     * Construct a CodeGenException with the specified error message and position.
     */
    public CodeGenException(Position position, String message)
      {
        super("Code Generation", position, message);
      }


    /**
     * Construct a CodeGenException with the specified error message.
     */
    public CodeGenException(String message)
      {
        super("Code Generation", message);
      }
  }
