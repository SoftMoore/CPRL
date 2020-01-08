package edu.citadel.cvm.assembler.optimize;


import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;
import edu.citadel.cvm.assembler.ast.*;

import java.util.List;


/*
 * Replaces multiplication by a power of 2 times a variable with left
 * shift.  Basically, this class looks for patterns of the form
 * "LDCINT 2**n, LDLADDR x, LOADW, MUL" and replaces it with
 * "LDLADDR x, LOADW, SHL n".  Note that the analogous replacement
 * for division will not work since division is not commutative.
 */
public class ShiftLeft implements Optimization
  {
    @Override
    public void optimize(List<Instruction> instructions, int instNum)
      {
        // quick check that there are at least four instructions remaining
        if (instNum > instructions.size() - 4)
            return;

        Instruction inst0 = instructions.get(instNum);
        Instruction inst1 = instructions.get(instNum + 1);
        Instruction inst2 = instructions.get(instNum + 2);
        Instruction inst3 = instructions.get(instNum + 3);
        
        // quick check that we are dealing with a constant and a variable
        Symbol symbol0 = inst0.getOpCode().getSymbol();
        Symbol symbol1 = inst1.getOpCode().getSymbol();
        Symbol symbol2 = inst2.getOpCode().getSymbol();

        // quick check that we have LDCINT, LDLADDR, and LOADW
        if (symbol0 != Symbol.LDCINT || symbol1 != Symbol.LDLADDR || symbol2 != Symbol.LOADW)
              return;

        String arg0 = inst0.getArg().getText();
        int shiftAmount = OptimizationUtil.getShiftAmount(Integer.parseInt(arg0));

        if (shiftAmount > 0)
          {
            Symbol symbol3 = inst3.getOpCode().getSymbol();

            if (symbol3 == Symbol.MUL)
              {
                // replace MUL by SHL
                Token shlToken = new Token(Symbol.SHL);
                List<Token> labels = inst3.getLabels();
                String argStr = Integer.toString(shiftAmount);
                Token argToken = new Token(Symbol.intLiteral, argStr);
                Instruction shlInst = new InstructionSHL(labels, shlToken, argToken);
                instructions.set(instNum + 3, shlInst);
              }
            else
                return;

            // copy labels from inst0 to inst1 before removing it
            List<Token> inst1Labels = inst1.getLabels();
            inst1Labels.addAll(inst0.getLabels());
    
            // remove the LDCINT instruction
            instructions.remove(instNum);
          }
      }
  }
