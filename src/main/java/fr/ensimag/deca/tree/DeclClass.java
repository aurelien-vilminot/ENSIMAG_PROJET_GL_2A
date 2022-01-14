package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final AbstractIdentifier name;
    private final AbstractIdentifier superClass;
    private ListDeclField listDeclField;
    private ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier name,
                     AbstractIdentifier superClass,
                     ListDeclField listDeclField,
                     ListDeclMethod listDeclMethod) {
        Validate.notNull(name);
        Validate.notNull(superClass);
        Validate.notNull(listDeclField);
        Validate.notNull(listDeclMethod);
        this.name = name;
        this.superClass = superClass;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify DeclClass: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        // Check type of super-class identifier
        TypeDefinition superClassType = compiler.getEnvironmentTypes().get(this.superClass.getName());
        if (!superClassType.isClass()) {
            throw new ContextualError("Expected class identifier", this.getLocation());
        }

        // Add new class if not already existed
        try {
            compiler.getEnvironmentTypes().declare(
                    this.name.getName(),
                    new TypeDefinition(
                            new ClassType(
                                    this.name.getName(),
                                    this.getLocation(),
                                    this.superClass.getClassDefinition()
                            ),
                            this.getLocation()
                    )
            );
        } catch (EnvironmentTypes.DoubleDefException e) {
            throw new ContextualError("Already class identifier declared", this.getLocation());
        }
        LOG.debug("verify DeclClass: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
