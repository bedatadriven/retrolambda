// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

import java.lang.reflect.Method;

public class ReflectedStaticMethodHandle extends MethodHandle {

    private Method method;
    private MethodType methodType;

    public ReflectedStaticMethodHandle(Method method) {
        this.method = method;
        this.methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        return method.invoke(null, arguments);
    }

    @Override
    public MethodType type() {
        return methodType;
    }
}
