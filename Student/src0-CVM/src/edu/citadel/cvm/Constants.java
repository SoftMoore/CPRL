package edu.citadel.cvm;


public class Constants
  {
    public static final int BYTES_PER_OPCODE  = 1;

    public static final int BYTES_PER_INTEGER = 4;

    public static final int BYTES_PER_ADDRESS = 4;

    public static final int BYTES_PER_CHAR    = 2;

    public static final int BYTES_PER_BOOLEAN = 1;

    /** frame contains 2 addresses -- return address and dynamic link */
    public static final int BYTES_PER_FRAME   = 2*BYTES_PER_ADDRESS;
  }
