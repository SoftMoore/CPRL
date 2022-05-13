package edu.citadel.sel;


import edu.citadel.compiler.AbstractToken;
import edu.citadel.compiler.Position;


/**
 * This class encapsulates the properties of a language token.  A token
 * consists of a symbol (a.k.a., the token type), a position, and a string
 * that contains the text of the token.
 */
public class Token extends AbstractToken<Symbol>
  {
    /**
     * Construct a new token with the given symbol, position, and text.
     */
    public Token(Symbol symbol, Position position, String text)
      {
        super(symbol, position, text);
      }


    /**
     * Construct a new Token with symbol = Symbol.unknown.
     * Position and text are initialized to null.
     */
    public Token()
      {
        this(Symbol.unknown, null, null);
      }
  }
