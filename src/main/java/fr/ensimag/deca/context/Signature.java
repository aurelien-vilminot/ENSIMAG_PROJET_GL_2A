package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl07
 * @date 01/01/2022
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return args.equals(signature.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(args);
    }
}
