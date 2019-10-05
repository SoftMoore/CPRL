package edu.citadel.cprl;


import edu.citadel.compiler.ParserException;
import edu.citadel.cprl.ast.Declaration;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


/**
 * The identifier table (also known as a symbol table) is used to
 * hold attributes of identifiers in the programming language CPRL.
 */
public final class IdTable
  {
    // NOTE: IdTable is implemented as a stack of maps, where each map associates
    // the identifier string with its declaration.  The stack is implemented using an
    // array list.  When a when a new scope is opened, a new map is pushed onto the
    // stack.  Searching for a declaration involves searching at the current level
    // (top map in the stack) and then at enclosing scopes (maps under the top)

    private static final int INITIAL_SCOPE_LEVELS = 2;
    private static final int INITIAL_MAP_SIZE     = 50;

    private ArrayList<Map<String, Declaration>> table;
    private int currentLevel;


    /**
     * Construct an empty identifier table with scope level initialized to 0.
     */
    public IdTable()
      {
        table = new ArrayList<Map<String, Declaration>>(INITIAL_SCOPE_LEVELS);
        currentLevel = 0;
        table.add(currentLevel, new HashMap<>(INITIAL_MAP_SIZE));
      }


    /**
     * Returns the current scope level.
     */
    public ScopeLevel getCurrentLevel()
      {
        return currentLevel == 0 ? ScopeLevel.PROGRAM : ScopeLevel.SUBPROGRAM;
      }


    /**
     * Opens a new scope for identifiers.
     */
    public void openScope()
      {
        ++currentLevel;
        table.add(currentLevel, new HashMap<String, Declaration>(INITIAL_MAP_SIZE));
      }


    /**
     * Closes the outermost scope.
     */
    public void closeScope()
      {
        table.remove(currentLevel);
        --currentLevel;
      }


    /**
     * Add a declaration at the current scope level.
     *
     * @throws ParserException if the identifier token associated with the
     *                         declaration is already defined in the current scope.
     */
    public void add(Declaration decl) throws ParserException
      {
        Token idToken = decl.getIdToken();

        // assumes that idToken is actually an identifier token
        assert idToken.getSymbol() == Symbol.identifier :
            "IdTable.add(): The symbol for idToken is not an identifier.";

        Map<String, Declaration> idMap = table.get(currentLevel);
        Declaration oldDecl = idMap.put(idToken.getText(), decl);

        // check that the identifier has not been defined previously
        if (oldDecl != null)
          {
            String errorMsg = "Identifier \"" + idToken.getText()
                            + "\" is already defined in the current scope.";
            throw new ParserException(idToken.getPosition(), errorMsg);
          }
      }


    /**
     * Returns the Declaration associated with the identifier token's text.
     * Searches enclosing scopes if necessary.
     */
    public Declaration get(Token idToken)
      {
        // assumes that idToken is an identifier
        assert idToken.getSymbol() == Symbol.identifier :
            "IdTable.get(): The symbol for idToken is not an identifier.";

        Declaration decl = null;
        int level = currentLevel;

        while (level >= 0 && decl == null)
          {
            Map<String, Declaration> idMap = table.get(level);
            decl = (Declaration) idMap.get(idToken.getText());
            --level;
          }

        return decl;
      }
  }
