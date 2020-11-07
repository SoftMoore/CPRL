package edu.citadel.sel.ast;


/**
 * Interface for all SEL expressions.
 */
public interface Expression
  {
    /**
     * Interpret the expression with the specified context.
     * 
     * @return the value of the expression.
     */
    public abstract double interpret(Context context);
  }
