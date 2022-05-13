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
        // interpret all but the last expression
        for (int i = 0; i < expressions.size() - 1; ++i)
            expressions.get(i).interpret(context);

        // now interpret the last one
        Expression lastExpr = expressions.get(expressions.size() - 1);
        return lastExpr.interpret(context);
      }
  }
