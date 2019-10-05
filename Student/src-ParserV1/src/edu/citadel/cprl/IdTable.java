package edu.citadel.cprl;


import edu.citadel.compiler.ParserException;
import edu.citadel.compiler.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


/**
 * The types for identifiers stored in the identifier table.
 */
enum IdType
  {
    constantId, variableId, arrayTypeId, procedureId, functionId;
  }


/**
 * A simplified version of an identifier table (also known as a symbol table)
 * used to hold attributes of identifiers in the programming language CPRL.
 */
public final class IdTable
  {
    // NOTE: IdTable is implemented as a stack of maps, where each map associates
    // the identifier string with its IdType.  The stack is implemented using an
    // array list.  When a when a new scope is opened, a new map is pushed onto the
    // stack.  Searching for a declaration involves searching at the current level
    // (top map in the stack) and then at enclosing scopes (maps under the top).

    private static final int INITIAL_SCOPE_LEVELS = 2;
    private static final int INITIAL_MAP_SIZE     = 50;

    private ArrayList<Map<String, IdType>> table;
    private int currentLevel;


    /**
     * Construct an empty identifier table with scope level initialized to 0.
     */
    public IdTable()
      {
        table = new ArrayList<Map<String, IdType>>(INITIAL_SCOPE_LEVELS);
        currentLevel = 0;
        table.add(currentLevel, new HashMap<>(INITIAL_MAP_SIZE));
      }


    /**
     * Opens a new scope for identifiers.
     */
    public void openScope()
      {
        ++currentLevel;
        table.add(currentLevel, new HashMap<String, IdType>(INITIAL_MAP_SIZE));
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
     * Add a token and its type at the current scope level.
     *
     * @throws ParserException if the identifier token is already defined in the current scope.
     */
    public void add(Token idToken, IdType idType) throws ParserException
      {
        // assumes that idToken is actually an identifier token
        assert idToken.getSymbol() == Symbol.identifier :
            "IdTable.add(): The symbol for idToken is not an identifier.";

        Map<String, IdType> idMap = table.get(currentLevel);
        IdType oldDecl = idMap.put(idToken.getText(), idType);

        // check that the identifier has not been defined previously
        if (oldDecl != null)
          {
            String errorMsg = "Identifier \"" + idToken.getText()
                            + "\" is already defined in the current scope.";
            throw new ParserException(idToken.getPosition(), errorMsg);
          }
      }


    /**
     * Returns the id type associated with the identifier.  Returns null
     * if the identifier is not found.  Searches enclosing scopes if necessary.
     */
    public IdType get(Token idToken)
      {
        assert idToken.getSymbol() == Symbol.identifier :
            "IdTable.get(): The symbol for idToken is not an identifier.";

        IdType idType = null;
        int level = currentLevel;

        while (level >= 0 && idType == null)
          {
            Map<String, IdType> idMap = table.get(level);
            idType = idMap.get(idToken.getText());
            --level;
          }

        return idType;
      }
  }
