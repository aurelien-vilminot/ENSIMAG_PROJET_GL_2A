package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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
     * Pass 1 of [Gencode]
     *
     * @param compiler Deca Compiler used to add IMA instructions
     */
    protected void codeGenMethodTable(DecacCompiler compiler) {
        if (!getList().isEmpty()) {
            compiler.addComment("Virtual methods table");
            // Generate Object.equals
            int index = compiler.incGlobalStackSize(1);
            DAddr dAddr = new RegisterOffset(index, Register.GB);

            Label equalsLabel = new Label("code.Object.equals");
            ClassDefinition objectDefinition = ((ClassDefinition)(compiler.getEnvironmentTypes()
                    .get(compiler.getSymbolTable().create("Object"))));
            objectDefinition.setOperand(dAddr);
            objectDefinition.getLabelArrayList().add(equalsLabel);
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, dAddr));
            index = compiler.incGlobalStackSize(1);
            compiler.addInstruction(new LOAD(new LabelOperand(equalsLabel), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.GB)));
        }
        for (AbstractDeclClass c : getList()) {
            c.codeGenMethodTable(compiler);
        }
    }

    /**
     * Pass 2 of [Gencode]
     *
     * @param compiler Deca Compiler used to add IMA instructions
     */
    protected void codeGenListDeclClass(DecacCompiler compiler) {
        if (!getList().isEmpty()) {
            compiler.addComment("Methods table");
            // Generate code.Object.equals
            codeGenObjectEquals(compiler);

        }
        for (AbstractDeclClass c : getList()) {
            c.codeGenDeclClass(compiler);
        }
    }

    /**
     * Generate assembly code for the Object.equals method
     *
     * @param compiler Deca Compiler used to add IMA instructions
     */
    protected void codeGenObjectEquals(DecacCompiler compiler) {
        Label equalsLabel = new Label("code.Object.equals");
        Label endEqualsLabel = new Label("fin.Object.equals");
        compiler.addLabel(equalsLabel);

        Label trueBranch = new Label(compiler.getLabelGenerator().generateLabel("boolIsTrue"));
        Label continueBranch = new Label(compiler.getLabelGenerator().generateLabel("continue"));

        // Generate code that sends to "branch" if objects are equal
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), Register.R0));
        compiler.addInstruction(new CMP(new RegisterOffset(-4, Register.LB), Register.R0));
        compiler.addInstruction(new BEQ(trueBranch));

        // If "this" is not evaluated to "bool", load !bool and jump to continue
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.getR(0)));
        compiler.addInstruction(new BRA(continueBranch));
        // If "this" is evaluated to "bool", load bool
        compiler.addLabel(trueBranch);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.getR(0)));
        // Generate label continue
        compiler.addLabel(continueBranch);

        // End method
        compiler.addInstruction(new RTS());
        compiler.addLabel(endEqualsLabel);
        compiler.addInstruction(new RTS());
    }

}
