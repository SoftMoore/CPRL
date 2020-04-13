package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Type;

import java.io.IOException;
import java.util.List;


/**
 * The AST for an elsif part of an if statement.
 */
public class ElsifPart extends AST
  {
    private Expression booleanExpr;
    private List<Statement> thenStmts;   // List of Statement objects following "then"
    private String endIfLabel;           // the label at the end of the if statement

    private String L1;   // label at end of statements


    /**
     * Construct an elsif part with the specified boolean expression
     * and list of "then" statements.
     *
     * @param booleanExpr the boolean expression that, if true, will result
     *                    in the execution of the list of "then" statements.
     * @param thenStmts   the statements to be executed when the boolean
     *                    expression evaluates to true.
     */
    public ElsifPart(Expression booleanExpr, List<Statement> thenStmts)
      {
        this.booleanExpr = booleanExpr;
        this.thenStmts   = thenStmts;

        L1 = getNewLabel();
      }


    /**
     * Returns the list of "then" statements for this elsif part.
     */
    public List<Statement> getThenStmts()
      {
        return thenStmts;
      }


    /**
     * Set the label associated with the end of the if statement
     * (used for code generation.)
     */
    public void setEndIfLabel(String endIfLabel)
      {
        this.endIfLabel = endIfLabel;
      }


    @Override
    public void checkConstraints()
      {
        try
          {
            booleanExpr.checkConstraints();

            for (Statement stmt : thenStmts)
                stmt.checkConstraints();

            if (booleanExpr.getType() != Type.Boolean)
              {
                String errorMsg = "An \"elsif\" condition should have type Boolean";
                throw error(booleanExpr.getPosition(), errorMsg);
              }
          }
        catch (ConstraintException e)
          {
            ErrorHandler.getInstance().reportError(e);
          }
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
        booleanExpr.emitBranch(false, L1);

        // emit code for statements
        for (Statement stmt : thenStmts)
            stmt.emit();

        // branch to end of if statement
        emit("BR " + endIfLabel);

        // L1:
        emitLabel(L1);
      }
  }
