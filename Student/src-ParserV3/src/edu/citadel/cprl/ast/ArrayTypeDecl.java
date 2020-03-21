package edu.citadel.cprl.ast;


import edu.citadel.compiler.ConstraintException;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.cprl.ArrayType;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;


/**
 * The abstract syntax tree node for an array type declaration.
 */
public class ArrayTypeDecl extends InitialDecl
  {
    private ConstValue numElements;
    private Type       elemType;     // type of elements in the array


    /**
     * Construct an ArrayTypeDecl with its identifier and element type.
     * Note that the index type is always Integer in CPRL.
     */
    public ArrayTypeDecl(Token typeId, Type elemType, ConstValue numElements)
      {
        super(typeId, new ArrayType(typeId.getText(), numElements.getLiteralIntValue(), elemType));
        this.elemType    = elemType;
        this.numElements = numElements;
      }


    /**
     * Returns the number of elements in the array type definition.
     */
    public ConstValue getNumElements()
      {
// ...
      }


    /**
     * Returns the type of the elements in the array.
     */
    public Type getElementType()
      {
 // ...
      }


    @Override
    public void checkConstraints()
      {
// ...
      }
 }
