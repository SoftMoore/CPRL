package edu.citadel.cprl;


/**
 * An enum class for the two declaration scope levels in CPRL.
 */
public enum ScopeLevel
  {
    PROGRAM("Program"),
    SUBPROGRAM("Subprogram");

    private String text;


    private ScopeLevel(String text)
      {
        this.text = text;
      }


    /**
     * Returns a "nice" string for the name of the scope level.  For
     * example, this method returns "Program" instead of PROGRAM.
     */
    public String toString()
      {
        return text;
      }
  }
