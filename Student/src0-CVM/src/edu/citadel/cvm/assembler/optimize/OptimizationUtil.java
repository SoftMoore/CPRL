package edu.citadel.cvm.assembler.optimize;


import edu.citadel.cvm.assembler.Symbol;


public class OptimizationUtil
  {
    /**
     * Returns true if both symbols are LDCINT 
     */
    public static boolean isTwoConstInts(Symbol s1, Symbol s2)
      {
        return s1 == Symbol.LDCINT && s2 == Symbol.LDCINT;
      }


    /**
     * Returns true if the three symbols are LDCINT, LDLADDR, and LOADW.  
     */
    public static boolean isConstAndVar(Symbol s0, Symbol s1, Symbol s2)
      {
        return s0 == Symbol.LDCINT && s1 == Symbol.LDLADDR && s2 == Symbol.LOADW;
      }


    /**
     * If n is a power of 2, returns log2(n) (i.e., the exponent);
     * otherwise returns 0. 
     */
    public static byte getShiftAmount(int n)
      {
        byte result = 0;

        while (n > 1)
          {
            if (n % 2 == 1)
                return 0;
            else
              {
                n = n/2;
                ++result;
              }
          }

        return result;
      }
  }
