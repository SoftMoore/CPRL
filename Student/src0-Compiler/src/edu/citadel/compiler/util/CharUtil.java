package edu.citadel.compiler.util;


public class CharUtil
  {
    /**
     * Returns true only if the specified character is a binary digit
     * ('0' or '1').
     */
    public static boolean isBinaryDigit(char ch)
      {
        return ch == '0' || ch == '1';
      }


    /**
     * Returns true only if the specified character is a hex digit
     * ('0'-'9', 'A'..'F', or 'a'..'f').
     */
    public static boolean isHexDigit(char ch)
      {
        if (Character.isDigit(ch))
            return true;
        else
          {
            ch = Character.toUpperCase(ch);
            return ch >= 'A' && ch <= 'F';
          }
      }
  }
