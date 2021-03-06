package org.jruby.compiler.ir.instructions;

import org.jruby.compiler.ir.Operation;
import org.jruby.compiler.ir.operands.GlobalVariable;
import org.jruby.compiler.ir.operands.Operand;
import org.jruby.compiler.ir.representations.InlinerInfo;

public class PUT_GLOBAL_VAR_Instr extends PUT_Instr
{
    public PUT_GLOBAL_VAR_Instr(String varName, Operand value) {
        super(Operation.PUT_GLOBAL_VAR, new GlobalVariable(varName), null, value);
    }

    public IR_Instr cloneForInlining(InlinerInfo ii) {
        return new PUT_GLOBAL_VAR_Instr(((GlobalVariable)_target).name, _value.cloneForInlining(ii));
    }
}
