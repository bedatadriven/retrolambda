// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

import java.util.*;

class FoldingHandle extends MethodHandle {
    private MethodType methodType;

    public FoldingHandle(MethodHandle target, MethodHandle combiner) {
        if(combiner.type().returnType().equals(void.class)) {
            methodType = target.type();
        } else {
            methodType = target.type().dropParameterTypes(0, 1);
        }
    }

    @Override
    public Object invokeWithArguments(Object... arguments) throws Throwable {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public MethodType type() {
        return methodType;
    }
}
