package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public void codeGenListDeclVar(DecacCompiler compiler) {
        for (AbstractDeclVar d : getList()) {
            d.codeGenDeclVar(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (getList().isEmpty()) {
            return;
        }
        for (AbstractDeclVar v : getList()) {
            v.decompile(s);
            s.println();
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to
     *      the "env_exp_r" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify ListDeclVar: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        for (AbstractDeclVar abstractDeclVar: getList()) {
            abstractDeclVar.verifyDeclVar(compiler, localEnv, currentClass);
        }

        LOG.debug("verify ListDeclVar: end");

    }


}
