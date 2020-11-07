package edu.citadel.sel.ast;


import edu.citadel.sel.Symbol;
import edu.citadel.sel.Token;


/**
 * The abstract syntax tree node for a multiplying expression.  A multiplying
 * expression is a binary expression where the operator is a either "*" or "/".
 * A simple example would be "5*x".
 */
public class MultiplyingExpr implements Expression
  {
    private Expression leftOperand;
    private Token      operator;
    private Expression rightOperand;


    /**
     * Construct a multiplying expression with the operator ("*" or "/")
     * and the two operands.
     */
    public MultiplyingExpr(Expression leftOperand, Token operator, Expression rightOperand)
      {
        assert operator.getSymbol().isMultiplyingOperator();
        this.leftOperand  = leftOperand;
        this.operator     = operator;
        this.rightOperand = rightOperand;
      }


    @Override
    public double interpret(Context context)
      {
        if (operator.getSymbol() == Symbol.times)
            return leftOperand.interpret(context)*rightOperand.interpret(context);
        else
            return leftOperand.interpret(context)/rightOperand.interpret(context);
      }
  }
