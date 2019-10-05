package edu.citadel.compiler.util;


/**
 * Methods to convert integers and characters to byte representations, and vice versa.
 */
public class ByteUtil
  {
    /**
     * Converts 2 bytes to a char.  The bytes passed as arguments are
     * ordered with b0 as the high order byte and b1 as the low order byte.
     */
    public static char bytesToChar(byte b0, byte b1)
      {
        return (char) ( (((int)b0 << 8) & 0x0000FF00)
                      |  ((int)b1       & 0x000000FF));
      }


    /**
     * Converts 4 bytes to an int.  The bytes passed as arguments are
     * ordered with b0 as the high order byte and b3 as the low order byte.
     */
    public static int bytesToInt(byte b0, byte b1, byte b2, byte b3)
      {
        return (((int)b0 << 24) & 0xFF000000)
            |  (((int)b1 << 16) & 0x00FF0000)
            |  (((int)b2 << 8)  & 0x0000FF00)
            |   ((int)b3        & 0x000000FF);
      }


    /**
     * Converts a char to an array of 2 bytes.  The bytes in the return
     * array are ordered with the one at index 0 as the high order byte
     * and the one at index 1 as the low order byte.
     */
    public static byte[] charToBytes(char c)
      {
        byte[] result = new byte[2];

        result[0] = (byte) ((c >>> 8) & 0x00FF);
        result[1] = (byte) ((c >>> 0) & 0x00FF);

        return result;
      }


    /**
     * Converts a short to an array of 2 bytes.  The bytes in the return
     * array are ordered with the one at index 0 as the high order byte
     * and the one at index 1 as the low order byte.
     */
    public static byte[] shortToBytes(short n)
      {
        byte[] result = new byte[2];

        result[0] = (byte) ((n >>> 8) & 0x00FF);
        result[1] = (byte) ((n >>> 0) & 0x00FF);

        return result;
      }


    /**
     * Converts an int to an array of 4 bytes.  The bytes in the return
     * array are ordered with the one at index 0 as the high order byte
     * and the one at index 3 as the low order byte.
     */
    public static byte[] intToBytes(int n)
      {
        byte[] result = new byte[4];

        result[0] = (byte) ((n >>> 24) & 0x000000FF);
        result[1] = (byte) ((n >>> 16) & 0x000000FF);
        result[2] = (byte) ((n >>>  8) & 0x000000FF);
        result[3] = (byte) ((n >>>  0) & 0x000000FF);

        return result;
      }
  }
