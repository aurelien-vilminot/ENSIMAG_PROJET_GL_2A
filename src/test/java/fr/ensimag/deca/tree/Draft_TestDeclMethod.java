package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class Draft_TestDeclMethod {
    DecacCompiler compiler;
    DeclMethod declMethod;

    @Mock Type type;
    @Mock AbstractIdentifier returnType, methodName;
    @Mock ListDeclParam listDeclParam;
    @Mock MethodBody methodBody;
    @Mock SymbolTable.Symbol superSymbol, classSymbol;
    @Mock Signature signature;
    @Mock EnvironmentExp envExpName, environmentExpCurrentClass;
    @Mock ClassDefinition currentClassDefinition;

    // @BeforeEach
    void setup() throws ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        declMethod = new DeclMethod(returnType, methodName, listDeclParam, methodBody);

        when(returnType.verifyType(compiler)).thenReturn(type);
        when(listDeclParam.verifyDeclParam(compiler)).thenReturn(signature);
        when(((ClassDefinition) compiler.getEnvironmentTypes().get(superSymbol)).getMembers()).thenReturn(envExpName);
        when(currentClassDefinition.getMembers()).thenReturn(environmentExpCurrentClass);
        when(currentClassDefinition.incNumberOfMethods()).thenReturn(0);

    }

    // @Test
    public void testVerifyDeclMethod(){

    }

    // @Test
    public void testVerifyMethodBody(){

    }

}
