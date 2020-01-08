package edu.citadel.cvm.assembler.optimize;


import java.util.List;


import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;
import edu.citadel.cvm.assembler.ast.*;


/**
 * Replaces multiplication by a power of 2 with left shift and
 * division by a power of two with right shift where possible.  
 */
public class ShiftLeftRight implements Optimization
  {
    @Override
    public void optimize(List<Instruction> instructions, int instNum)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size() - 2)
            return;

        Instruction inst0 = instructions.get(instNum);
        Instruction inst1 = instructions.get(instNum + 1);
        
        Symbol symbol0 = inst0.getOpCode().getSymbol();

        // quick check that we have LDCINT
        if (symbol0 != Symbol.LDCINT)
            return;
          
        int shiftAmount = OptimizationUtil.getShiftAmount(((InstructionOneArg) inst0).argToInt());
        if (shiftAmount > 0)
          {
            // make sure that inst1 does not have any labels
            List<Token> inst1Labels = inst1.getLabels();
            if (inst1Labels.isEmpty())
              {
                List<Token> labels = inst0.getLabels();
                String argStr = Integer.toString(shiftAmount);
                Token argToken = new Token(Symbol.intLiteral, argStr);

                Symbol symbol1 = inst1.getOpCode().getSymbol();
                
                if (symbol1 == Symbol.MUL)
                  {
                    // replace LDCINT with SHL 
                    Token shlToken = new Token(Symbol.SHL);
                    Instruction shlInst = new InstructionSHL(labels, shlToken, argToken);
                    instructions.set(instNum, shlInst);
                  }
                else if (symbol1 == Symbol.DIV)
                  {
                    // replace LDCINT by SHR
                    Token shrToken = new Token(Symbol.SHR);
                    Instruction shrInst = new InstructionSHR(labels, shrToken, argToken);
                    instructions.set(instNum, shrInst);
                  }
                else
                    return;

                // remove the MUL/DIV instruction
                instructions.remove(instNum + 1);
              }
          }
      }
  }
