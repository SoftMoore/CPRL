package edu.citadel.cprl;


import edu.citadel.compiler.Position;
import edu.citadel.compiler.ParserException;
import edu.citadel.compiler.InternalCompilerException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.ast.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This class uses recursive descent to perform syntax analysis of
 * the CPRL source language and to generate an abstract syntax tree.
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
    private LoopContext loopContext;
    private SubprogramContext subprogramContext;


    /**
     * Construct a parser with the specified scanner.
     */
    public Parser(Scanner scanner)
      {
        this.scanner = scanner;
        idTable = new IdTable();
        loopContext = new LoopContext();
        subprogramContext = new SubprogramContext();
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>program = declarativePart statementPart "." .</code>
     *
     * @return the parsed program.  Returns null if parsing fails.
     */
    public Program parseProgram() throws IOException
      {
        try
          {
            DeclarativePart declPart = parseDeclarativePart();
            StatementPart stmtPart = parseStatementPart();
            match(Symbol.dot);
            match(Symbol.EOF);
            return new Program(declPart, stmtPart);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.EOF};
            recover(followers);
            return null;
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>declarativePart = initialDecls subprogramDecls .</code>
     *
     * @return the parsed declarative part.
     */
    public DeclarativePart parseDeclarativePart() throws IOException
      {
        List<InitialDecl>    initialDecls = parseInitialDecls();
        List<SubprogramDecl> subprogDecls = parseSubprogramDecls();

        return new DeclarativePart(initialDecls, subprogDecls);
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>initialDecls = ( initialDecl )* .</code>
     *
     * @return the list of initial declarations.
     */
    public List<InitialDecl> parseInitialDecls() throws IOException
      {
        List<InitialDecl> initialDecls = new ArrayList<>(10);

        while (scanner.getSymbol().isInitialDeclStarter())
          {
            InitialDecl decl = parseInitialDecl();

            if (decl instanceof VarDecl)
              {
                // add the single variable declarations
                VarDecl varDecl = (VarDecl) decl;
                for (SingleVarDecl singleVarDecl : varDecl.getSingleVarDecls())
                    initialDecls.add(singleVarDecl);
              }
            else
                initialDecls.add(decl);
          }

        return initialDecls;
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>initialDecl = constDecl | arrayTypeDecl | varDecl .</code>
     *
     * @return the parsed initial declaration.  Returns null if parsing fails.
     */
    public InitialDecl parseInitialDecl() throws IOException
      {
// ...   throw an internal error if the symbol is not one of constRW, varRW, or typeRW
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>constDecl = "const" constId ":=" literal ";" .</code>
     *
     * @return the parsed constant declaration.  Returns null if parsing fails.
     */
    public ConstDecl parseConstDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>literal = intLiteral | charLiteral | stringLiteral | booleanLiteral .<br>
     *    booleanLiteral = "true" | "false" .</code>
     *
     * @return the parsed literal token.  Returns null if parsing fails.
     */
    public Token parseLiteral() throws IOException
      {
        try
          {
            if (scanner.getSymbol().isLiteral())
              {
                Token literal = scanner.getToken();
                matchCurrentSymbol();
                return literal;
              }
            else
                throw error("Invalid literal expression.");
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(factorFollowers);
            return null;
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>varDecl = "var" identifiers ":" typeName ";" .</code>
     *
     * @return the parsed variable declaration.  Returns null if parsing fails.
     */
    public VarDecl parseVarDecl() throws IOException
      {
        try
          {
            match(Symbol.varRW);
            List<Token> identifiers = parseIdentifiers();
            match(Symbol.colon);
            Type varType = parseTypeName();
            match(Symbol.semicolon);

            ScopeLevel scopeLevel = idTable.getCurrentLevel();
            VarDecl varDecl = new VarDecl(identifiers, varType, scopeLevel);

            for (SingleVarDecl decl : varDecl.getSingleVarDecls())
                idTable.add(decl);

            return varDecl;
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(initialDeclFollowers);
            return null;
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
    public ArrayTypeDecl parseArrayTypeDecl() throws IOException
      {
// ...
// Hint: If parseConstDecl() returns a null value, create a "dummy" token for the
//       ConstValue to prevent additional errors associated with a null pointer; e.g.,
//       Token token = new Token(Symbol.intLiteral, scanner.getPosition(), "0");
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>typeName = "Integer" | "Boolean" | "Char" | typeId .</code>
     *
     * @return the parsed named type object.  Returns Type.UNKNOWN if parsing fails.
     */
    public Type parseTypeName() throws IOException
      {
        Type type = Type.UNKNOWN;

        try
          {
            if (scanner.getSymbol() == Symbol.IntegerRW)
              {
                type = Type.Integer;
                matchCurrentSymbol();
              }
            else if (scanner.getSymbol() == Symbol.BooleanRW)
              {
                type = Type.Boolean;
                matchCurrentSymbol();
              }
            else if (scanner.getSymbol() == Symbol.CharRW)
              {
                type = Type.Char;
                matchCurrentSymbol();
              }
            else if (scanner.getSymbol() == Symbol.identifier)
              {
                Token typeId = scanner.getToken();
                matchCurrentSymbol();
                Declaration decl = idTable.get(typeId);

                if (decl != null)
                  {
                    if (decl instanceof ArrayTypeDecl)
                        type = decl.getType();
                    else
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

        return type;
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>subprogramDecls = ( subprogramDecl )* .</code>
     *
     * @return the list of subprogram declarations.
     */
    public List<SubprogramDecl> parseSubprogramDecls() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>subprogramDecl = procedureDecl | functionDecl .</code>
     *
     * @return the parsed subprogram declaration.  Returns null if parsing fails.
     */
    public SubprogramDecl parseSubprogramDecl() throws IOException
      {
// ...   throw an internal error if the symbol is not one of procedureRW or functionRW
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>procedureDecl = "procedure" procId ( formalParameters )?
     *      "is" initialDecls statementPart procId ";" .</code>
     *
     * @return the parsed procedure declaration.  Returns null if parsing fails.
     */
    public ProcedureDecl parseProcedureDecl() throws IOException
      {
        try
          {
            match(Symbol.procedureRW);
            Token procId = scanner.getToken();
            match(Symbol.identifier);
            ProcedureDecl procDecl = new ProcedureDecl(procId);
            idTable.add(procDecl);
            idTable.openScope();

            if (scanner.getSymbol() == Symbol.leftParen)
                procDecl.setFormalParams(parseFormalParameters());

            match(Symbol.isRW);
            procDecl.setInitialDecls(parseInitialDecls());

            subprogramContext.beginSubprogramDecl(procDecl);
            procDecl.setStatementPart(parseStatementPart());
            subprogramContext.endSubprogramDecl();
            idTable.closeScope();

            Token procId2 = scanner.getToken();
            match(Symbol.identifier);

            if (!procId.getText().equals(procId2.getText()))
                throw error(procId2.getPosition(), "Procedure name mismatch.");

            match(Symbol.semicolon);
            return procDecl;
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(subprogDeclFollowers);
            return null;
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>functionDecl = "function" funcId ( formalParameters )? "return" typeName
     *       "is" initialDecls statementPart funcId ";" .</code>
     *
     * @return the parsed function declaration.  Returns null if parsing fails.
     */
    public FunctionDecl parseFunctionDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>formalParameters = "(" parameterDecl ( "," parameterDecl )* ")" .</code>
     *
     * @return a list of parameter declarations.  Returns an empty list if parsing fails.
     */
    public List<ParameterDecl> parseFormalParameters() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>parameterDecl = ( "var" )? paramId ":" typeName .</code>
     *
     * @return the parsed parameter declaration.  Returns null if parsing fails.
     */
    public ParameterDecl parseParameterDecl() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>statementPart = "begin" statements "end" .</code>
     *
     * @return the parsed statement part.  Returns null if parsing fails.
     */
    public StatementPart parseStatementPart() throws IOException
      {
        try
          {
            match(Symbol.beginRW);
            List<Statement> statements = parseStatements();
            match(Symbol.endRW);
            return new StatementPart(statements);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.dot, Symbol.identifier};
            recover(followers);
            return null;
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>statements = ( statement )* .</code>
     *
     * @return a list of statements.
     */
    public List<Statement> parseStatements() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>statement = assignmentStmt | ifStmt | loopStmt | exitStmt | readStmt
     *                 | writeStmt | writelnStmt | procedureCallStmt | returnStmt .</code>
     *
     * @return the parsed statement.  Returns null if parsing fails.
     */
    public Statement parseStatement() throws IOException
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
     *
     * @return the parsed assignment statement.  Returns null if parsing fails.
     */
    public AssignmentStmt parseAssignmentStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>ifStmt = "if" booleanExpr "then" statements<br>
     *       ( "elsif" booleanExpr "then" statements )*<br>
     *       ( "else" statements )? "end" "if" ";" .</code>
     *
     * @return the parsed if statement.  Returns null if parsing fails.
     */
    public IfStmt parseIfStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>loopStmt = ( "while" booleanExpr )? "loop" statements
     *       "end" "loop" ";" .</code>
     *
     * @return the parsed loop statement.  Returns null if parsing fails.
     */
    public LoopStmt parseLoopStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>exitStmt = "exit" ( "when" booleanExpr )? ";" .</code>
     *
     * @return the parsed exit statement.  Returns null if parsing fails.
     */
    public ExitStmt parseExitStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>readStmt = "read" variable ";" .</code>
     *
     * @return the parsed read statement.  Returns null if parsing fails.
     */
    public ReadStmt parseReadStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>writeStmt = "write" expressions ";" .</code>
     *
     * @return the parsed write statement.  Returns null if parsing fails.
     */
    public WriteStmt parseWriteStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>expressions = expression ( "," expression )* .</code>
     *
     * @return a list of Expressions.
     */
    public List<Expression> parseExpressions() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>writelnStmt = "writeln" ( expressions )? ";" .</code>
     *
     * @return the parsed writeln statement.  Returns null if parsing fails.
     */
    public WritelnStmt parseWritelnStmt() throws IOException
      {
        try
          {
            match(Symbol.writelnRW);

            List<Expression> expressions;
            if (scanner.getSymbol().isExprStarter())
                expressions = parseExpressions();
            else
                expressions = Collections.emptyList();

            match(Symbol.semicolon);
            return new WritelnStmt(expressions);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(stmtFollowers);
            return null;
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>procedureCallStmt = procId ( actualParameters )? ";" .</code>
     *
     * @return the parsed procedure call statement.  Returns null if parsing fails.
     */
    public ProcedureCallStmt parseProcedureCallStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>actualParameters = "(" expressions ")" .</code>
     *
     * @return a list of expressions.  Returns an empty list if parsing fails.
     */
    public List<Expression> parseActualParameters() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>returnStmt = "return" ( expression )? ";" .</code>
     *
     * @return the parsed return statement.  Returns null if parsing fails.
     */
    public ReturnStmt parseReturnStmt() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>variable = ( varId | paramId ) ( "[" expression "]" )* .</code>
     * <br>
     * This method provides common logic for methods parseVariable() and
     * parseNamedValue().  The method does not handle any ParserExceptions
     * but throws them back to the calling method where they can be handled
     * appropriately.
     *
     * @return the parsed variable.
     * @throws ParserException if parsing fails.
     * @see #parseVariable()
     * @see #parseNamedValue()
     */
    public Variable parseVariableExpr() throws IOException, ParserException
      {
        Token idToken = scanner.getToken();
        match(Symbol.identifier);
        Declaration decl = idTable.get(idToken);

        if (decl == null)
          {
            String errorMsg = "Identifier \"" + idToken + "\" has not been declared.";
            throw error(idToken.getPosition(), errorMsg);
          }
        else if (!(decl instanceof NamedDecl))
          {
            String errorMsg = "Identifier \"" + idToken + "\" is not a variable.";
            throw error(idToken.getPosition(), errorMsg);
          }

        NamedDecl namedDecl = (NamedDecl) decl;

        List<Expression> indexExprs = new ArrayList<>(3);
        while (scanner.getSymbol() == Symbol.leftBracket)
          {
            matchCurrentSymbol();
            indexExprs.add(parseExpression());
            match(Symbol.rightBracket);
          }

        return new Variable(namedDecl, idToken.getPosition(), indexExprs);
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>variable = ( varId | paramId ) ( "[" expression "]" )* .</code>
     *
     * @return the parsed variable.  Returns null if parsing fails.
     */
    public Variable parseVariable() throws IOException
      {
        try
          {
            return parseVariableExpr();
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            Symbol[] followers = {Symbol.assign, Symbol.semicolon};
            recover(followers);
            return null;
          }
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>expression = relation ( logicalOp relation )* .<br>
     *       logicalOp = "and" | "or" . </code>
     *
     * @return the parsed expression.  Returns null if parsing fails.
     */
    public Expression parseExpression() throws IOException
      {
        Expression expr = parseRelation();

        while (scanner.getSymbol().isLogicalOperator())
          {
            Token operator = scanner.getToken();
            matchCurrentSymbol();
            Expression expr2 = parseRelation();
            expr = new LogicalExpr(expr, operator, expr2);
          }

        return expr;
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>relation = simpleExpr ( relationalOp simpleExpr )? .<br>
     *   relationalOp = "=" | "!=" | "&lt;" | "&lt;=" | "&gt;" | "&gt;=" .</code>
     *
     * @return the parsed relation expression.  Returns null if parsing fails.
     */
    public Expression parseRelation() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>simpleExpr = ( addingOp )? term ( addingOp term )* .<br>
     *       addingOp = "+" | "-" .</code>
     *
     * @return the parsed simple expression.  Returns null if parsing fails.
     */
    public Expression parseSimpleExpr() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>term = factor ( multiplyingOp factor )* .<br>
     *       multiplyingOp = "*" | "/" | "mod" .</code>
     *
     * @return the parsed term expression.  Returns null if parsing fails.
     */
    public Expression parseTerm() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>factor = "not" factor | constValue | namedValue | functionCall
     *              | "(" expression ")" .</code>
     *
     * @return the parsed factor expression.  Returns null if parsing fails.
     */
    public Expression parseFactor() throws IOException
      {
        try
          {
            Expression expr;

            if (scanner.getSymbol() == Symbol.notRW)
              {
                Token operator = scanner.getToken();
                matchCurrentSymbol();
                Expression factorExpr = parseFactor();
                expr = new NotExpr(operator, factorExpr);
              }
            else if (scanner.getSymbol().isLiteral())
              {
                // Handle constant literals separately from constant identifiers.
                expr = parseConstValue();
              }
            else if (scanner.getSymbol() == Symbol.identifier)
              {
                // Handle identifiers based on whether they are declared
                // as variables, constants, or functions.
                Token idToken = scanner.getToken();
                Declaration decl = idTable.get(idToken);

                if (decl != null)
                  {
                    if (decl instanceof ConstDecl)
                        expr = parseConstValue();
                    else if (decl instanceof NamedDecl)
                        expr = parseNamedValue();
                    else if (decl instanceof FunctionDecl)
                        expr = parseFunctionCall();
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
                expr = parseExpression();
                match(Symbol.rightParen);
              }
            else
                throw error("Invalid expression.");

            return expr;
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(factorFollowers);
            return null;
          }
      }

    /**
     * Parse the following grammar rule:<br>
     * <code>constValue = literal | constId .</code>
     *
     * @return the parsed constant value.  Returns null if parsing fails.
     */
    public ConstValue parseConstValue() throws IOException
      {
// ...
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>namedValue = variable .</code>
     *
     * @return the parsed named value.  Returns null if parsing fails.
     */
    public NamedValue parseNamedValue() throws IOException
      {
        try
          {
            Variable variableExpr = parseVariableExpr();
            return new NamedValue(variableExpr);
          }
        catch (ParserException e)
          {
            ErrorHandler.getInstance().reportError(e);
            recover(factorFollowers);
            return null;
          }
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>functionCall = funcId ( actualParameters )? .</code>
     *
     * @return the parsed function call.  Returns null if parsing fails.
     */
    public FunctionCall parseFunctionCall() throws IOException
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
    private ParserException error(String errorMsg)
      {
        return new ParserException(scanner.getPosition(), errorMsg);
      }


    /**
     * Create a parser exception with the specified error position and message.
     */
    private ParserException error(Position errorPos, String errorMsg)
      {
        return new ParserException(errorPos, errorMsg);
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
