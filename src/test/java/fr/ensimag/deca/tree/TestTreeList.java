package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTreeList {
    
    private ListExpr treeList;

    @BeforeEach
    void setup() {
        treeList = new ListExpr();
    }
    
    @Test
    public void testSet(){
        int numElements = 3;
        for (int i = 0; i < numElements; i++) { 
            treeList.add(new IntLiteral(i));
        }

        IntLiteral myElement = new IntLiteral(42);
        treeList.set(1, myElement);

        Iterator<AbstractExpr> iterator = treeList.iterator();
        // pass element at index 0
        iterator.next();
        // test with element at index 1
        assertEquals(myElement, iterator.next());
    }
    
    @Test
    public void testIsEmptyTrue(){
        assertTrue(treeList.isEmpty());
    }
    
    @Test
    public void testIsEmptyFalse(){
        treeList.add(new IntLiteral(42));
        assertFalse(treeList.isEmpty());
    }
    
    @Test
    public void testIterator(){
        int numElements = 3;
        IntLiteral[] intLits = new IntLiteral[numElements];
        for (int i = 0; i < numElements; i++) { 
            intLits[i] = new IntLiteral(i);
            treeList.add(intLits[i]);
        }

        Iterator<AbstractExpr> iterator = treeList.iterator();

        int i = 0;
        while(iterator.hasNext()) {
            assertEquals(intLits[i],iterator.next());
            i++;
        }
    }
    
    @Test
    public void testSize(){
        int numElements = 3;
        for (int i = 0; i < numElements; i++) { 
            treeList.add(new IntLiteral(i));
        }
        
        assertEquals(numElements, treeList.size());
    }
}
