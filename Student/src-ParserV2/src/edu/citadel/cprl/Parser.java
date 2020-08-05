package edu.citadel.cprl;


import edu.citadel.compiler.Position;
import edu.citadel.compiler.ParserException;
import edu.citadel.compiler.InternalCompilerException;
import edu.citadel.compiler.ErrorHandler;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;


/**
 * This class uses recursive descent to perform syntax analysis of
 * the CPRL source language.
 */
public class Parser
  {
    /**
     * Symbols that can follow an initial declaration.
     */
    private static final Symbol[] initialDeclFollowers =
      {
// ...
      };


    /**
     * Symbols that can follow a subprogram declaration.
     */
    private static final Symbol[] subprogDeclFollowers =
      {
// ...
      };


    /**
     * Symbols that can follow a statement.
     */
    private static final Symbol[] stmtFollowers =
      {
// ...
      };


    /**
     * Symbols that can follow a factor.
     */
    private static final Symbol[] factorFollowers =
      {
        Symbol.semicolon, Symbol.loopRW,       Symbol.thenRW,      Symbol.rightParen,
        Symbol.andRW,     Symbol.orRW,         Symbol.equals,      Symbol.notEqual,
        Symbol.lessThan,  Symbol.lessOrEqual,  Symbol.greaterThan, Symbol.greaterOrEqual,
        Symbol.plus,      Symbol.minus,        Symbol.times,       Symbol.divide,
        Symbol.modRW,     Symbol.rightBracket, Symbol.comma
      };


    private Scanner scanner;
    private IdTable idTable;


    /**
     * Construct a parser with the specified scanner.
     */
    public Parser(Scanner scanner)
      {
        this.scanner = scanner;
        idTable = new IdTable();
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>program = declarativePart statementPart "." .</code>
     */
    public void parseProgram() throws IOException
      {
        try
          {
            parseDeclarativePart();
            parseStatementPart();
            match(Symbol.dot);
            match(Symbol.EOF);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.EOF};
            recover(followers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>declarativePart = initialDecls subprogramDecls .</code>
     */
    public void parseDeclarativePart() throws IOException
      {
        parseInitialDecls();
        parseSubprogramDecls();
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>initialDecls = ( initialDecl )* .</code>
     */
    public void parseInitialDecls() throws IOException
      {
        while (scanner.getSymbol().isInitialDeclStarter())
            parseInitialDecl();
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>initialDecl = constDecl | arrayTypeDecl | varDecl .</code>
     */
    public void parseInitialDecl() throws IOException
      {
// ...   throw an internal error if the symbol is not one of constRW, varRW, or typeRW
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>constDecl = "const" constId ":=" literal ";" .</code>
     */
    public void parseConstDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>literal = intLiteral | charLiteral | stringLiteral | booleanLiteral .
     *    booleanLiteral = "true" | "false" .</code>
     */
    public void parseLiteral() throws IOException
      {
        try
          {
            if (scanner.getSymbol().isLiteral())
                matchCurrentSymbol();
            else
                throw error("Invalid literal expression.");
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(factorFollowers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>varDecl = "var" identifiers ":" typeName ";" .</code>
     */
    public void parseVarDecl() throws IOException
      {
        try
          {
            match(Symbol.varRW);
            List<Token> identifiers = parseIdentifiers();
            match(Symbol.colon);
            parseTypeName();
            match(Symbol.semicolon);

            for (Token identifier : identifiers)
                idTable.add(identifier, IdType.variableId);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(initialDeclFollowers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>identifiers = identifier ( "," identifier )* .</code>
     *
     * @return the list of identifier tokens.  Returns an empty list if parsing fails.
     */
    public List<Token> parseIdentifiers() throws IOException
      {
        try
          {
            List<Token> identifiers = new ArrayList<>(10);
            Token idToken = scanner.getToken();
            match(Symbol.identifier);
            identifiers.add(idToken);

            while (scanner.getSymbol() == Symbol.comma)
              {
                matchCurrentSymbol();
                idToken = scanner.getToken();
                match(Symbol.identifier);
                identifiers.add(idToken);
              }

            return identifiers;
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.colon};
            recover(followers);
            return Collections.emptyList();
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>arrayTypeDecl = "type" typeId "=" "array" "[" intConstValue "]"
     *                       "of" typeName ";" .</code>
     */
    public void parseArrayTypeDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>typeName = "Integer" | "Boolean" | "Char" | typeId .</code>
     */
    public void parseTypeName() throws IOException
      {
        try
          {
            if (scanner.getSymbol() == Symbol.IntegerRW)
                matchCurrentSymbol();
            else if (scanner.getSymbol() == Symbol.BooleanRW)
                matchCurrentSymbol();
            else if (scanner.getSymbol() == Symbol.CharRW)
                matchCurrentSymbol();
            else if (scanner.getSymbol() == Symbol.identifier)
              {
                Token typeId = scanner.getToken();
                matchCurrentSymbol();
                IdType idType = idTable.get(typeId);

                if (idType != null)
                  {
                    if (idType != IdType.arrayTypeId)
                        throw error(typeId.getPosition(), "Identifier \""
                                  + typeId + "\" is not a valid type name.");
                  }
                else
                    throw error(typeId.getPosition(), "Identifier \""
                              + typeId + "\" has not been declared.");
              }
            else
                throw error("Invalid type name.");
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.semicolon,  Symbol.comma,
                                  Symbol.rightParen, Symbol.isRW};
            recover(followers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>subprogramDecls = ( subprogramDecl )* .</code>
     */
    public void parseSubprogramDecls() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>subprogramDecl = procedureDecl | functionDecl .</code>
     */
    public void parseSubprogramDecl() throws IOException
      {
// ...   throw an internal error if the symbol is not one of procedureRW or functionRW
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>procedureDecl = "procedure" procId ( formalParameters )?
     *      "is" initialDecls statementPart procId ";" .</code>
     */
    public void parseProcedureDecl() throws IOException
      {
        try
          {
            match(Symbol.procedureRW);
            Token procId = scanner.getToken();
            match(Symbol.identifier);
            idTable.add(procId, IdType.procedureId);
            idTable.openScope();

            if (scanner.getSymbol() == Symbol.leftParen)
                parseFormalParameters();

            match(Symbol.isRW);
            parseInitialDecls();
            parseStatementPart();
            idTable.closeScope();

            Token procId2 = scanner.getToken();
            match(Symbol.identifier);

            if (!procId.getText().equals(procId2.getText()))
                throw error(procId2.getPosition(), "Procedure name mismatch.");

            match(Symbol.semicolon);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(subprogDeclFollowers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>functionDecl = "function" funcId ( formalParameters )? "return" typeName
     *       "is" initialDecls statementPart funcId ";" .</code>
     */
    public void parseFunctionDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>formalParameters = "(" parameterDecl ( "," parameterDecl )* ")" .</code>
     */
    public void parseFormalParameters() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>parameterDecl = ( "var" )? paramId ":" typeName .</code>
     */
    public void parseParameterDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>statementPart = "begin" statements "end" .</code>
     */
    public void parseStatementPart() throws IOException
      {
        try
          {
            match(Symbol.beginRW);
            parseStatements();
            match(Symbol.endRW);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.dot, Symbol.identifier};
            recover(followers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>statements = ( statement )* .</code>
     */
    public void parseStatements() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>statement = assignmentStmt | ifStmt | loopStmt | exitStmt | readStmt
     *                 | writeStmt | writelnStmt | procedureCallStmt | returnStmt .</code>
     */
    public void parseStatement() throws IOException
      {
        // assumes that scanner.getSymbol() can start a statement
        assert scanner.getSymbol().isStmtStarter() : "Invalid statement.";

// ...

// Error recovery here is complicated for identifiers since they can both
// start a statement and appear elsewhere in the statement.  (Consider,
// for example, an assignment statement or a procedure call statement.)
// Since the most common error is to declare or reference an identifier
// incorrectly, we will assume that this is the case and advance to the
// next semicolon (which hopefully ends the erroneous statement) before
// performing error recovery.
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>assignmentStmt = variable ":=" expression ";" .</code>
     */
    public void parseAssignmentStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>ifStmt = "if" booleanExpr "then" statements<br>
     *       ( "elsif" booleanExpr "then" statements )*<br>
     *       ( "else" statements )? "end" "if" ";" .</code>
     */
    public void parseIfStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>loopStmt = ( "while" booleanExpr )? "loop" statements
     *       "end" "loop" ";" .</code>
     */
    public void parseLoopStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>exitStmt = "exit" ( "when" booleanExpr )? ";" .</code>
     */
    public void parseExitStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>readStmt = "read" variable ";" .</code>
     */
    public void parseReadStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>writeStmt = "write" expressions ";" .</code>
     */
    public void parseWriteStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>expressions = expression ( "," expression )* .</code>
     */
    public void parseExpressions() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>writelnStmt = "writeln" ( expressions )? ";" .</code>
     */
    public void parseWritelnStmt() throws IOException
      {
        try
          {
            match(Symbol.writelnRW);

            if (scanner.getSymbol().isExprStarter())
                parseExpressions();

            match(Symbol.semicolon);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(stmtFollowers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>procedureCallStmt = procId ( actualParameters )? ";" .</code>
     */
    public void parseProcedureCallStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>actualParameters = "(" expressions ")" .</code>
     */
    public void parseActualParameters() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>returnStmt = "return" ( expression )? ";" .</code>
     */
    public void parseReturnStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>variable = ( varId | paramId ) ( "[" expression "]" )* .</code>
     * <br/>
     * This helper method provides common logic for methods parseVariable() and
     * parseNamedValue().  The method does not handle any ParserExceptions but
     * throws them back to the calling method where they can be handled appropriately.
     *
     * @throws ParserException if parsing fails.
     * @see #parseVariable()
     * @see #parseNamedValue()
     */
    public void parseVariableExpr() throws IOException, ParserException
      {
        Token idToken = scanner.getToken();
        match(Symbol.identifier);
        IdType idType = idTable.get(idToken);

        if (idType == null)
          {
            String errorMsg = "Identifier \"" + idToken + "\" has not been declared.";
            throw error(idToken.getPosition(), errorMsg);
          }
        else if (idType != IdType.variableId)
          {
            String errorMsg = "Identifier \"" + idToken + "\" is not a variable.";
            throw error(idToken.getPosition(), errorMsg);
          }

        while (scanner.getSymbol() == Symbol.leftBracket)
          {
            matchCurrentSymbol();
            parseExpression();
            match(Symbol.rightBracket);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>variable = ( varId | paramId ) ( "[" expression "]" )* .</code>
     */
    public void parseVariable() throws IOException
      {
        try
          {
            parseVariableExpr();
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.assign, Symbol.semicolon};
            recover(followers);
          }
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>expression = relation ( logicalOp relation )* .<br>
     *       logicalOp = "and" | "or" . </code>
     */
    public void parseExpression() throws IOException
      {
        parseRelation();

        while (scanner.getSymbol().isLogicalOperator())
          {
            matchCurrentSymbol();
            parseRelation();
          }
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>relation = simpleExpr ( relationalOp simpleExpr )? .<br>
     *   relationalOp = "=" | "!=" | "<" | "<=" | ">" | ">=" .</code>
     */
    public void parseRelation() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>simpleExpr = ( addingOp )? term ( addingOp term )* .<br>
     *       addingOp = "+" | "-" .</code>
     */
    public void parseSimpleExpr() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>term = factor ( multiplyingOp factor )* .<br>
     *       multiplyingOp = "*" | "/" | "mod" .</code>
     */
    public void parseTerm() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>factor = "not" factor | constValue | namedValue | functionCall
     *              | "(" expression ")" .</code>
     */
    public void parseFactor() throws IOException
      {
        try
          {
            if (scanner.getSymbol() == Symbol.notRW)
              {
                matchCurrentSymbol();
                parseFactor();
              }
            else if (scanner.getSymbol().isLiteral())
              {
                // Handle constant literals separately from constant identifiers.
                parseConstValue();
              }
            else if (scanner.getSymbol() == Symbol.identifier)
              {
                // Handle identifiers based on whether they are
                // declared as variables, constants, or functions.
                Token idToken = scanner.getToken();
                IdType idType = idTable.get(idToken);

                if (idType != null)
                  {
                    if (idType == IdType.constantId)
                        parseConstValue();
                    else if (idType == IdType.variableId)
                        parseNamedValue();
                    else if (idType == IdType.functionId)
                        parseFunctionCall();
                    else
                        throw error("Identifier \"" + scanner.getToken()
                                  + "\" is not valid as an expression.");
                  }
                else
                    throw error("Identifier \"" + scanner.getToken()
                              + "\" has not been declared.");
              }
            else if (scanner.getSymbol() == Symbol.leftParen)
              {
                matchCurrentSymbol();
                parseExpression();
                match(Symbol.rightParen);
              }
            else
                throw error("Invalid expression.");
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(factorFollowers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>constValue = literal | constId .</code>
     */
    public void parseConstValue() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>namedValue = variable .</code>
     */
    public void parseNamedValue() throws IOException
      {
        try
          {
            parseVariableExpr();
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(factorFollowers);
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>functionCall = funcId ( actualParameters )? .</code>
     */
    public void parseFunctionCall() throws IOException
      {
// ...
      }


    // Utility parsing methods


    /**
     * Check that the current scanner symbol is the expected symbol.  If it is,
     * then advance the scanner.  Otherwise, throw a ParserException object.
     */
    private void match(Symbol expectedSymbol) throws IOException, ParserException
      {
        if (scanner.getSymbol() == expectedSymbol)
            scanner.advance();
        else
          {
            String errorMsg = "Expecting \"" + expectedSymbol + "\" but found \""
                            + scanner.getToken() + "\" instead.";
            throw error(errorMsg);
          }
      }


    /**
     * Advance the scanner.  This method represents an unconditional match
     * with the current scanner symbol.
     */
    private void matchCurrentSymbol() throws IOException
      {
        scanner.advance();
      }


    /**
     * Advance the scanner until the current symbol is one of the
     * symbols in the specified array of follows.
     */
    private void recover(Symbol[] followers) throws IOException
      {
        scanner.advanceTo(followers);
      }


    /**
     * Create a parser exception with the specified message and the
     * current scanner position.
     */
    private ParserException error(String errMsg)
      {
        return new ParserException(scanner.getPosition(), errMsg);
      }


    /**
     * Create a parser exception with the specified error position and message.
     */
    private ParserException error(Position errPos, String errMsg)
      {
        return new ParserException(errPos, errMsg);
      }


    /**
     * Create an internal compiler exception with the specified message
     * and the current scanner position.
     */
    private InternalCompilerException internalError(String message)
      {
        return new InternalCompilerException(scanner.getPosition(), message);
      }
  }
