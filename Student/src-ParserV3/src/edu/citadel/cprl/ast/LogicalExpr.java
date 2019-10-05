package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Symbol;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;

import java.io.IOException;


/**
 * The abstract syntax tree node for a logical expression.  A logical expression
 * is a binary expression where the operator is either "and" or "or".  A simple
 * example would be "(x &gt; 5) and (y &lt; 0)".
 */
public class LogicalExpr extends BinaryExpr
  {
    // labels used during code generation for short-circuit version
    private String L1;   // label at start of right operand
    private String L2;   // label at end of logical expression


    /**
     * Construct a logical expression with the operator ("and" or "or")
     * and the two operands.
     */
    public LogicalExpr(Expression leftOperand, Token operator, Expression rightOperand)
      {
        super(leftOperand, operator, rightOperand);
        assert operator.getSymbol().isLogicalOperator() :
            "LogicalExpression: operator is not a logical operator.";

        L1 = getNewLabel();
        L2 = getNewLabel();
      }


    @Override
    public void checkConstraints()
      {
        try
          {
            Expression leftOperand  = getLeftOperand();
            Expression rightOperand = getRightOperand();

            leftOperand.checkConstraints();
            rightOperand.checkConstraints();

            if (leftOperand.getType() != Type.Boolean)
              {
                String errorMsg = "Left operand for a logical expression "
                                + "should have type Boolean.";
                throw error(leftOperand.getPosition(), errorMsg);
              }

            if (rightOperand.getType() != Type.Boolean)
              {
                String errorMsg = "Right operand for a logical expression "
                                + "should have type Boolean.";
                throw error(rightOperand.getPosition(), errorMsg);
              }
          }
        catch (ConstraintException e)
          {
            ErrorHandler.getInstance().reportError(e);
          }

        setType(Type.Boolean);
      }


    /**
     * Uses short-circuit evaluation for logical expressions.
     */
    @Override
    public void emit() throws CodeGenException, IOException
      {
        // Note: Unlike the various emitBranch methods, this method will leave the
        // value (true or false) of the logical expression on the top of the stack.

        Expression leftOperand  = getLeftOperand();
        Expression rightOperand = getRightOperand();
        Token      operator     = getOperator();

        Symbol operatorSym = operator.getSymbol();

        // emit code to evaluate the left operand
        leftOperand.emit();

        if (operatorSym == Symbol.andRW)
          {
            // if true, branch to code that will evaluate right operand
            emit("BNZ " + L1);

            // otherwise, place "false" back on top of stack as value
            // for the compound "and" expression
            emit("LDCB " + FALSE);
          }
        else if (operatorSym == Symbol.orRW)
          {
            // if false, branch to code that will evaluate right operand
            emit("BZ " + L1);

            // otherwise, place "true" back on top of stack as value
            // for the compound "or" expression
            emit("LDCB " + TRUE);
          }
        else
          {
            throw new CodeGenException(operator.getPosition(), "Invalid logical operator.");
          }

        // branch to code following the expression
        emit("BR " + L2);

        // L1:
        emitLabel(L1);

        // evaluate the right operand and leave result on
        // top of stack as value for compound expression
        rightOperand.emit();

        // L2:
        emitLabel(L2);
      }
  }
