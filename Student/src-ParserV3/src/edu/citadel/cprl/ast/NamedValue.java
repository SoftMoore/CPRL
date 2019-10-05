package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;

import java.io.IOException;


/**
 * The abstract syntax tree node for a named value.  A named value is similar
 * to a variable except that it generates different code.  For example, consider
 * the assignment statement "x := y;"  The identifier "x" represents a variable,
 * and the identifier "y" represents a named value.  Code generation for "x"
 * would leave its address on the top of the stack, while code generation for
 * "y" would leave its value on the top of the stack.
 */
public class NamedValue extends Variable
  {
    /**
     * Construct a named value from a variable.
     */
    public NamedValue(Variable var)
      {
        super(var.getDecl(), var.getPosition(), var.getIndexExprs());
      }


    @Override
    public void emit() throws CodeGenException, IOException
      {
        super.emit();             // leaves address of variable on top of stack
        emitLoadInst(getType());  // replaces address by value at that address
      }
  }
