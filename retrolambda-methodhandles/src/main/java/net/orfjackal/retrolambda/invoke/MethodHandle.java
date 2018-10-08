// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

public abstract class MethodHandle {

    public abstract Object invokeWithArguments(Object... arguments) throws Throwable;

    public abstract MethodType type();

    public MethodHandle asSpreader(Class<?> arrayType, int arrayLength) {
        return new SpreadingHandle(this, arrayType, arrayLength);
    }
}
