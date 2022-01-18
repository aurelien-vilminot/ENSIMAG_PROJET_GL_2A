package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass abstractDeclClass : this.getList()) {
            abstractDeclClass.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassMembers: start");
        for (AbstractDeclClass abstractDeclClass : this.getList()) {
            abstractDeclClass.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass abstractDeclClass : this.getList()) {
            abstractDeclClass.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    /**
     * Pass 1 of [CodeGen]
     * @param compiler
     */
    protected void codeGenMethodTable(DecacCompiler compiler) {
        if (!getList().isEmpty()) {
            // Generate Object.equals
            int index = compiler.incGlobalStackSize(1);
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
            Label equalsLabel = null;
            try {
                equalsLabel = compiler.getEnvironmentTypes()
                        .get(compiler.getSymbolTable().create("equals"))
                        .asMethodDefinition("Impossible to convert in method definition", this.getLocation())
                        .getLabel();
            } catch (ContextualError contextualError) {
                contextualError.printStackTrace();
            }
            index = compiler.incGlobalStackSize(1);
            compiler.addInstruction(new LOAD(new LabelOperand(equalsLabel), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
        }
        for (AbstractDeclClass c : getList()) {
            c.codeGenMethodTable(compiler);
        }
    }

    /**
     * Pass 2 of [CodeGen]
     * @param compiler
     */
    protected void codeGenListDeclClass(DecacCompiler compiler) {
        for (AbstractDeclClass c : getList()) {
            c.codeGenDeclClass(compiler);
        }
    }

}
