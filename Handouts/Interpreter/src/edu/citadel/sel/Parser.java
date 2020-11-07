package edu.citadel.sel;


import edu.citadel.compiler.Position;
import edu.citadel.sel.ast.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


/**
 * This class uses recursive descent to perform syntax analysis of the SEL
 * source language and to generate an abstract syntax tree for the interpreter.
 */
public class Parser
  {
    private Scanner scanner;


    /**
     * Construct a parser with the specified scanner.
     */
    public Parser(Scanner scanner)
      {
        this.scanner = scanner;
      }

    
    /**
     * Parse the following grammar rule:<br>
     * <code>program = ( statement )* .</code>
     *
     * @return the parsed program Expression
     */
    public Program parseProgram() throws IOException, InterpreterException
      {
        List<Expression> expressions = new ArrayList<>(50);

        while (scanner.getSymbol() != Symbol.EOF)
            expressions.add(parseExpression());

        return new Program(expressions);
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>expression = assignExpr | numericExpr  ( EOL )?.</code>
     *
     * @return the parsed Expression
     */
    public Expression parseExpression() throws IOException, InterpreterException
      {
        Expression returnExpr;
        
        if (scanner.getSymbol() == Symbol.identifier && scanner.getPeekSymbol() == Symbol.assign)
            returnExpr = parseAssignExpr();
        else
            returnExpr = parseNumericExpr();
        
        if (scanner.getSymbol() == Symbol.EOL)
            match(Symbol.EOL);
        
        return returnExpr;
      }

    
    /**
     * Parse the following grammar rule:<br>
     * <code>assignExpr = identifier "<-" numericExpr .</code>
     *
     * @return the parsed assign Expression
     */
    public Expression parseAssignExpr() throws IOException, InterpreterException
      {
        assert scanner.getSymbol() == Symbol.identifier && scanner.getPeekSymbol() == Symbol.assign;
        
        Token idToken = scanner.getToken();
        scanner.advance();
        match(Symbol.assign);
        Expression expr = parseExpression();
        return new AssignExpr(idToken, expr);
      }

    /**
    
     * Parse the following grammar rules:<br>
     * <code>numericExpr = ( addOp )? term (addOp term)* .</code>
     *       addOp = "+" | "-" . </code>
     *
     * @return the parsed numeric Expression
     */
    public Expression parseNumericExpr() throws IOException, InterpreterException
      {
        Expression expr = null;
        Token operator = null;

        if (scanner.getSymbol().isAddingOperator())
          {
            if (scanner.getSymbol() == Symbol.minus)
                operator = scanner.getToken();

            // leave operator as null if it is "+"

            matchCurrentSymbol();
          }

        expr = parseTerm();

        if (operator != null)
            expr = new NegationExpr(operator, expr);

        while (scanner.getSymbol().isAddingOperator())
          {
            operator = scanner.getToken();
            matchCurrentSymbol();
            Expression expr2 = parseTerm();
            expr = new AddingExpr(expr, operator, expr2);
          }

        return expr;
      }


    /**
     * Parse the following grammar rules:<br>
     * <code>term = factor ( multiplyingOp factor )* .</code>
     *       multiplyingOp = "*" | "/".</code>
     *
     * @return the parsed term Expression
     */
    public Expression parseTerm() throws IOException, InterpreterException
      {
        Expression expr = parseFactor();

        while (scanner.getSymbol().isMultiplyingOperator())
          {
            Token operator = scanner.getToken();
            matchCurrentSymbol();
            Expression expr2 = parseFactor();
            expr = new MultiplyingExpr(expr, operator, expr2);
          }

        return expr;
      }


    /**
     * Parse the following grammar rule:<br>
     * <code>factor = numericLiteral | identifier | "(" expression ")" .</code>
     *
     * @return the parsed factor Expression
     */
    public Expression parseFactor() throws IOException, InterpreterException
      {
        Expression expr = null;

        if (scanner.getSymbol() == Symbol.numericLiteral)
          {
            expr = parseNumericLiteral();
          }
        else if (scanner.getSymbol() == Symbol.identifier)
          {
            expr = parseIdExpr();
          }
        else if (scanner.getSymbol() == Symbol.leftParen)
          {
            matchCurrentSymbol();
            expr = parseNumericExpr();
            match(Symbol.rightParen);
          }
        else
            error("Invalid expression.");

        return expr;
      }


    /**
     * Parse a numeric literal expression.
     *
     * @return the parsed numeric literal Expression
     */
    public Expression parseNumericLiteral() throws IOException, InterpreterException
      {
        assert scanner.getSymbol() == Symbol.numericLiteral;

        Expression expr = new NumericLiteral(scanner.getToken());
        matchCurrentSymbol();
        return expr;
      }


    /**
     * Parse an identifier expression.
     *
     * @return the parsed identifier Expression
     */
    public Expression parseIdExpr() throws IOException, InterpreterException
      {
        assert scanner.getSymbol() == Symbol.identifier;

        Expression expr = new IdExpr(scanner.getToken());
        matchCurrentSymbol();
        return expr;
      }


    /**
     * Check that the current scanner symbol is the expected symbol.
     * If it is, then advance the scanner.  Otherwise, signal an error.
     */
    private void match(Symbol expectedSymbol) throws IOException, InterpreterException
      {
        if (scanner.getSymbol() == expectedSymbol)
            scanner.advance();
        else
          {
            String errorMsg = "Expecting \"" + expectedSymbol + "\" but found \""
                + scanner.getSymbol() + "\" instead.";
            error(errorMsg);
          }
      }


    /**
     * Advance the scanner.  This method represents an unconditional
     * match with the current scanner symbol.
     */
    private void matchCurrentSymbol() throws IOException, InterpreterException
      {
        scanner.advance();
      }


    /**
     * Throws an exception with an appropriate error message.
     */
    private void error(String message) throws InterpreterException
      {
        Position position = scanner.getToken().getPosition();
        String errorMsg = "*** Syntax error detected near position "
                         + position + ":\n    " + message;
        throw new InterpreterException(errorMsg);
      }
  }
