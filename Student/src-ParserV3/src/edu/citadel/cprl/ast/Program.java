package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;

import java.io.IOException;
import java.util.List;


/**
 * The abstract syntax tree node for a CPRL program.
 */
public class Program extends AST
  {
    private DeclarativePart declPart;
    private StatementPart   stmtPart;

    private int varLength;      // # bytes of all declared variables


    // label for first program statement (used during code generation)
    private String L1;


    /**
     * Construct a program with the specified declarative and statement parts.
     */
    public Program(DeclarativePart declPart, StatementPart stmtPart)
      {
        this.declPart = declPart;
        this.stmtPart = stmtPart;
        this.varLength = 0;

        L1 = getNewLabel();
      }


    @Override
    public void checkConstraints()
      {
        // This method should never be called with null declarative and statement parts.
        assert declPart != null : "declPart should never be null";
        assert stmtPart != null : "stmtPart should never be null";

        declPart.checkConstraints();
        stmtPart.checkConstraints();
      }


    /**
     * Set the relative address (offset) for each variable
     * and compute the length of all variables.
     */
    private void setRelativeAddresses()
      {
        // initial relative address is 0 for a program
        int currentAddr = 0;

        for (InitialDecl decl : declPart.getInitialDecls())
          {
            // set relative address for single variable declarations
            if (decl instanceof SingleVarDecl)
              {
                SingleVarDecl singleVarDecl = (SingleVarDecl) decl;
                singleVarDecl.setRelAddr(currentAddr);
                currentAddr = currentAddr + singleVarDecl.getSize();
              }
          }

        // compute length of all variables
        varLength = currentAddr;
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
        setRelativeAddresses();

        // no need to emit PROGRAM instruction if varLength == 0
        if (varLength > 0)
            emit("PROGRAM " + varLength);

        // emit branch over subprograms only if necessary
        List<SubprogramDecl> subprogDecls = declPart.getSubprogramDecls();
        if (!subprogDecls.isEmpty())
          {
            // jump over code for subprograms
            emit("BR " + L1);
            declPart.emit();
            emitLabel(L1);
          }

        stmtPart.emit();
        emit("HALT");
      }
  }
