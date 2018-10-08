// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

class SpreadingHandle extends MethodHandle {
    private final MethodHandle target;
    private final MethodType type;
    private final Class<?> arrayType;
    private final int arrayLength;
    private final int remainingArguments;

    SpreadingHandle(MethodHandle target, Class<?> arrayType, int arrayLength) {
        this.target = target;
        this.remainingArguments = target.type().parameterCount() - arrayLength;
        this.type = target.type();
        this.arrayType = arrayType;
        this.arrayLength = arrayLength;
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        Object[] targetArguments = new Object[target.type().parameterCount()];
        int i;
        for (i = 0; i < remainingArguments; i++) {
            targetArguments[i] = arguments[i];
        }
        Object[] array = (Object[]) targetArguments[i];
        for (int j = 0; j < arrayLength; j++) {
            targetArguments[i++] = array[j];
        }
        return target.invokeWithArguments(targetArguments);
    }

    @Override
    public MethodType type() {
        return type;
    }
}
