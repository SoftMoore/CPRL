package edu.citadel.cprl;


/**
 * This class encapsulates the symbols (also known as token types)
 * of the programming language CPRL.
 */
public enum Symbol
  {
    // reserved words
    BooleanRW("Boolean"),
    CharRW("Char"),
    IntegerRW("Integer"),
    StringRW("String"),
    andRW("and"),
    arrayRW("array"),
    beginRW("begin"),
    classRW("class"),
    constRW("const"),
    declareRW("declare"),
    elseRW("else"),
    elsifRW("elsif"),
    endRW("end"),
    exitRW("exit"),
    falseRW("false"),
    forRW("for"),
    functionRW("function"),
    ifRW("if"),
    inRW("in"),
    isRW("is"),
    loopRW("loop"),
    modRW("mod"),
    notRW("not"),
    ofRW("of"),
    orRW("or"),
    privateRW("private"),
    procedureRW("procedure"),
    programRW("program"),
    protectedRW("protected"),
    publicRW("public"),
    readRW("read"),
    readlnRW("readln"),
    returnRW("return"),
    thenRW("then"),
    trueRW("true"),
    typeRW("type"),
    varRW("var"),
    whenRW("when"),
    whileRW("while"),
    writeRW("write"),
    writelnRW("writeln"),

    // arithmetic operator symbols
    plus("+"),
    minus("-"),
    times("*"),
    divide("/"),

    // relational operator symbols
    equals("="),
    notEqual("!="),
    lessThan("<"),
    lessOrEqual("<="),
    greaterThan(">"),
    greaterOrEqual(">="),

    // assignment, punctuation, and grouping symbols
    assign(":="),
    leftParen("("),
    rightParen(")"),
    leftBracket("["),
    rightBracket("]"),
    comma(","),
    colon(":"),
    semicolon(";"),
    dot("."),

    // literal values and identifier symbols
    intLiteral("Integer Literal"),
    charLiteral("Character Literal"),
    stringLiteral("String Literal"),
    identifier("Identifier"),

    // special scanning symbols
    EOF("End-of-File"),
    unknown("Unknown");


    // instance fields
    private final String label;


    /**
     * Constructs a new Symbol with its label.
     */
    private Symbol(String label)
      {
        this.label = label;
      }


    public boolean isReservedWord()
      {
        return this.compareTo(BooleanRW) >=0 && this.compareTo(writelnRW) <= 0;
      }


    /**
     * Returns true if this symbol can start an initial declaration.
     */
    public boolean isInitialDeclStarter()
      {
        return this == constRW || this == varRW || this == typeRW;
      }


    /**
     * Returns true if this symbol can start a subprogram declaration.
     */
    public boolean isSubprogramDeclStarter()
      {
        return this == procedureRW || this == functionRW;
      }


    /**
     * Returns true if this symbol can start a statement.
     */
    public boolean isStmtStarter()
      {
        return this == exitRW  || this == identifier || this == ifRW
            || this == loopRW  || this == whileRW    || this == readRW
            || this == writeRW || this == writelnRW  || this == returnRW;
      }


    /**
     * Returns true if this symbol is a logical operator.
     */
    public boolean isLogicalOperator()
      {
        return this == andRW || this == orRW;
      }


    /**
     * Returns true if this symbol is a relational operator.
     */
    public boolean isRelationalOperator()
      {
        return this == equals      || this == notEqual
            || this == lessThan    || this == lessOrEqual
            || this == greaterThan || this == greaterOrEqual;
      }


    /**
     * Returns true if this symbol is an adding operator.
     */
    public boolean isAddingOperator()
      {
        return this == plus || this == minus;
      }


    /**
     * Returns true if this symbol is a multiplying operator.
     */
    public boolean isMultiplyingOperator()
      {
        return this == times || this == divide || this == modRW;
      }


    /**
     * Returns true if this symbol is a literal.
     */
    public boolean isLiteral()
      {
        return this == intLiteral     || this == charLiteral || this == stringLiteral
                    || this == trueRW || this == falseRW;
      }


    /**
     * Returns true if this symbol can start an expression.
     */
    public boolean isExprStarter()
      {
        return isLiteral()         || this == Symbol.identifier || this == Symbol.leftParen
            || this == Symbol.plus || this == Symbol.minus      || this == Symbol.notRW;
      }


    /**
     * Returns the label for this Symbol.
     */
    public String toString()
      {
        return label;
      }
  }
