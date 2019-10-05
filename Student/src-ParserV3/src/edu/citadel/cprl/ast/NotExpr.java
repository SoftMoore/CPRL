package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Symbol;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;

import java.io.IOException;


/**
 * The abstract syntax tree node for a not expression.  A not expression is a unary
 * expression of the form "not expr".  A simple example would be "not isEmtpy()".
 */
public class NotExpr extends UnaryExpr
  {
    public NotExpr(Token operator, Expression operand)
      {
        super(operator, operand);
        assert operator.getSymbol() == Symbol.notRW :
            "NotExpr: operator is not the reserved word \"not\".";
      }


    @Override
    public void checkConstraints()
      {
// ...
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
// ...
      }
  }
