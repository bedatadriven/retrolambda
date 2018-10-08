// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

import java.lang.invoke.*;
import java.lang.reflect.*;

public class MethodHandles {


    public static final class Lookup {

        public static final Lookup PUBLIC_LOOKUP = new Lookup();

        public MethodHandle unreflect(Method method) {
            if (Modifier.isStatic(method.getModifiers())) {
                return new ReflectedStaticMethodHandle(method);
            } else {
                throw new UnsupportedOperationException("TODO: Instance methods");
            }
        }

        public MethodHandle findConstructor(Class clazz, MethodType methodType) throws Throwable {
            Constructor constructor = clazz.getConstructor(methodType.ptypes());
            return new ConstructorMethodHandle(constructor, methodType);
        }

        public MethodHandle findVirtual(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
            Method method = refc.getMethod(name, type.ptypes());
            if(Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException(method + " is static");
            }
            return new VirtualHandle(method, refc, type);
        }
    }

    public static Lookup publicLookup() {
        return Lookup.PUBLIC_LOOKUP;
    }

    public static MethodHandle insertArguments(MethodHandle target, int pos, Object... values) {
        return new BoundHandle(target, pos, values);
    }

    /**
     * Produces a method handle which will throw exceptions of the given {@code exType}.
     * The method handle will accept a single argument of {@code exType},
     * and immediately throw it as an exception.
     * The method type will nominally specify a return of {@code returnType}.
     * The return type may be anything convenient:  It doesn't matter to the
     * method handle's behavior, since it will never return normally.
     * @param returnType the return type of the desired method handle
     * @param exType the parameter type of the desired method handle
     * @return method handle which can throw the given exceptions
     * @throws NullPointerException if either argument is null
     */
    public static MethodHandle throwException(Class<?> returnType, Class<? extends Throwable> exType) {
        if (!Throwable.class.isAssignableFrom(exType)) {
            throw new ClassCastException(exType.getName());
        }
        return new ThrowingHandle(MethodType.methodType(returnType, exType));
    }

    public static MethodHandle foldArguments(MethodHandle target, MethodHandle combiner) {
        return new FoldingHandle(target, combiner);
    }

}