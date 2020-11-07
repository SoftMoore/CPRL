package edu.citadel.sel.ast;


import edu.citadel.sel.Token;


/**
 * The abstract syntax tree node for an identifier expression.  An identifier
 * expression is simply an identifier.  A simple example would be "x".
 */
public class IdExpr implements Expression
  {
    private Token idToken;
    

    /**
     * Construct an identifier expression with the identifier token.
     */
    public IdExpr(Token idToken)
      {
        this.idToken = idToken;
      }


    @Override
    public double interpret(Context context)
      {
        String identifier = idToken.getText();
        return context.get(identifier);
      }
  }
