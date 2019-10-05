package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;

import java.util.List;
import java.io.IOException;


/**
 * The abstract syntax tree node for a write statement.
 */
public class WriteStmt extends OutputStmt
  {
    public WriteStmt(List<Expression> expressions)
      {
        super(expressions);
      }


    @Override
    public void checkConstraints()
      {
        super.checkConstraints();
        assert getExpressions().size() > 0 : "A \"write\" statement must have an expression.";
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
        super.emit();
      }
  }
