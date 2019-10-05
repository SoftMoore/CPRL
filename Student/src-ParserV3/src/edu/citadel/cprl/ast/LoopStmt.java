package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Type;

import java.util.Collections;
import java.util.List;
import java.io.IOException;


/**
 * The abstract syntax tree node for a loop statement.
 */
public class LoopStmt extends Statement
  {
    private Expression whileExpr;
    private List<Statement> statements;

    // labels used during code generation
    private String L1;    // label for start of loop
    private String L2;    // label for end of loop


    /**
     * Default constructor.  Construct a loop statement with a null "while"
     * expression and an empty list of statements for the loop body.
     */
    public LoopStmt()
      {
// ...
      }


    /**
     * Set the while expression for this loop statement.
     */
    public void setWhileExpr(Expression whileExpr)
      {
// ...
      }


    /**
     * Returns the list of statements for the body of this loop statement.
     */
    public List<Statement> getStatements()
      {
// ...
      }


    /**
     * Set the list of statements for the body of this loop statement.
     */
    public void setStatements(List<Statement> statements)
      {
// ...
      }


    /**
     * Returns the label for the end of the loop statement.
     */
    public String getExitLabel()
      {
// ...
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
