package edu.citadel.cvm.assembler;


import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.Position;
import edu.citadel.compiler.ScannerException;
import edu.citadel.compiler.Source;

import java.io.*;
import java.util.HashMap;


public class Scanner
  {
    private Source   source;
    private Symbol   symbol;
    private Position position;
    private String   text;

    private StringBuilder scanBuffer;

    /** maps strings to opcode symbols */
    private HashMap<String, Symbol> opCodeMap;


    /**
     * Initialize scanner with its associated source and advance
     * to the first token.
     */
    public Scanner(Source source) throws IOException
      {
        this.source  = source;
        scanBuffer   = new StringBuilder(100);

        // initialize HashMap with reserved word symbols
        opCodeMap = new HashMap<>(100);
        Symbol[] symbols = Symbol.values();
        for (Symbol symbol : symbols)
          {
            if (symbol.isOpCode())
                opCodeMap.put(symbol.toString(), symbol);
          }

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
     * Returns a reference to the current symbol in the source file.
     */
    public Symbol getSymbol()
      {
        return symbol;
      }


    /**
     * Returns a reference to the position of the current
     * symbol in the source file.
     */
    public Position getPosition()
      {
        return position;
      }


    /**
     * Advance to the next token in the source file.
     */
    public void advance() throws IOException
      {
        try
          {
            skipWhiteSpace();

            // currently at starting character of next token
            position = source.getCharPosition();
            text = null;

            if (source.getChar() == Source.EOF)
              {
                // set symbol but don't advance
                symbol = Symbol.EOF;
              }
            else if (Character.isLetter((char) source.getChar()))
              {
                // opcode symbol, identifier, or label
                String idString = scanIdentifier();
                symbol = getIdentifierSymbol(idString);

                if (symbol == Symbol.identifier)
                  {
                    // check to see if we have a label
                    if (source.getChar() == ':')
                      {
                        symbol = Symbol.labelId;
                        text = idString + ":";
                        source.advance();
                      }
                    else
                        text = idString;
                  }
              }
            else if (Character.isDigit((char) source.getChar()))     // integer literal
              {
                text   = scanIntegerLiteral();
                symbol = Symbol.intLiteral;
              }
            else
                switch((char) source.getChar())
                  {
                    case ';':
                        skipComment();
                        advance();   // continue scanning for next token
                        break;
                    case '\'':
                        text = scanCharLiteral();
                        symbol = Symbol.charLiteral;
                        break;
                    case '\"':
                        text = scanStringLiteral();
                        symbol = Symbol.stringLiteral;
                        break;
                    case '-':
                        // should be a negative integer literal
                        source.advance();
                        if (Character.isDigit((char) source.getChar()))     // integer literal
                          {
                            text = "-" + scanIntegerLiteral();
                            symbol = Symbol.intLiteral;
                          }
                        else
                          {
                            throw error("Expecting an integer literal");
                          }
                        break;
                    default:
                        // error: invalid token
                        source.advance();
                        throw error("Invalid Token");
                  }
          }
        catch (ScannerException e)
          {
            // stop on first error -- no error recovery
            ErrorHandler.getInstance().reportError(e);
            System.exit(1);
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
        // assumes that source.getChar() is the first character of the identifier
        assert Character.isLetter((char) source.getChar()) :
            "scanIdentifier(): check identifier start for letter at position "
            + getPosition();

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
     * Scans characters in the source file for a valid integer literal.
     * Assumes that source.getChar() is the first character of the Integer literal.
     *
     * @return the string of digits for the integer literal.
     */
    private String scanIntegerLiteral() throws ScannerException, IOException
      {
        // assumes that source.getChar() is the first digit of the integer literal
        assert Character.isDigit((char) source.getChar()) :
            "scanIntegerLiteral(): check integer literal start for digit at position "
            + getPosition();

        clearScanBuffer();

        do
          {
            scanBuffer.append((char) source.getChar());
            source.advance();
          }
        while (Character.isDigit((char) source.getChar()));

        return scanBuffer.toString();
      }


    private void skipComment() throws ScannerException, IOException
      {
        // assumes that source.getChar() is the leading ';'
        assert (char) source.getChar() == ';' :
            "skipComment(): check for ';' to start comment";

        skipToEndOfLine();
        source.advance();
      }


    /**
     * Scans characters in the source file for a String literal.
     * Escaped characters are converted; e.g., '\t' is converted to
     * the tab character.  Assumes that source.getChar() is the
     * opening quote (") of the String literal.
     *
     * @return the string of characters for the string literal, including
     *         opening and closing quotes
     */
    private String scanStringLiteral() throws ScannerException, IOException
      {
        // assumes that source.getChar() is the opening double quote for the string literal
        assert (char) source.getChar() == '\"' :
            "scanStringLiteral(): check for opening quote (\") at position "
            + getPosition();

        clearScanBuffer();

        do
          {
            checkGraphicChar(source.getChar());
            char c = (char) source.getChar();

            if (c == '\\')
                scanBuffer.append(scanEscapedChar());   // call to scanEscapedChar() advances source
            else
              {
                scanBuffer.append(c);
                source.advance();
              }
          }
        while ((char) source.getChar() != '\"');

        scanBuffer.append('\"');     // append closing quote
        source.advance();

        return scanBuffer.toString();
      }


    /**
     * Scans characters in the source file for a valid char literal.
     * Escaped characters are converted; e.g., '\t' is converted to the
     * tab character.  Assumes that source.getChar() is the opening
     * single quote (') of the Char literal.
     *
     * @return the string of characters for the char literal, including
     *         opening and closing single quotes.
     */
    private String scanCharLiteral() throws ScannerException, IOException
      {
        // assumes that source.getChar() is the opening single quote for the char literal
        assert (char) source.getChar() == '\'' :
            "scanCharLiteral(): check for opening quote (\') at position " + getPosition();

        clearScanBuffer();

        char c = (char) source.getChar();           // opening quote
        scanBuffer.append(c);                       // append the opening quote

        source.advance();
        checkGraphicChar(source.getChar());
        c = (char) source.getChar();                // the character literal

        if (c == '\\')                              // escaped character
            scanBuffer.append(scanEscapedChar());   // call to scanEscapedChar() advances source
        else if (c == '\'')                         // check for empty char literal
          {
            source.advance();
            throw error("Char literal must contain exactly one character");
          }
        else
          {
            scanBuffer.append(c);                       // append the character literal
            source.advance();
          }

        checkGraphicChar(source.getChar());
        c = (char) source.getChar();                // should be the closing quote

        if (c == '\'')                              // should be the closing quote
          {
            scanBuffer.append(c);                   // append the closing quote
            source.advance();
          }
        else
            throw error("Char literal not closed properly");

        return scanBuffer.toString();
      }


    /**
     * Scans characters in the source file for an escaped character; i.e.,
     * a character preceded by a backslash.  This method handles escape
     * characters \b, \t, \n, \f, \r, \", \', and \\.  If the character
     * following a backslash is anything other than one of these characters,
     * then an exception is thrown.  Assumes that source.getChar() is the
     * escape character (\).
     *
     * @return the value for an escaped character.
     */
    private char scanEscapedChar() throws ScannerException, IOException
      {
        // assumes that source.getChar() is a backslash character
        assert (char) source.getChar() == '\\' :
            "scanEscapedChar(): check for escape character ('\\') at position " + getPosition();

        // Need to save current position for error reporting.
        Position backslashPosition = source.getCharPosition();

        source.advance();
        checkGraphicChar(source.getChar());
        char c = (char) source.getChar();

        source.advance();  // leave source at second character following the backslash

        switch (c)
          {
            case 'b'  : return '\b';   // backspace
            case 't'  : return '\t';   // tab
            case 'n'  : return '\n';   // linefeed (a.k.a. newline)
            case 'f'  : return '\f';   // form feed
            case 'r'  : return '\r';   // carriage return
            case '\"' : return '\"';   // double quote
            case '\'' : return '\'';   // single quote
            case '\\' : return '\\';   // backslash
            default   : throw new ScannerException(backslashPosition, "Illegal escape character.");
          }
      }


    /**
     * Returns the symbol associated with an identifier
     * (Symbol.ADD, Symbol.AND, Symbol.identifier, etc.)
     */
    private Symbol getIdentifierSymbol(String idString)
      {
         return opCodeMap.getOrDefault(idString.toUpperCase(), Symbol.identifier);
      }


    /**
     * Fast skip over white space.
     */
    private void skipWhiteSpace() throws IOException
      {
        while (Character.isWhitespace((char) source.getChar()))
          {
            source.advance();
          }
      }


    /**
     * Advances over source characters to the end of the current line.
     */
    private void skipToEndOfLine() throws ScannerException, IOException
      {
        while ((char) source.getChar() != '\n')
          {
            source.advance();
            checkEOF();
          }
      }


    /**
     * Performs a linear search of the array for the given value.
     *
     * @return the index of the value in the array if found, otherwise -1.
     */
    private int search(Symbol[] symbols, Symbol value)
      {
        for (int i = 0;  i < symbols.length;  ++i)
          {
            if (symbols[i].equals(value))
                return i;
          }
        return -1;
      }


    /**
     * Advance until the symbol in the source file matches one of the
     * symbols in the given array or until end of file is encountered.
     */
    public void advanceTo(Symbol[] symbols) throws IOException
      {
        while (true)
          {
            if (search(symbols, getSymbol()) >= 0
                || source.getChar() == Source.EOF)
                return;
            else
                advance();
          }

      }


    /**
     * Checks that the integer represents a graphic character in the Unicode
     * Basic Multilingual Plane (BMP).
     *
     * @throws ScannerException if the integer does not represent a BMP graphic
     *         character.
     */
    private void checkGraphicChar(int n) throws ScannerException
      {
        if (n == Source.EOF)
            throw error("End of file reached before closing quote for Char or String literal.");
        else if (n > 0xffff)
            throw error("Character not in Unicode Basic Multilingual Pane (BMP)");
        else
          {
            char c = (char) n;
            if (c == '\r' || c == '\n')   // special check for end of line
                throw error("Char and String literals can not extend past end of line.");
            else if (Character.isISOControl(c))    // Sorry.  No ISO control characters.
                throw new ScannerException(source.getCharPosition(),
                    "Control characters not allowed in Char or String literal.");
          }
      }


    /**
     * Throws a ScannerException with the specified error message.
     */
    private ScannerException error(String errorMsg)
      {
        return new ScannerException(getPosition(), errorMsg);
      }


    /**
     * Used to check for EOF in the middle of scanning tokens that
     * require closing characters such as strings and comments.
     *
     * @throws ScannerException if source is at end of file.
     */
    private void checkEOF() throws ScannerException
      {
        if (source.getChar() == Source.EOF)
            throw error("Unexpected end of file");
      }
  }
