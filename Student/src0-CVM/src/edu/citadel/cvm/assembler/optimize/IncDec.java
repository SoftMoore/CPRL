package edu.citadel.cvm.assembler.optimize;

import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;
import edu.citadel.cvm.assembler.ast.*;

import java.util.List;


/*
 * Replaces addition of 1 with increment and subtraction of 1 with decrement.
 * Basically, this class looks for patterns of the form "LDCINT 1, ADD" and
 * replaces it with "INC", and similarly for SUB.
 */
public class IncDec implements Optimization
  {
    @Override
    public void optimize(List<Instruction> instructions, int instNum)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size() - 2)
            return;

        Instruction inst0 = instructions.get(instNum);
        Instruction inst1 = instructions.get(instNum + 1);

        // quick check that we have LDCINT
        Symbol symbol0 = inst0.getOpCode().getSymbol();
        if (symbol0 != Symbol.LDCINT)
            return;
        
        String arg0 = inst0.getArg().getText();

        if (arg0.equals("1"))
          {
            // Make sure that inst1 does not have any labels
            if (inst1.getLabels().isEmpty())
              {
                Symbol symbol1 = inst1.getOpCode().getSymbol();
                
                if (symbol1 == Symbol.ADD)
                  {
                    // replace LDCINT by INC
                    Token incToken = new Token(Symbol.INC);
                    List<Token> labels = inst0.getLabels();
                    Instruction incInst = new InstructionINC(labels, incToken);
                    instructions.set(instNum, incInst);
                  }
                else if (symbol1 == Symbol.SUB)
                  {
                    // replace LDCINT 1 by DEC
                    Token decToken = new Token(Symbol.DEC);
                    List<Token> labels = inst0.getLabels();
                    Instruction decInst = new InstructionDEC(labels, decToken);
                    instructions.set(instNum, decInst);
                  }
                else
                    return;

                // remove the ADD/SUB instruction
                instructions.remove(instNum + 1);
              }
          }
      }
  }
