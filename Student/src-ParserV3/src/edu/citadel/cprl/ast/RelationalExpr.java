package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Symbol;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;

import java.io.IOException;


/**
 * The abstract syntax tree node for a relational expression.  A relational
 * expression is a binary expression where the operator is a relational
 * operator such as "&lt;=" or "&gt;".  A simple example would be "x &lt; 5".
 */
public class RelationalExpr extends BinaryExpr
  {
    // labels used during code generation
    private String L1;   // label at start of right operand
    private String L2;   // label at end of the relational expression


    /**
     * Construct a relational expression with the operator ("=", "&lt;=", etc.)
     * and the two operands.
     */
    public RelationalExpr(Expression leftOperand, Token operator, Expression rightOperand)
      {
        super(leftOperand, operator, rightOperand);

        assert operator.getSymbol().isRelationalOperator() :
            "RelationalExpr: operator is not a relational operator";

        L1 = getNewLabel();
        L2 = getNewLabel();
      }


    @Override
    public void checkConstraints()
      {
// ...
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
        emitBranch(false, L1);

        // emit true
        emit("LDCB " + TRUE);

        // jump over code to emit false
        emit("BR " + L2);

        // L1:
        emitLabel(L1);

        // emit false
        emit("LDCB " + FALSE);

        // L2:
        emitLabel(L2);
      }


    @Override
    public void emitBranch(boolean condition, String label) throws CodeGenException, IOException
      {
        Token operator = getOperator();

        emitOperands();
        emit("CMP");

        Symbol operatorSym = operator.getSymbol();

        if (operatorSym == Symbol.equals)
            emit(condition ? "BZ "  + label : "BNZ " + label);
        else if (operatorSym == Symbol.notEqual)
            emit(condition ? "BNZ " + label : "BZ "  +  label);
        else if (operatorSym == Symbol.lessThan)
            emit(condition ? "BL "  + label : "BGE " + label);
        else if (operatorSym == Symbol.lessOrEqual)
            emit(condition ? "BLE " + label : "BG "  + label);
        else if (operatorSym == Symbol.greaterThan)
            emit(condition ? "BG "  + label : "BLE " + label);
        else if (operatorSym == Symbol.greaterOrEqual)
            emit(condition ? "BGE " + label : "BL "  + label);
        else
            throw new CodeGenException(operator.getPosition(), "Invalid relational operator.");
      }


    private void emitOperands() throws CodeGenException, IOException
      {
        Expression leftOperand  = getLeftOperand();
        Expression rightOperand = getRightOperand();

        // Relational operators compare integers only, so we need to make sure
        // that we have enough bytes on the stack.  Pad with zero bytes.
        int leftOperandSize = leftOperand.getType().getSize();
        for (int n = 1;  n <= (Type.Integer.getSize() - leftOperandSize);  ++n)
            emit("LDCB 0");

        leftOperand.emit();

        int rightOperandSize = rightOperand.getType().getSize();
        for (int n = 1;  n <= (Type.Integer.getSize() - rightOperandSize);  ++n)
            emit("LDCB 0");

        rightOperand.emit();
      }
  }
