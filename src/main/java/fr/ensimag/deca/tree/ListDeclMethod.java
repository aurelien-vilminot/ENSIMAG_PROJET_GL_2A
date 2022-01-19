package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    protected void verifyListDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol classSymbol)
            throws ContextualError {
        LOG.debug("verify listDeclMethod: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(superSymbol, "Symbol of super class should not be null");

        for (AbstractDeclMethod abstractDeclMethod : this.getList()) {
            abstractDeclMethod.verifyDeclMethod(compiler, superSymbol, classSymbol);
        }
        LOG.debug("verify listDeclMethod: end");
    }

    protected void verifyListMethodBody(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol classSymbol)
            throws ContextualError {
        LOG.debug("verify listMethodBody: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(superSymbol, "Symbol of super class should not be null");

        for (AbstractDeclMethod abstractDeclMethod : this.getList()) {
            abstractDeclMethod.verifyMethodBody(compiler, classSymbol);
        }
        LOG.debug("verify listMethodBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
        }
    }

    protected void codeGenListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod m : getList()) {
            m.codeGenDeclMethod(compiler);
        }
    }

    protected void codeGenMethodTable(DecacCompiler compiler, AbstractIdentifier className, AbstractIdentifier superClass) {
        // Construct labelArrayList from parent
        className.getClassDefinition().getLabelArrayList().addAll(superClass.getClassDefinition().getLabelArrayList());

        // Generate parent class methods
        for (Label l : className.getClassDefinition().getLabelArrayList()) {
            int addr = compiler.incGlobalStackSize(1);
            compiler.addInstruction(new LOAD(new LabelOperand(l), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(addr, Register.GB)));
        }

        // Generate current class methods
        for (AbstractDeclMethod m : getList()) {
            m.codeGenMethodTable(compiler, className);
        }
    }
}
