package edu.citadel.cprl;


import edu.citadel.compiler.AbstractToken;
import edu.citadel.compiler.Position;


/**
 * Instantiates the generic class AbstractToken for CPRL symbols.
 */
public class Token extends AbstractToken<Symbol>
  {
    /**
     * Constructs a new token with the given symbol, position, and text.
     */
    public Token(Symbol symbol, Position position, String text)
      {
        super(symbol, position, text);
      }
  }
