package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.cprl.Type;

import java.util.List;
import java.io.IOException;


/**
 * Base class with common code for write and writeln statements.
 */
public abstract class OutputStmt extends Statement
  {
    private List<Expression> expressions;


    public OutputStmt(List<Expression> expressions)
      {
        this.expressions = expressions;
      }


    /**
     * Returns the list of expressions for this output statement.
     */
    public List<Expression> getExpressions()
      {
        return expressions;
      }


    /**
     * Calls method checkConstraints() for each expression.
     */
    @Override
    public void checkConstraints()
      {
        for (Expression expr : expressions)
            expr.checkConstraints();
      }


    /**
     * Emits code to write the value of each expression to standard output.
     */
    @Override
    public void emit() throws CodeGenException, IOException
      {
        for (Expression expr : expressions)
          {
            if (expr != null)
              {
                expr.emit();

                Type exprType = expr.getType();

                if (exprType == Type.Integer)
                    emit("PUTINT");
                else if (exprType == Type.Boolean)
                    emit("PUTBYTE");
                else if (exprType == Type.Char)
                    emit("PUTCH");
                else if (exprType == Type.String)
                    emit("PUTSTR");
                else
                    throw new CodeGenException(expr.getPosition(), "Invalid type.");
              }
          }
      }
  }
