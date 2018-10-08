// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.invoke;

import java.util.List;

public class Constants {
    public static MethodHandle of(int tag, String className, String name, String descriptor) throws Throwable {
        // invoke static
        if(tag == 6) {
            Class<?> classDef = Class.forName(className.replace('/', '.'));
            List<Class<?>> classes = BytecodeDescriptor.parseMethod(descriptor, Constants.class.getClassLoader());
            Class[] parameterTypes = new Class[classes.size() - 1];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameterTypes[i] = classes.get(i);
            }
            return new ReflectedStaticMethodHandle(classDef.getMethod(name, parameterTypes));
        } else {
            throw new UnsupportedOperationException("Tag: " + tag);
        }
    }
}
