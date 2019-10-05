package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Position;

import java.io.IOException;


/**
 * The abstract syntax tree node for a return statement.
 */
public class ReturnStmt extends Statement
  {
    private Expression     returnExpr;    // may be null
    private SubprogramDecl subprogramDecl;

    // position of the return token (needed for error reporting)
    private Position returnPosition;


    /**
     * Construct a return statement with a reference to the enclosing subprogram
     * and the expression for the value being returned, which may be null.
     */
    public ReturnStmt(SubprogramDecl subprogramDecl, Expression returnExpr, Position returnPosition)
      {
// ...
      }


    @Override
    public void checkConstraints()
      {
        assert subprogramDecl != null : "Return statement must be nested within a subprogram.";

// ...
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
// ...
      }
  }
