package edu.citadel.cprl.ast;


import edu.citadel.cprl.Token;
import edu.citadel.cvm.Constants;

import java.util.*;


/**
 * Base class for CPRL procedures and functions.
 */
public abstract class SubprogramDecl extends Declaration
  {
    private List<ParameterDecl> formalParams;
    private List<InitialDecl> initialDecls;
    private StatementPart stmtPart;

    private int varLength;   // # bytes of all declared variables

    private String L1;       // label of address of first statement
                             // (used during code generation)


    /**
     * Construct a subprogram declaration with the specified subprogram identifier.
     */
    public SubprogramDecl(Token subprogId)
      {
        super(subprogId);

        this.formalParams = new ArrayList<>();
        this.initialDecls = null;
        this.stmtPart     = null;
        this.varLength    = 0;

        L1 = getNewLabel();
      }


    /**
     * Set the list of initial declarations for this subprogram. 
     */
    public void setInitialDecls(List<InitialDecl> initialDecls)
      {
        this.initialDecls = initialDecls;
      }


    /**
     * Returns the list of formal parameter declarations for this subprogram. 
     */
    public List<ParameterDecl> getFormalParams()
      {
        return formalParams;
      }


    /**
     * Set the list of formal parameter declarations for this subprogram. 
     */
    public void setFormalParams(List<ParameterDecl> formalParams)
      {
        this.formalParams = formalParams;
      }


    /**
     * Returns the statement part for this subprogram. 
     */
    public StatementPart getStatementPart()
      {
        return stmtPart;
      }


    /**
     * Set the statement part for this subprogram. 
     */
    public void setStatementPart(StatementPart stmtPart)
      {
        this.stmtPart = stmtPart;
      }


    /**
     * Returns the number of bytes required for all variables in the initial declarations.
     */
    protected int getVarLength()
      {
        return varLength;
      }


    /**
     * Returns the label associated with the first statement of the subprogram.
     */
    protected String getSubprogramLabel()
      {
        return L1;
      }


    /**
     * Returns the number of bytes for all parameters
     */
    protected int getParamLength()
      {
        int paramLength = 0;

        for (ParameterDecl decl : formalParams)
            paramLength += decl.getSize();

        return paramLength;
      }


    @Override
    public void checkConstraints()
      {
        for (InitialDecl decl : initialDecls)
            decl.checkConstraints();

        for (ParameterDecl paramDecl : formalParams)
            paramDecl.checkConstraints();
        
        stmtPart.checkConstraints();
      }
    
    
    /**
    * Set the relative address (offset) for each variable and
    * parameter, and compute the length of all variables.
    */
    protected void setRelativeAddresses()
      {
        // initial relative address for a subprogram
        int currentAddr = Constants.BYTES_PER_FRAME;

        for (InitialDecl decl : initialDecls)
          {
            // set relative address for single variable declarations
            if (decl instanceof SingleVarDecl)
              {
                SingleVarDecl singleVarDecl = (SingleVarDecl) decl;
                singleVarDecl.setRelAddr(currentAddr);
                currentAddr = currentAddr + singleVarDecl.getSize();
              }
          }

        // compute length of all variables by subtracting initial relative address
        varLength = currentAddr - Constants.BYTES_PER_FRAME;

        // set relative address for parameters
        if (formalParams.size() > 0)
          {
            // initial relative address for a subprogram parameter
            currentAddr = 0;

            // we need to process the parameter declarations in reverse order
            ListIterator<ParameterDecl> iter = formalParams.listIterator(formalParams.size());
            while (iter.hasPrevious())
              {
                ParameterDecl decl = iter.previous();
                currentAddr = currentAddr - decl.getSize();
                decl.setRelAddr(currentAddr);
              }
          }
      }
  }
