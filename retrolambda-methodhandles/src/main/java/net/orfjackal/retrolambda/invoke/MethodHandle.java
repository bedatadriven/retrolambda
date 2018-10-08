// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

public abstract class MethodHandle {

    public int invokeI(int x) throws Throwable {
        return ((Number) invoke(x)).intValue();
    }

    public double invokeD() throws Throwable {
        return ((Number)invokeWithArguments()).doubleValue();
    }

    public Object invoke(Object x) throws Throwable {
        return invokeWithArguments(x);
    }

    public Object invoke() throws Throwable {
        return invokeWithArguments();
    }

    public abstract Object invokeWithArguments(Object... arguments) throws Throwable;

    public abstract MethodType type();

    public MethodHandle asSpreader(Class<?> arrayType, int arrayLength) {
        return new SpreadingHandle(this, arrayType, arrayLength);
    }
}
