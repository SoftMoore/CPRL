package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Type;

import java.util.List;
import java.io.IOException;


/**
 * The abstract syntax tree node for an if statement.
 */
public class IfStmt extends Statement
  {
    private Expression booleanExpr;
    private List<Statement> thenStmts;      // List of Statement objects following "then"
    private List<ElsifPart> elsifParts;     // List of ElsifPart objects
    private List<Statement> elseStmts;      // List of Statement objects following "else"

    // labels used during code generation
    private String L1;   // label of address at end of then statements
    private String L2;   // label of address at end of if statement


    /**
     * Construct an if statement with the specified boolean expression
     * and list of "then" statements.
     *
     * @param booleanExpr the boolean expression that, if true, will result
     *                    in the execution of the list of "then" statements.
     * @param thenStmts   the statements to be executed when the boolean
     *                    expression evaluates to true.
     * @param elsifParts  the elsif clauses for the if statement.
     * @param elseStmts   the list of statements that are in the else clause.
     */
    public IfStmt(Expression booleanExpr,
                  List<Statement> thenStmts,
                  List<ElsifPart> elsifParts,
                  List<Statement> elseStmts)
      {
        this.booleanExpr = booleanExpr;
        this.thenStmts   = thenStmts;
        this.elsifParts  = elsifParts;
        this.elseStmts   = elseStmts;

        L1 = getNewLabel();
        L2 = getNewLabel();
      }


    /**
     * Returns the list of "then" statements for this if statement.
     */
    public List<Statement> getThenStmts()
      {
        return thenStmts;
      }


    /**
     * Returns the list of elsif parts for this if statement.
     */
    public List<ElsifPart> getElsifParts()
      {
        return elsifParts;
      }


    /**
     * Returns the list of "else" statements for this if statement.
     */
    public List<Statement> getElseStmts()
      {
        return elseStmts;
      }


    @Override
    public void checkConstraints()
      {
        try
          {
            booleanExpr.checkConstraints();

            for (Statement stmt : thenStmts)
                stmt.checkConstraints();

            for (ElsifPart part : elsifParts)
                part.checkConstraints();

            for (Statement stmt : elseStmts)
                stmt.checkConstraints();

            if (booleanExpr.getType() != Type.Boolean)
              {
                String errorMsg = "An \"if\" condition should have type Boolean.";
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
        // if expression evaluates to false, branch to L1
        booleanExpr.emitBranch(false, L1);

        // emit code for then statements
        for (Statement stmt : thenStmts)
            stmt.emit();

        // if there are elsif parts or an else part, branch to end of if statement
        if (elsifParts.size() > 0 || elseStmts.size() > 0)
            emit("BR " + L2);

        // L1:
        emitLabel(L1);

        // emit code for elsif statements
        for (ElsifPart part : elsifParts)
          {
            part.setEndIfLabel(L2);
            part.emit();
          }

        // emit code for else statements
        for (Statement stmt : elseStmts)
            stmt.emit();

        // L2:
        emitLabel(L2);
      }
  }
