package edu.citadel.compiler;


/**
 * Class for exceptions encountered during scanning.
 */
public class ScannerException extends CompilerException
  {
    private static final long serialVersionUID = 4897417646057044051L;

    /**
     * Construct a ScannerException with the specified error message and position.
     */
    public ScannerException(Position position, String message)
      {
        super("Lexical", position, message);
      }
  }
