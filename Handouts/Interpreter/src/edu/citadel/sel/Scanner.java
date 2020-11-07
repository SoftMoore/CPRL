package edu.citadel.sel;


import edu.citadel.compiler.Position;
import edu.citadel.compiler.Source;
import java.io.IOException;


/**
 * A scanner for SEL with two-token lookahead. 
 */
public class Scanner
  {
    private Source   source;
    private Symbol   symbol;         // current symbol
    private Position position;       // position of current symbol
    private String   text;           // text of current token (for identifiers and literals)
    private Symbol   peekSymbol;     // second symbol
    private Position peekPosition;   // position of second symbol
    private String   peekText;       // text of second token (for identifiers and literals)
    private Boolean  isPeekValid;    // true if peek fields are valid

    private StringBuilder scanBuffer;


    /**
     * Initialize scanner with its associated source and advance
     * to the first token.
     */
    public Scanner(Source source) throws IOException, InterpreterException
      {
        this.source  = source;
        scanBuffer   = new StringBuilder(100);
        isPeekValid  = false;
        advance();           // advance to the first token
      }


    /**
     * Returns a copy of the current token in the source file.
     */
    public Token getToken()
      {
        return new Token(symbol, position, text);
      }
    
    
    /**
     * Returns a reference to the current symbol in the source.
     */
    public Symbol getSymbol()
      {
        return symbol;
      }


    /**
     * Returns a reference to the position of the current symbol in the source file.
     */
    public Position getPosition()
      {
        return position;
      }


    /**
     * Returns a copy of the current token in the source file.
     */
    public Token getPeekToken() throws IOException, InterpreterException
      {
        advancePeek();
        return new Token(peekSymbol, peekPosition, peekText);
      }


    /**
     * @return the peekSymbol
     */
    public Symbol getPeekSymbol() throws IOException, InterpreterException
      {
        advancePeek();
        return peekSymbol;
      }


    /**
     * @return the peekPosition
     */
    public Position getPeekPosition() throws IOException, InterpreterException
      {
        advancePeek();
        return peekPosition;
      }


    private void advancePeek() throws IOException, InterpreterException
      {
        if (!isPeekValid)
          {
            // save current values
            Symbol savedSymbol   = symbol;
            Position savedPosition = position;
            String savedText     = text;

            advance();

            // set values for peek properties
            peekSymbol = symbol;
            peekPosition = position;
            peekText = text;

            // restore saved current values
            symbol = savedSymbol;
            position = savedPosition;
            text = savedText;

            isPeekValid = true;
          }
      }


    /**
     * Advance to the next symbol in the expression string.
     */
    public void advance() throws IOException, InterpreterException
      {
        if (isPeekValid)
          {
            // peekToken was previously used as the lookahead
            // token and is now valid as the current token
            symbol      = peekSymbol;
            position    = peekPosition;
            text        = peekText;  
            isPeekValid = false;
          }
        else
          {
            skipWhiteSpace();
            
            // currently at starting character of the next token
            position = source.getCharPosition();
            text = "";

            if (source.getChar() == Source.EOF)
              {
                // set symbol but don't advance
                symbol = Symbol.EOF;
              }
            else if (Character.isLetter((char) source.getChar()))
              {
                text = scanIdentifier();
                symbol = Symbol.identifier;
              }
            else if (Character.isDigit((char) source.getChar()))
              {
                text = scanNumericLiteral();
                symbol = Symbol.numericLiteral;
              }
            else
              {
                switch((char) source.getChar())
                  {
                    case '\r':          // assume Windows style for end-of-line
                        source.advance();
                        if ((char) source.getChar() == '\n')
                          {
                            symbol = Symbol.EOL;
                            source.advance();
                          }
                        else
                          {
                            error("Invalid end-of-line character.");
                          }
                        break;
                    case '\n':
                        symbol = Symbol.EOL;
                        source.advance();
                        break;
                    case '+':
                        symbol = Symbol.plus;
                        source.advance();
                        break;
                    case '-':
                        symbol = Symbol.minus;
                        source.advance();
                        break;
                    case '*':
                        symbol = Symbol.times;
                        source.advance();
                        break;
                    case '/':
                        symbol = Symbol.divide;
                        source.advance();
                        break;
                    case '(':
                        symbol = Symbol.leftParen;
                        source.advance();
                        break;
                    case ')':
                        symbol = Symbol.rightParen;
                        source.advance();
                        break;
                    case '.':
                        symbol = Symbol.dot;
                        source.advance();
                        break;
                    case '=':
                        symbol = Symbol.assign;
                        source.advance();
                        break;
                    default:           // error:  invalid character
                      {
                        String errorMsg = "Invalid character \'" + (char) source.getChar() + "\'";
                        error(errorMsg);
                      }
                  }
              }
          }
      }


    /**
     * Fast skip over tabs and spaces.
     */
    private void skipWhiteSpace() throws IOException
      {
        while (Character.isWhitespace((char) source.getChar()))
          {
            source.advance();
          }
      }


    /**
     * Clear the scan buffer (makes it empty).
     */
    private void clearScanBuffer()
      {
        scanBuffer.delete(0, scanBuffer.length());
      }


    /**
     * Scans characters in the source file for a valid identifier using the
     * lexical rule: identifier = letter ( letter | digit)* .
     *
     * @return the string of letters and digits for the identifier.
     */
    private String scanIdentifier() throws IOException
      {
        assert Character.isLetter((char) source.getChar());
        clearScanBuffer();

        do
          {
            scanBuffer.append((char) source.getChar());
            source.advance();
          }
        while (Character.isLetterOrDigit((char) source.getChar()));

        return scanBuffer.toString();
      }


    /**
     * Scans characters in the expression string for a valid numeric literal.
     * Assumes that getChar() is the first character of the numeric literal.
     *
     * @return the string of digits for the numeric literal.
     */
    private String scanNumericLiteral() throws IOException, InterpreterException
      {
        assert Character.isDigit((char) source.getChar());
        clearScanBuffer();

        do
          {
            scanBuffer.append((char) source.getChar());
            source.advance();
          }
        while (Character.isDigit((char) source.getChar()));
        
        if ((char) source.getChar() == '.')
          {
            scanBuffer.append((char) source.getChar());
            source.advance();

            if (!Character.isDigit((char) source.getChar()))
                error("Invalid numeric literal");
            
            do
              {
                scanBuffer.append((char) source.getChar());
                source.advance();
              }
            while (Character.isDigit((char) source.getChar()));
          }

        return scanBuffer.toString();
      }


    /**
     * Throws an exception with an appropriate error message.
     */
    private void error(String message) throws InterpreterException
      {
        Position position = source.getCharPosition();
        String errorMsg = "*** Lexical error detected near position "
                         + position + ":\n    " + message;
        throw new InterpreterException(errorMsg);
      }
  }

