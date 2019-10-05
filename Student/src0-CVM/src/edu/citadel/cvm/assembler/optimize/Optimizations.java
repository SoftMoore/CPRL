package edu.citadel.cvm.assembler.optimize;


import java.util.List;
import java.util.LinkedList;


/**
 * This class is used to retrieve the list of all optimizations. 
 */
public class Optimizations
  {
    private static final List<Optimization> optimizations;


    public static List<Optimization> getOptimizations()
      {
        return optimizations;
      }


    static
      {
        optimizations = new LinkedList<>();
        optimizations.add(new ConstFolding());
        optimizations.add(new IncDec());
        optimizations.add(new IncDec2());
        optimizations.add(new ShiftLeftRight());
        optimizations.add(new ShiftLeft());
        optimizations.add(new BranchingReduction());
        optimizations.add(new ConstNeg());
        optimizations.add(new LoadSpecialConstants());
      }
  }
