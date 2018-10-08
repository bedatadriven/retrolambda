// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.methodhandle;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class MethodHandleTypeRewriter extends ClassRemapper {

    public static final String PREFIX = "java/lang/invoke/";

    public MethodHandleTypeRewriter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv, new Remapper() {

            @Override
            public String map(String type) {
                if(type.startsWith(PREFIX)) {
                    return "net/orfjackal/retrolambda/invoke/" + type.substring(PREFIX.length());
                } else {
                    return type;
                }
            }
        });
    }

    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor mv) {
        return new MethodRemapper(mv, remapper) {

            @Override
            public void visitLdcInsn(Object cst) {
                if(cst instanceof Handle) {
                    Handle method = (Handle) cst;
                    super.visitLdcInsn(method.getTag());
                    super.visitLdcInsn(method.getOwner());
                    super.visitLdcInsn(method.getName());
                    super.visitLdcInsn(method.getDesc());
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "net/orfjackal/retrolambda/invoke/Constants", "of",
                            "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)" +
                                    "Lnet/orfjackal/retrolambda/invoke/MethodHandle;", false);
                } else {
                    super.visitLdcInsn(cst);
                }
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                if(opcode == Opcodes.INVOKEVIRTUAL &&
                        owner.equals("java/lang/invoke/MethodHandle") &&
                        (name.equals("invokeExact") || name.equals("invoke"))) {

                    Type returnType = Type.getReturnType(desc);
                    Type collapsedReturnType = collapseObjects(returnType);
                    Type argumentTypes[] = Type.getArgumentTypes(desc);

                    // Collapse all array/object types to java.lang.Object
                    for (int i = 0; i < argumentTypes.length; i++) {
                        argumentTypes[i] = collapseObjects(argumentTypes[i]);
                    }

                    if(returnType.getSort() == Type.OBJECT) {
                        name = "invoke";
                    } else {
                        name = "invoke" + collapsedReturnType.getDescriptor();
                    }
                    super.visitMethodInsn(opcode, owner, name,
                            Type.getMethodDescriptor(collapsedReturnType, argumentTypes), false);

                    if(!returnType.equals(collapsedReturnType)) {
                        visitTypeInsn(Opcodes.CHECKCAST, returnType.getInternalName());
                    }
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            }
        };
    }

    private Type collapseObjects(Type type) {
        if(type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
            return Type.getType("Ljava/lang/Object;");
        } else {
            return type;
        }
    }
}
