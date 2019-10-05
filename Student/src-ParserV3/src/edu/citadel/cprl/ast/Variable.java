package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Position;
import edu.citadel.cprl.ArrayType;
import edu.citadel.cprl.ScopeLevel;
import edu.citadel.cprl.Type;

import java.util.List;
import java.util.Collections;
import java.io.IOException;


/**
 * The abstract syntax tree node for a variable, which is any named variable
 * that can appear on the left hand side of an assignment statement.
 */
public class Variable extends Expression
  {
    private NamedDecl decl;
    private List<Expression> indexExprs;   // index expressions for an array;
                                           // empty if the variable is not an array

    /**
     * Construct a variable with a reference to its declaration,
     * its position, and a list of index expressions.
     */
    public Variable(NamedDecl decl, Position position, List<Expression> indexExprs)
      {
        super(decl.getType(), position);

        this.decl       = decl;
        this.indexExprs = indexExprs;
      }

    /**
     * Construct a variable with a reference to its declaration,
     * its position, and an empty list of index expressions.
     */
    public Variable(NamedDecl decl, Position position)
      {
        this(decl, position, Collections.emptyList());
      }


    /**
     * Construct a variable that corresponds to a named value.
     */
    public Variable(NamedValue nv)
      {
        this(nv.getDecl(), nv.getPosition(), nv.getIndexExprs());
      }


    /**
     * Returns the declaration for this variable.
     */
    public NamedDecl getDecl()
      {
        return decl;
      }


    /**
     * Returns the list of index expressions for an array.  Returns
     * an empty list if the variable is not an array variable.
     */
    public List<Expression> getIndexExprs()
      {
        return indexExprs;
      }


    /**
     * Sets the list of index expressions for an array.
     */
    public void setIndexExprs(List<Expression> indexExprs)
      {
        this.indexExprs = indexExprs;
      }


    @Override
    public void checkConstraints()
      {
        try
          {
            assert decl instanceof SingleVarDecl || decl instanceof ParameterDecl :
                "Declaration is not a variable.";

            for (Expression expr : indexExprs)
               {
                 expr.checkConstraints();

                // check that the variable's type is an array type
                if (getType() instanceof ArrayType)
                  {
                    // Applying the index effectively changes the
                    // variable's type to the base type of the array
                    ArrayType type = (ArrayType) getType();
                    setType(type.getElementType());
                  }
                else
                  {
                    String errorMsg = "Index expression not allowed; not an array";
                    throw error(expr.getPosition(), errorMsg);
                  }

                // check that the type of the index expression is Integer
                if (expr.getType() != Type.Integer)
                  {
                    String errorMsg = "Index expression must have type Integer.";
                    throw error(expr.getPosition(), errorMsg);
                  }
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
        if (decl instanceof ParameterDecl && ((ParameterDecl)decl).isVarParam())
          {
            // address of actual parameter is value of var parameter
            emit("LDLADDR " + decl.getRelAddr());
            emit("LOADW");
          }
        else if (decl.getScopeLevel() == ScopeLevel.PROGRAM)
            emit("LDGADDR " + decl.getRelAddr());
        else
            emit("LDLADDR " + decl.getRelAddr());

        // For an array, at this point the base address of the array
        // is on the top of the stack.  We need to replace it by the
        // sum: base address + offset
// ...
      }
  }
