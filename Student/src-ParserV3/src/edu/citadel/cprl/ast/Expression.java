package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.Position;
import edu.citadel.cprl.Type;

import java.io.IOException;


/**
 * Base class for all CPRL expressions.
 */
public abstract class Expression extends AST
  {
    /** constant for false */
    public static final String FALSE = "0";

    /** constant for true */
    public static final String TRUE = "1";

    private Type     exprType;
    private Position exprPosition;   // position of the start of the expression


    /**
     * Construct an expression with the specified type and position.
     */
    public Expression(Type exprType, Position exprPosition)
      {
        this.exprType     = exprType;
        this.exprPosition = exprPosition;
      }


    /**
     * Construct an expression with the specified position.  Initializes
     * the type of the expression to UNKNOWN.
     */
    public Expression(Position exprPosition)
      {
        this(Type.UNKNOWN, exprPosition);
      }


    /**
     * Returns the type of this expression.
     */
    public Type getType()
      {
        return exprType;
      }


    /**
     * Sets the type of this expression.
     */
    public void setType(Type exprType)
      {
        this.exprType = exprType;
      }


    /**
     * Returns the position of this expression.
     */
    public Position getPosition()
      {
        return exprPosition;
      }


    /**
     * Sets the position of this expression.
     */
    public void setPosition(Position exprPosition)
      {
        this.exprPosition = exprPosition;
      }


    /**
     * For Boolean expressions, the method emits the appropriate branch opcode
     * based on the condition.  For example, if the expression is a "&lt;"
     * relational expression and the condition is false, then the opcode "BGE"
     * is emitted.  The method defined in this class works correctly for Boolean
     * constants, Boolean named values, and "not" expressions.  It should be
     * overridden for other Boolean expression ASTs (e.g., RelationalExpr).
     *
     * @param condition  the condition that determines the branch to be emitted.
     * @param label      the label for the branch destination.
     *
     * @throws IOException                if there is a problem writing to the target file.
     * @throws CodeGenException           if the method is unable to generate appropriate
     *                                    target code.
     */
    public void emitBranch(boolean condition, String label) throws CodeGenException, IOException
      {
        // default behavior unless overridden; correct for constants and named values
        assert exprType == Type.Boolean : "Expression type is not Boolean";

        emit();  // leaves boolean value on top of stack
        emit(condition ? "BNZ " + label : "BZ " + label);
      }
  }
