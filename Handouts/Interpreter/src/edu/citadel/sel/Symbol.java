package edu.citadel.sel;



/**
 * This class encapsulates the symbols of the programming language SEL.
 */
public enum Symbol
  {
    // arithmetic operator symbols
    plus("+"),
    minus("-"),
    times("*"),
    divide("/"),

    // assignment, punctuation, and grouping symbols
    assign("="),
    leftParen("("),
    rightParen(")"),
    dot("."),

    // numeric literal and identifier
    numericLiteral("Numeric Literal"),
    identifier("Identifier"),

    // special scanning symbols
    EOL("End-of-Line"),
    EOF("End-of-File"),
    unknown("Unknown");


    // instance fields
    private final String label;

    
    /**
     * Constructs a new Symbol with its label.
     */
    private Symbol(String label)
      {
        this.label = label;
      }

    
    /**
     * Returns true if this symbol is an adding operator.
     */
    public boolean isAddingOperator()
      {
        return this == Symbol.plus
            || this == Symbol.minus;
      }


    /**
     * Returns true if this symbol is a multiplying operator.
     */
    public boolean isMultiplyingOperator()
      {
        return this == Symbol.times
            || this == Symbol.divide;
      }


    /**
     * Returns true if this symbol can start an expression.
     */
    public boolean isExprStarter()
      {
        return this == numericLiteral
            || this == Symbol.leftParen
            || this == Symbol.plus
            || this == Symbol.minus;
      }


    /**
     * Returns the label for this Symbol.
     */
    public String toString()
      {
        return label;
      }
  }
