package edu.citadel.cprl.ast;


import java.util.List;


/**
 * The abstract syntax tree node for a write statement.
 */
public class WriteStmt extends OutputStmt
  {
    public WriteStmt(List<Expression> expressions)
      {
        super(expressions);
        assert expressions.size() > 0 : "A \"write\" statement must have an expression.";
      }


    // inherited checkConstraints() method is sufficient

    // inherited emit() method is sufficient
  }
