package edu.citadel.compiler;


/**
 * Class for exceptions encountered during parsing.
 */
public class ParserException extends CompilerException
  {
    private static final long serialVersionUID = -6997169373446585998L;

    /**
     * Construct a ParserException with the specified error message and position.
     */
    public ParserException(Position position, String message)
      {
        super("Syntax", position, message);
      }
  }
