package edu.citadel.cprl.ast;


import edu.citadel.compiler.CodeGenException;
import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.Position;
import edu.citadel.cprl.Type;

import java.io.*;


/**
 * Base class for all abstract syntax tree classes.
 */
public abstract class AST
  {
    // Number of spaces to print before opcode
    private static final String SPACES = "   ";

    private static PrintWriter out = null;

    // current label number for control flow
    private static int currentLabelNum = -1;


    /**
     * Set the PrintWriter to be used for code generation.
     */
    public static void setPrintWriter(PrintWriter out)
      {
        AST.out = out;
      }


    /**
     * Creates/returns a new constraint exception with the specified position and message.
     */
    protected ConstraintException error(Position errorPos, String errorMsg)
      {
        return new ConstraintException(errorPos, errorMsg);
      }


    /**
     * Check semantic/contextual constraints.
     */
    public abstract void checkConstraints();


    /**
     * Emit the object code for the AST.
     *
     * @throws IOException if there is a problem writing to the target file.
     * @throws CodeGenException if the method is unable to generate appropriate target code.
     */
    public abstract void emit() throws CodeGenException, IOException;


    /**
     * Returns a new value for a label number.  This method should
     * be called once for each label before code generation.
     */
    protected String getNewLabel()
      {
        ++currentLabelNum;
        return "L" + currentLabelNum;
      }


    /**
     * Returns true if the two types are assignment compatible.
     * (Handles special compiler internal types)
     */
    protected boolean matchTypes(Type t1, Type t2)
      {
        return t1 == t2;
      }


    /**
     * Emits the appropriate LOAD instruction based on the type.
     */
    protected void emitLoadInst(Type t) throws IOException
      {
        int numBytes = t.getSize();

        if (numBytes == 4)
            emit("LOADW");
        else if (numBytes == 2)
            emit("LOAD2B");
        else if (numBytes == 1)
            emit("LOADB");
        else
            emit("LOAD " + numBytes);
      }


    /**
     * Emits the appropriate STORE instruction based on the type.
     */
    protected void emitStoreInst(Type t) throws IOException
      {
        int numBytes = t.getSize();

        if (numBytes == 4)
            emit("STOREW");
        else if (numBytes == 2)
            emit("STORE2B");
        else if (numBytes == 1)
            emit("STOREB");
        else
            emit("STORE " + numBytes);
      }


    /**
     * Emit label for assembly instruction.  This instruction appends a colon
     * to the end of the label and writes out the result on a single line.
     */
    protected void emitLabel(String label)
      {
        out.println(label + ":");
      }


    /**
     * Emit string representation for an assembly instruction.
     */
    protected void emit(String instruction)
      {
        out.println(SPACES + instruction);
      }
  }
