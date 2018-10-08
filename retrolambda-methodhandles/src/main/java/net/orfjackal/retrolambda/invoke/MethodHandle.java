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

    public double invokeD(Object a1, int a2, int a3, int a4, int a5) throws Throwable {
        return ((Number)invokeWithArguments(a1, a2, a3, a4, a5)).doubleValue();
    }

    public Object invoke() throws Throwable {
        return invokeWithArguments();
    }

    public Object invoke(Object a1) throws Throwable {
        return invokeWithArguments(a1);
    }

    public Object invoke(Object a1, Object a2) throws Throwable {
        return invokeWithArguments(a1, a2);
    }

    public Object invoke(Object a1, Object a2, Object a3) throws Throwable {
        return invokeWithArguments(a1, a2, a3);
    }

    public Object invoke(Object a1, Object a2, Object a3, Object a4) throws Throwable {
        return invokeWithArguments(a1, a2, a3, a4);
    }

    public Object invoke(Object a1, Object a2, Object a3, Object a4, Object a5) throws Throwable {
        return invokeWithArguments(a1, a2, a3, a4, a5);
    }

    public Object invoke(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6) throws Throwable {
        return invokeWithArguments(a1, a2, a3, a4, a5, a6);
    }

    public abstract Object invokeWithArguments(Object... arguments) throws Throwable;

    public abstract MethodType type();

    public MethodHandle asSpreader(Class<?> arrayType, int arrayLength) {
        return new SpreadingHandle(this, arrayType, arrayLength);
    }
}
