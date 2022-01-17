package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final AbstractIdentifier returnType;
    private final AbstractIdentifier name;
    private final AbstractDeclParam listDeclParam;

    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier name, AbstractDeclParam listDeclParam) {
        Validate.notNull(returnType);
        Validate.notNull(name);
        this.returnType = returnType;
        this.name = name;
        this.listDeclParam = listDeclParam;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superSymbol) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        // label code.nameClass.nameMethod
        // TSTO / BOV stack_overflow
        // sauvegarde des registres
        // code methode (valeur de retour dans R0)
        // label fin.nameClass.nameMethod
        // restauration des registres
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
