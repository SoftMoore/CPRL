package edu.citadel.compiler;


import java.io.*;


/**
 * This class handles the reporting of error messages. <br>
 * (implements the singleton pattern)
 */
public class ErrorHandler
  {
    private static ErrorHandler instance = null;

    private PrintWriter err = null;


    /**
     * Maximum number of errors to be reported.
     */
    private static int MAX_ERRORS = 15;


    private int errorCount;


    /**
     * Constructs a new ErrorHandler.
     */
    protected ErrorHandler()
      {
        errorCount = 0;
        err = new PrintWriter(System.err, true);
      }

    
    /**
     * Returns the single instance of this class. 
     */
    public static ErrorHandler getInstance()
      {
        if (instance == null)
            instance = new ErrorHandler();

        return instance;
      }


    /**
     * Sets the PrintWriter to be used for all error messages written
     * by the ErrorHandler.  (Defaults to System.err if not set.)
     */
    public void setPrintWriter(PrintWriter err)
      {
        this.err = err;
      }


    /**
     * Returns true if errors have been reported by the error handler.
     */
    public boolean errorsExist()
      {
        return errorCount > 0;
      }


    /**
     * Reports the error.  Stops compilation if the maximum number of
     * errors have been reported.
     */
    public void reportError(CompilerException e)
      {
        if (errorCount <= MAX_ERRORS)
          {
            err.println(e.getMessage());
            ++errorCount;
          }
        else
          {
            err.println("*** Max errors exceeded.  Compilation halted***");
            System.exit(1);       // stop the compiler with a nonzero status code
          }
      }


    /**
     * Reports the error and exits compilation.
     */
    public void reportFatalError(Exception e)
      {
        e.printStackTrace();
        System.exit(1);       // stop the compiler with a nonzero status code
      }


    /**
     * Reports a warning and continues compilation.
     */
    public void reportWarning(String warningMessage)
      {
        err.println("Warning: " + warningMessage);
      }
  }
