package edu.citadel.sel.ast;


import edu.citadel.sel.Symbol;
import edu.citadel.sel.Token;


/**
 * The abstract syntax tree node for a negation expression.  A negation
 * expression is a unary expression where the operator is "-".
 * A simple example would be "-x".
 */
public class NegationExpr implements Expression
  {
    private Expression operand;


    /**
     * Construct a negation expression with a negation operator token and an operand.
     */
    public NegationExpr(Token operator, Expression operand)
      {
        assert operator.getSymbol() == Symbol.minus;
        this.operand  = operand;
      }


    @Override
    public double interpret(Context context)
      {
        return -operand.interpret(context);
      }
  }
