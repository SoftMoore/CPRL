package edu.citadel.sel.ast;


import edu.citadel.sel.Symbol;
import edu.citadel.sel.Token;


/**
 * The abstract syntax tree node for a numeric literal expression.  A numeric
 * expression is simply a numeric literal.  A simple example would be "5".
 */
public class NumericLiteral implements Expression
  {
    private double literalValue; 
    

    /**
     * Construct a numeric literal expression with a numeric literal token.
     */
    public NumericLiteral(Token literalToken)
      {
        assert literalToken.getSymbol() == Symbol.numericLiteral;
        literalValue = Double.parseDouble(literalToken.getText());
      }
    

    @Override
    public double interpret(Context context)
      {
        // note that context is ignored for a numeric literal
        return literalValue;
      }
  }
