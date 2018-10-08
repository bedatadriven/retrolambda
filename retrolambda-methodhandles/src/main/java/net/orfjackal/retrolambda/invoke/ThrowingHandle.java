// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

public class ThrowingHandle extends MethodHandle {
    private final MethodType methodType;

    public ThrowingHandle(MethodType methodType) {
        this.methodType = methodType;
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        Throwable exception = (Throwable) arguments[0];
        throw exception;
    }

    @Override
    public MethodType type() {
        return methodType;
    }
}
