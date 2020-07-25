package edu.citadel.compiler;


/**
 * Class for exceptions encountered during preprocessing phase of the assembler.
 */
public class PreprocessorException extends Exception
  {
    private static final long serialVersionUID = 5714933818029616070L;


    /**
     * Construct a preprocessor exception with the specified message and line number.
     */
    public PreprocessorException(String message, int lineNum)
      {
        super("*** Preprocessor error detected on line " + lineNum + ":\n    " + message);
      }
  }
