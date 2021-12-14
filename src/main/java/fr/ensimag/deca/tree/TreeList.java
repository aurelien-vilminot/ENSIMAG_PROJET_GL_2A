package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;

/**
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public abstract class TreeList<TreeType extends Tree> extends Tree {
    /*
     * We could allow external iterators by adding
     * "implements Iterable<AbstractInst>" but it's cleaner to provide our own
     * iterators, to make sure all callers iterate the same way (Main,
     * IfThenElse, While, ...). If external iteration is needed, use getList().
     */

    private List<TreeType> list = new ArrayList<TreeType>();

    public void add(TreeType i) {
        Validate.notNull(i);
        list.add(i);
    }

    /**
     * @return the list contained in the class, read-only. Use getModifiableList()
     *         if you need to change elements of the list.
     */
    public List<TreeType> getList() {
        return Collections.unmodifiableList(list);
    }

    public TreeType set(int index, TreeType element) {
        return list.set(index, element);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Iterator<TreeType> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }

    /**
     * Do not check anything about the location.
     * 
     * It is possible to use setLocation() on a list, but it is also OK not to
     * set it.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    protected String prettyPrintNode() {
        return super.prettyPrintNode() +
                " [List with " + getList().size() + " elements]";
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        int count = getList().size();
        for (TreeType i : getList()) {
            i.prettyPrint(s, prefix, count == 1, true);
            count--;
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        for (TreeType i : getList()) {
            i.iter(f);
        }
    }

}
