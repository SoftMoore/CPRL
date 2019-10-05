package edu.citadel.cvm.assembler.optimize;

import edu.citadel.cvm.assembler.Symbol;
import edu.citadel.cvm.assembler.Token;
import edu.citadel.cvm.assembler.ast.Instruction;
import edu.citadel.cvm.assembler.ast.InstructionDEC;
import edu.citadel.cvm.assembler.ast.InstructionINC;

import java.util.List;
import java.util.LinkedList;


/*
 * Replaces addition of 1 to a variable with increment and subtraction
 * of 1 from a variable with decrement.  Basically, this class looks for
 * patterns of the form "LDCINT 1, LDLADDR x, LOADW, ADD" and replaces it
 * with "LDLADDR x, LOADW, INC", and similarly for SUB.
 */
public class IncDec2 implements Optimization
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

        if (OptimizationUtil.isConstAndVar(symbol0, symbol1, symbol2))
          {
            String arg0 = inst0.getArg().getText();

            if (arg0.equals("1"))
              {
                Symbol symbol3 = inst3.getOpCode().getSymbol();

                if (symbol3 == Symbol.ADD)
                  {
                    // replace ADD with INC
                    Token incToken = new Token(Symbol.INC);
                    List<Token> labels = inst3.getLabels();
                    Instruction incInst = new InstructionINC(labels, incToken);
                    instructions.set(instNum + 3, incInst);
                  }
                else if (symbol3 == Symbol.SUB)
                  {
                    // replace SUB with DEC
                    Token decToken = new Token(Symbol.DEC);
                    List<Token> labels = inst3.getLabels();
                    Instruction decInst = new InstructionDEC(labels, decToken);
                    instructions.set(instNum + 3, decInst);
                  }
                else
                    return;

                // copy labels from inst0 to inst1 before removing it
                List<Token> inst0Labels = inst0.getLabels();

                if (inst0Labels != null)
                  {
                    List<Token> inst1Labels = inst1.getLabels();
                    if (inst1Labels == null)
                        inst1Labels = new LinkedList<>();

                    inst1Labels.addAll(inst0Labels);
                  }

                // remove the LDCINT instruction
                instructions.remove(instNum);
              }
          }
      }
  }
