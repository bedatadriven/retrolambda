// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

import java.lang.reflect.*;

public class ConstructorMethodHandle extends MethodHandle {
    private final Constructor constructor;
    private final MethodType methodType;

    public ConstructorMethodHandle(Constructor constructor, MethodType methodType) {
        this.constructor = constructor;
        this.methodType = methodType;
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        return constructor.newInstance(arguments);
    }

    @Override
    public MethodType type() {
        return methodType;
    }
}
