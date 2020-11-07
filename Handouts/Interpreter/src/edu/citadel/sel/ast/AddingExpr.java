package edu.citadel.sel.ast;


import edu.citadel.sel.Symbol;
import edu.citadel.sel.Token;


/**
 * The abstract syntax tree node for an adding expression.  An adding
 * expression is a binary expression where the operator is either "+" or "-".
 * A simple example would be "x + 5".
 */
public class AddingExpr implements Expression
  {
    private Expression leftOperand;
    private Token      operator;
    private Expression rightOperand;


    /**
     * Construct an adding expression with the operator ("+" or "-")
     * and the two operands.
     */
    public AddingExpr(Expression leftOperand, Token operator, Expression rightOperand)
      {
        assert operator.getSymbol().isAddingOperator();
        this.leftOperand  = leftOperand;
        this.operator     = operator;
        this.rightOperand = rightOperand;
      }
    

    @Override
    public double interpret(Context context)
      {
        if (operator.getSymbol() == Symbol.plus)
            return leftOperand.interpret(context) + rightOperand.interpret(context);
        else
            return leftOperand.interpret(context) - rightOperand.interpret(context);
      }
  }

