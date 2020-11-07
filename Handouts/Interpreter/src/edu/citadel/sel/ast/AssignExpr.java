package edu.citadel.sel.ast;


import edu.citadel.sel.Token;
import edu.citadel.sel.Symbol;


/**
 * The abstract syntax tree node for an assignment expression.  An assignment
 * expression has the form "identifier <- expression".  A simple example would
 * be "x <- 5".
 */
public class AssignExpr implements Expression
  {
    private Token idToken;
    private Expression expression;


    /*
     * Construct an assignment expression with an identifier (the left side
     * of the expression) and an expression whose value is being assigned
     * to the identifier.
     */
    public AssignExpr(Token idToken, Expression expression)
      {
        assert idToken.getSymbol() == Symbol.identifier;
        this.idToken = idToken;
        this.expression = expression;
      }


    @Override
    public double interpret(Context context)
      {
        double value = expression.interpret(context);
        String identifier = idToken.getText();
        context.put(identifier, value);
        return value;
      }
  }
