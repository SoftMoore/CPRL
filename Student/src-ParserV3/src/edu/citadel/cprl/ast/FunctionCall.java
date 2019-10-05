package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Token;

import java.util.List;
import java.util.Iterator;
import java.io.IOException;


/**
 * The abstract syntax tree node for a function call expression.
 */
public class FunctionCall extends Expression
  {
    private Token funcId;
    private List<Expression> actualParams;
    private FunctionDecl funcDecl;


    /**
     * Construct a function call with the function identifier (name), the
     * list of actual parameters, and a reference to the function declaration.
     */
    public FunctionCall(Token funcId,
                        List<Expression> actualParams,
                        FunctionDecl funcDecl)
      {
        super(funcDecl.getType(), funcId.getPosition());

        this.funcId = funcId;
        this.actualParams = actualParams;
        this.funcDecl = funcDecl;
      }


    @Override
    public void checkConstraints()
      {
        try
          {
            List<ParameterDecl> formalParams = funcDecl.getFormalParams();

            // check that numbers of parameters match
            if (actualParams.size() != formalParams.size())
                throw error(funcId.getPosition(), "Incorrect number of actual parameters.");

            // call checkConstraints for each actual parameter
            for (Expression expr : actualParams)
                expr.checkConstraints();

            // check that parameter types match
            Iterator<Expression>    iterActual = actualParams.iterator();
            Iterator<ParameterDecl> iterFormal = formalParams.iterator();

            while (iterActual.hasNext())
              {
                Expression    expr  = iterActual.next();
                ParameterDecl param = iterFormal.next();

                if (!matchTypes(expr.getType(), param.getType()))
                  {
                    String errorMsg = "Parameter type mismatch.";
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
        // allocate space on the stack for the return value
        emit("ALLOC " + funcDecl.getType().getSize());

        // emit code for actual parameters
        for (Expression expr : actualParams)
            expr.emit();

        emit("CALL " + funcDecl.getSubprogramLabel());
      }
  }
