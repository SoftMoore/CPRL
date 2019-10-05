package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;

import java.util.List;
import java.io.IOException;


/**
 * The abstract syntax tree node for the declarative part of a program.
 */
public class DeclarativePart extends AST
  {
    private List<InitialDecl>    initialDecls;
    private List<SubprogramDecl> subprogDecls;


    /**
     * Construct a DeclarativePart with the lists of initial and subprogram declarations.
     */
    public DeclarativePart(List<InitialDecl> initialDecls, List<SubprogramDecl> subprogramDecls)
      {
// ...
      }


    /**
     * Returns the list of initial declarations.
     */
    public List<InitialDecl> getInitialDecls()
      {
// ...
      }


    /**
     * Returns the list of subprogram declarations.
     */
    public List<SubprogramDecl> getSubprogramDecls()
      {
// ...
      }


    @Override
    public void checkConstraints()
      {
// ...
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
// ...
      }
  }
