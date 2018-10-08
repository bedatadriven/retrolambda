// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.test;

import org.junit.Test;

import java.lang.invoke.*;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MethodHandleTest {

    @Test
    public void test() throws Throwable {

        MethodHandle square = MethodHandles.publicLookup().unreflect(MethodHandleTest.class.getMethod("square", int.class));

        assertThat(receiver(square, 4), equalTo(16));
    }

    @Test
    public void testVirtual() throws Throwable {
        MethodHandle toString = MethodHandles.publicLookup()
                .findVirtual(Object.class, "toString", MethodType.methodType(String.class));

        String string = (String) toString.invoke(Integer.valueOf(3));
        assertThat(string, equalTo("3"));

        MethodHandle bound = MethodHandles.insertArguments(toString, 0, Integer.valueOf(42));

        assertThat(bound.invoke(), equalTo("42"));
    }

    public static int square(int x) {
        return x * x;
    }

    private int receiver(MethodHandle methodHandle, int argument) throws Throwable {
        return (int) methodHandle.invokeExact(argument);
    }
}
