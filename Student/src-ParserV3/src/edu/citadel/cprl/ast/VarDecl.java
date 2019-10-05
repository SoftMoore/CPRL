package edu.citadel.cprl.ast;


import edu.citadel.cprl.ScopeLevel;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;

import java.util.List;
import java.util.ArrayList;


/**
 * The abstract syntax tree node for a variable declaration.  Note that a variable
 * declaration is simply a container for a list of single variable declarations
 * (SingleVarDecls).
 */
public class VarDecl extends InitialDecl
  {
    // the list of SingleVarDecls for the variable declaration
    private List<SingleVarDecl> singleVarDecls;


    /**
     * Construct a VarDecl with its list of identifier tokens, type, and scope level
     */
    public VarDecl(List<Token> identifiers, Type varType, ScopeLevel scopeLevel)
      {
        super(null, varType);
        
        singleVarDecls = new ArrayList<>(identifiers.size());
        for (Token id : identifiers)
            singleVarDecls.add(new SingleVarDecl(id, varType, scopeLevel));
      }


    /**
     * Returns the list of SingleVarDecls for this variable declaration.
     */
    public List<SingleVarDecl> getSingleVarDecls()
      {
        return singleVarDecls;
      }
  }
