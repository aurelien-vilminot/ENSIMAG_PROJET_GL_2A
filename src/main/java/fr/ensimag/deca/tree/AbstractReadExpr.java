package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import org.apache.commons.lang.Validate;

/**
 * read...() statement.
 *
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        Validate.isTrue((n <= compiler.getCompilerOptions().getRegisterNumber() - 1));

        if (this.getType().isInt()) {
            // R1 <- readInt()
            compiler.addInstruction(new RINT());
        } else if (this.getType().isFloat()){
            // R1 <- readFloat()
            compiler.addInstruction(new RFLOAT());
        }
        compiler.addIoError();
        // Rn <- V[R1]
        compiler.addInstruction(new LOAD(Register.getR(1), Register.getR(n)));
    }

}
