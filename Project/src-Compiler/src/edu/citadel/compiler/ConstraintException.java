package edu.citadel.compiler;


/**
 * Class for exceptions encountered during constraint analysis.
 */
public class ConstraintException extends CompilerException
  {
    private static final long serialVersionUID = 5793802201497958837L;

    /**
     * Construct a ConstraintException with the specified error message and position.
     */
    public ConstraintException(Position position, String message)
      {
        super("Constraint", position, message);
      }
  }
