package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final AbstractIdentifier returnType;
    private final AbstractIdentifier methodName;
    private final ListDeclParam listDeclParam;
    private final AbstractMethodBody methodBody;
    private EnvironmentExp localEnv;

    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam listDeclParam, AbstractMethodBody methodBody) {
        Validate.notNull(returnType);
        Validate.notNull(methodName);
        Validate.notNull(methodBody);
        this.returnType = returnType;
        this.methodName = methodName;
        this.listDeclParam = listDeclParam;
        this.methodBody = methodBody;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol classSymbol) throws ContextualError {
        LOG.debug("verify DeclMethod: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        Type returnType = this.returnType.verifyType(compiler);
        Signature signature = this.listDeclParam.verifyDeclParam(compiler);

        EnvironmentExp envExpName = ((ClassDefinition) compiler.getEnvironmentTypes().get(superSymbol)).getMembers();
        ClassDefinition currentClassDefinition = (ClassDefinition) compiler.getEnvironmentTypes().get(classSymbol);
        EnvironmentExp environmentExpCurrentClass = currentClassDefinition.getMembers();

        // Default index
        int indexMethod = currentClassDefinition.incNumberOfMethods();

        if (envExpName.get(this.methodName.getName()) != null) {
            if (envExpName.get(this.methodName.getName()).isMethod()) {
                // If there is an override method
                MethodDefinition methodDefinitionSuperEnvExp = envExpName.get(this.methodName.getName()).asMethodDefinition(
                        "Impossible to convert in methodDefinition",
                        this.getLocation()
                );

                if (!methodDefinitionSuperEnvExp.getSignature().equals(signature)) {
                    // Both signatures must be the same
                    throw new ContextualError("Method prototype must be same as herited", this.getLocation());
                }

                if (!compiler.getEnvironmentTypes().subTypes(returnType, methodDefinitionSuperEnvExp.getType())) {
                    // Both return types must be the same
                    throw new ContextualError("Return type must be a subtype of hertied method return", this.getLocation());
                }

                // Get index of override method
                indexMethod = methodDefinitionSuperEnvExp.getIndex();

            } else {
                throw new ContextualError("Super class symbol must be a method definition", this.getLocation());
            }
        }

        // Method declaration
        try {
            environmentExpCurrentClass.declare(
                    this.methodName.getName(),
                    new MethodDefinition(
                            returnType,
                            this.getLocation(),
                            signature,
                            indexMethod
                    )
                    );
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Method name already declared in the class", this.getLocation());
        }

        this.methodName.verifyExpr(compiler, environmentExpCurrentClass, currentClassDefinition);
        LOG.debug("verify DeclMethod: end");
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, SymbolTable.Symbol classSymbol) throws ContextualError {
        LOG.debug("verify MethodBody: start");

        // Get params for verify method
        EnvironmentExp environmentExpCurrentClass = ((ClassDefinition) compiler.getEnvironmentTypes().get(classSymbol)).getMembers();
        localEnv = this.listDeclParam.verifyParamEnvExp(compiler, environmentExpCurrentClass);
        ClassDefinition methodClassDefinition = (ClassDefinition) compiler.getEnvironmentTypes().get(classSymbol);

        this.methodBody.verifyMethodBody(compiler, localEnv, methodClassDefinition, this.returnType.getType());

        LOG.debug("verify MethodBody: end");
    }


    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        // label code.nameClass.nameMethod
        compiler.addLabel(methodName.getMethodDefinition().getLabel());
        // TODO: TSTO / BOV stack_overflow
        compiler.saveRegisters();
        listDeclParam.codeGenDeclMethod(compiler, localEnv);
        // code methode (valeur de retour dans R0)
        methodBody.codeGenDeclMethod(compiler, localEnv);
        Label fin = new Label("fin." + methodName.getMethodDefinition().getLabel().toString().substring(4));
        compiler.addLabel(fin);
        compiler.restoreRegisters();
        compiler.addInstruction(new RTS());
    }

    @Override
    protected void codeGenMethodTable(DecacCompiler compiler, AbstractIdentifier className) {
        Label methodLabel = new Label("code." + className.getName().toString() + "." + methodName.getName().toString());
        methodName.getMethodDefinition().setLabel(methodLabel);
        int index = methodName.getMethodDefinition().getIndex() - 1;
        if (index < className.getClassDefinition().getLabelArrayList().size()) {
            className.getClassDefinition().getLabelArrayList().set(index, methodLabel);
        } else {
            className.getClassDefinition().getLabelArrayList().add(methodLabel);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        returnType.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        listDeclParam.decompile(s);
        s.print(")");
        methodBody.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listDeclParam.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnType.iter(f);
        methodName.iter(f);
        listDeclParam.iter(f);
        methodBody.iter(f);
    }
}
