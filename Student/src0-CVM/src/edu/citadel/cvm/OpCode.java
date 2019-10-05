package edu.citadel.cvm;


/**
 * The set of opcodes for the CPRL virtual machine
 */
public final class OpCode
  {
    // halt opcode
    public static final byte HALT    =  0;

    // load opcodes (move data from memory to top of stack)
    public static final byte LOAD    = 10;
    public static final byte LOADB   = 11;
    public static final byte LOAD2B  = 12;
    public static final byte LOADW   = 13;
    public static final byte LDCB    = 14;
    public static final byte LDCCH   = 15;
    public static final byte LDCINT  = 16;
    public static final byte LDCSTR  = 17;
    public static final byte LDLADDR = 18;
    public static final byte LDGADDR = 19;
    
    // optimized loads for special constants
    public static final byte LDCB0   = 20;
    public static final byte LDCB1   = 21;
    public static final byte LDCINT0 = 22;
    public static final byte LDCINT1 = 23;

    // store opcodes (move data from top of stack to memory)
    public static final byte STORE   = 30;
    public static final byte STOREB  = 31;
    public static final byte STORE2B = 32;
    public static final byte STOREW  = 33;

    // compare/branch opcodes
    public static final byte CMP     = 40;
    public static final byte BR      = 41;
    public static final byte BNZ     = 42;
    public static final byte BZ      = 43;
    public static final byte BG      = 44;
    public static final byte BGE     = 45;
    public static final byte BL      = 46;
    public static final byte BLE     = 47;

    // shift opcodes
    public static final byte SHL     = 50;
    public static final byte SHR     = 51;

    // logical not opcode
    public static final byte NOT     = 60;

    // arithmetic opcodes
    public static final byte ADD     = 70;
    public static final byte SUB     = 71;
    public static final byte MUL     = 72;
    public static final byte DIV     = 73;
    public static final byte MOD     = 74;
    public static final byte NEG     = 75;
    public static final byte INC     = 76;
    public static final byte DEC     = 77;

    // I/O opcodes
    public static final byte GETCH   = 80;
    public static final byte GETINT  = 81;
    public static final byte PUTBYTE = 82;
    public static final byte PUTCH   = 83;
    public static final byte PUTINT  = 84;
    public static final byte PUTEOL  = 85;
    public static final byte PUTSTR  = 86;

    // program/procedure opcodes
    public static final byte PROGRAM = 90;
    public static final byte PROC    = 91;
    public static final byte CALL    = 92;
    public static final byte RET     = 93;
    public static final byte ALLOC   = 94;


    /**
     * Returns a string representation for an opcode.  Returns Byte.toString(n) if
     * the argument does not have a value equal to any of the declared opcodes.
     */
    public static String toString(byte n)
      {
        switch(n)
          {
            case HALT:
                return "HALT";
            case LOAD:
                return "LOAD";
            case LOADB:
                return "LOADB";
            case LOAD2B:
                return "LOAD2B";
            case LOADW:
                return "LOADW";
            case LDCB:
                return "LDCB";
            case LDCCH:
                return "LDCCH";
            case LDCINT:
                return "LDCINT";
            case LDCSTR:
                return "LDCSTR";
            case LDLADDR:
                return "LDLADDR";
            case LDGADDR:
                return "LDGADDR";
            case LDCB0:
                return "LDCB0";
            case LDCB1:
                return "LDCB1";
            case LDCINT0:
                return "LDCINT0";
            case LDCINT1:
                return "LDCINT1";
            case STORE:
                return "STORE";
            case STOREB:
                return "STOREB";
            case STORE2B:
                return "STORE2B";
            case STOREW:
                return "STOREW";
            case CMP:
                return "CMP";
            case BR:
                return "BR";
            case BNZ:
                return "BNZ";
            case BZ:
                return "BZ";
            case BG:
                return "BG";
            case BGE:
                return "BGE";
            case BL:
                return "BL";
            case BLE:
                return "BLE";
            case SHL:
                return "SHL";
            case SHR:
                return "SHR";
            case NOT:
                return "NOT";
            case ADD:
                return "ADD";
            case SUB:
                return "SUB";
            case MUL:
                return "MUL";
            case DIV:
                return "DIV";
            case MOD:
                return "MOD";
            case NEG:
                return "NEG";
            case INC:
                return "INC";
            case DEC:
                return "DEC";
            case GETCH:
                return "GETCH";
            case GETINT:
                return "GETINT";
            case PUTBYTE:
                return "PUTBYTE";
            case PUTCH:
                return "PUTCH";
            case PUTINT:
                return "PUTINT";
            case PUTEOL:
                return "PUTEOL";
            case PUTSTR:
                return "PUTSTR";
            case CALL:
                return "CALL";
            case PROC:
                return "PROC";
            case PROGRAM:
                return "PROGRAM";
            case RET:
                return "RET";
            case ALLOC:
                return "ALLOC";
            default:
                return Byte.toString(n);
          }
      }
  }
