package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;

import java.util.List;
import java.io.IOException;


/**
 * The abstract syntax tree node for a writeln statement.
 */
public class WritelnStmt extends OutputStmt
  {
    public WritelnStmt(List<Expression> expressions)
      {
        super(expressions);
      }


    // inherited checkConstraints() method is sufficient


    @Override
    public void emit() throws CodeGenException, IOException
      {
// ...
      }
  }
