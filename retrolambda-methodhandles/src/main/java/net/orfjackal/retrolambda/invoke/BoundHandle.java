// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

public class BoundHandle extends MethodHandle {


    private final MethodHandle target;
    private final MethodType type;
    private final int pos;
    private final Object[] boundValues;

    public BoundHandle(MethodHandle target, int pos, Object[] boundValues) {
        this.target = target;
        this.pos = pos;
        this.boundValues = boundValues;
        this.type = target.type().dropParameterTypes(pos, boundValues.length);
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        Object[] targetArguments = new Object[target.type().parameterCount()];
        int sourceArgumentIndex = 0;
        int targetArgumentIndex = 0;
        while(targetArgumentIndex < targetArguments.length) {
            if(targetArgumentIndex == pos) {
                for (Object boundValue : boundValues) {
                    targetArguments[targetArgumentIndex++] = boundValue;
                }
            } else {
                targetArguments[targetArgumentIndex++] = arguments[sourceArgumentIndex++];
            }
        }
        return target.invokeWithArguments(targetArguments);
    }

    @Override
    public MethodType type() {
        return type;
    }
}
