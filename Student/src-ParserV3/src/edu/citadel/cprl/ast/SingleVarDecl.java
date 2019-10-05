package edu.citadel.cprl.ast;


import edu.citadel.cprl.ScopeLevel;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;


/**
 * The abstract syntax tree node for a single variable declaration.
 * A single variable declaration has the form
 * <code>
 *    var x : Integer;
 * </code>
 * Note: A variable declaration where more than one variable is declared
 * is simply a container for multiple single variable declarations.
 */
public class SingleVarDecl extends InitialDecl implements NamedDecl
  {
    private ScopeLevel scopeLevel;
    private int relAddr;     // relative address for this declaration


    /**
     * Construct a SingleVarDecl with its identifier, type, and scope level.
     */
    public SingleVarDecl(Token identifier, Type varType, ScopeLevel scopeLevel)
      {
        super(identifier, varType);
        this.scopeLevel = scopeLevel;
      }


    /**
     * Returns the size (number of bytes) associated with this single
     * variable declaration, which is simply the number of bytes associated
     * with its type.
     */
    public int getSize()
      {
        return getType().getSize();
      }


    @Override
    public ScopeLevel getScopeLevel()
      {
        return scopeLevel;
      }


    /**
     * Sets the relative address for this declaration. <br>
     * Note: This method should be called before calling method getRelAddr().
     */
    public void setRelAddr(int relAddr)
      {
        this.relAddr = relAddr;
      }


    /**
     * Returns the relative address (offset) associated with this single
     * var declaration.
     */
    public int getRelAddr()
      {
        return relAddr;
      }
  }
