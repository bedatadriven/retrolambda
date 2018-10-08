// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

import java.lang.reflect.Method;

class VirtualHandle extends MethodHandle {

    private final Method method;
    private final int methodParameters;
    private final MethodType type;

    VirtualHandle(Method method, Class<?> receiver, MethodType type) {
        this.method = method;
        this.methodParameters = method.getParameterTypes().length;
        this.type = type.insertParameterTypes(0, receiver);
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        Object instance = arguments[0];
        Object[] methodArguments = new Object[methodParameters];
        System.arraycopy(arguments, 1, methodArguments, 0, methodParameters);

        return method.invoke(instance, methodArguments);
    }

    @Override
    public MethodType type() {
        return type;
    }
}
