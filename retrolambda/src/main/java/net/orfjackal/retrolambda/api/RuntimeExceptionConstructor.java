// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.api;

import org.objectweb.asm.*;

public class RuntimeExceptionConstructor extends ClassVisitor {

    public RuntimeExceptionConstructor(ClassVisitor next) {
        super(Opcodes.ASM5, next);
    }


    @Override
    public MethodVisitor visitMethod(
            int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor next = super.visitMethod(access, name, desc, signature, exceptions);
        return new MethodVisitor(Opcodes.ASM5, next) {
            @Override
            public void visitMethodInsn(
                    int opcode, String owner, String name, String desc, boolean itf) {
                if (opcode == Opcodes.INVOKESPECIAL
                        && owner.equals("java/lang/RuntimeException")
                        && name.equals("<init>")
                        && desc.equals("(Ljava/lang/String;Ljava/lang/Throwable;ZZ)V")) {

                    // Pop the last two booleans off the stack, they are not supported
                    super.visitInsn(Opcodes.POP2);
                    super.visitMethodInsn(
                            Opcodes.INVOKESPECIAL,
                            "java/lang/RuntimeException",
                            "<init>",
                            "(Ljava/lang/String;Ljava/lang/Throwable;)V",
                            false);
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            }
        };
    }
}
