package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelGenerator;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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
                    throw new ContextualError("Method prototype must be same as inherited", this.getLocation());
                }

                if (!compiler.getEnvironmentTypes().subTypes(returnType, methodDefinitionSuperEnvExp.getType())) {
                    // Both return types must be the same
                    throw new ContextualError("Return type must be a subtype of inherited method return", this.getLocation());
                }

                // Get index of override method
                indexMethod = methodDefinitionSuperEnvExp.getIndex();

            } else {
                throw new ContextualError("Super-class symbol must be a method definition", this.getLocation());
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
            throw new ContextualError("Method name '" + this.methodName.getName() + "' already declared in the class", this.getLocation());
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
        compiler.addLabel(new Label("code." + methodName.getMethodDefinition().getLabel()));

        // Create a new IMAProgram to be able to add instructions at the beginning of the block
        IMAProgram backupProgram = compiler.getProgram();
        IMAProgram initClassProgram = new IMAProgram();
        compiler.setProgram(initClassProgram);

        LabelGenerator gen = compiler.getLabelGenerator();

        // Declare parameters
        listDeclParam.codeGenDeclMethod(compiler, localEnv);

        // Method body code
        compiler.setMaxUsedRegister(0);
        compiler.setTempStackMax(0);
        compiler.setLocalStackSize(0);
        Label finLabel = new Label("fin." + methodName.getMethodDefinition().getLabel());
        gen.setEndLabel(finLabel);
        methodBody.codeGenDeclMethod(compiler, localEnv);

        // Generate an error if method has a return type and end of method is reached without a return instruction
        if (!returnType.getType().isVoid()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new WSTR("Error: end of method "
                        + methodName.getMethodDefinition().getLabel() + " without return instruction"));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
            }
        }

        // Instructions added at the beginning of the block
        compiler.saveRegisters();
        int v = compiler.getLocalStackSize();
        int d = compiler.getNumberOfRegistersUsed() + compiler.getTempStackMax();
        if (v > 0) {
            compiler.addFirst(new Line(new ADDSP(new ImmediateInteger(v))));
        }
        if (d > 0) {
            compiler.addStackOverflowError(true);
            compiler.addFirst(new Line(new TSTO(new ImmediateInteger(d))));
        }

        // Return
        compiler.addLabel(finLabel);
        compiler.restoreRegisters();
        compiler.addInstruction(new RTS());

        // Restore initial IMAProgram
        backupProgram.append(initClassProgram);
        compiler.setProgram(backupProgram);
        compiler.setMaxUsedRegister(0);
        compiler.setTempStackMax(0);
        compiler.setLocalStackSize(0);

    }

    @Override
    protected void codeGenMethodTable(DecacCompiler compiler, AbstractIdentifier className) {
        Label methodLabel = new Label(className.getName().toString() + "." + methodName.getName().toString());
        methodName.getMethodDefinition().setLabel(methodLabel);
        Label codeLabel = new Label("code." + methodLabel);
        int index = methodName.getMethodDefinition().getIndex() - 1;
        if (index < className.getClassDefinition().getLabelArrayList().size()) {
            // Replace existing parent method in labelArrayList
            className.getClassDefinition().getLabelArrayList().set(index, codeLabel);
        } else {
            // Add method to labelArrayList
            className.getClassDefinition().getLabelArrayList().add(codeLabel);
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
