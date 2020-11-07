package edu.citadel.sel.ast;


import java.util.List;


/**
 * The abstract syntax tree node for a program, which is simply a
 * list of expressions.
 */
public class Program implements Expression
  {
    private List<Expression> expressions;
    
    
    /**
     * Construct a program with a list of expressions. 
     */
    public Program(List<Expression> expressions)
      {
        this.expressions = expressions;
      }


    @Override
    public double interpret(Context context)
      {
        for (Expression expr : expressions)
            expr.interpret(context);

        Expression lastExpr = expressions.get(expressions.size() - 1);
        return lastExpr.interpret(context);
      }
  }
