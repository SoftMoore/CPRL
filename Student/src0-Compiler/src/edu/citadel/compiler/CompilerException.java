package edu.citadel.compiler;


/**
 * Superclass for all compiler exceptions.
 */
public abstract class CompilerException extends Exception
  {
    private static final long serialVersionUID = -6999301636930707946L;


    /**
     * Construct an exception with information about the nature and position
     * of the error.
     * 
     * @param errorType the name of compilation phase in which the error was detected.
     * @param position  the position in the source file where the error was detected.
     * @param message   a brief message about the nature of the error.
     */
    public CompilerException(String errorType, Position position, String message)
      {
        super("*** " + errorType + " error detected near " + position + ":\n    " + message);
      }


    /**
     * Construct an exception with information about the nature of the error
     * but not its position.
     * 
     * @param errorType the name of compilation phase in which the error was detected.
     * @param message   a brief message about the nature of the error.
     */
    public CompilerException(String errorType, String message)
      {
        super("*** " + errorType + " error detected: " + message);
      }
  }
