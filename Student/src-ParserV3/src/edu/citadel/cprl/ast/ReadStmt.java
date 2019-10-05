package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.Type;

import java.io.IOException;


/**
 * The abstract syntax tree node for a read statement.
 */
public class ReadStmt extends Statement
  {
    private Variable variable;


    /**
     * Construct an input statement with the specified
     * variable for storing the input.
     */
    public ReadStmt(Variable variable)
      {
// ...
      }


    @Override
    public void checkConstraints()
      {
        // input is limited to integers and characters
// ...
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
// ...
      }
  }
