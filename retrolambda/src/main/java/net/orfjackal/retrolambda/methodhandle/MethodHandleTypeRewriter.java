// Copyright © 2013-2018 Esko Luontola and other Retrolambda contributors
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.methodhandle;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

import java.util.*;

public class MethodHandleTypeRewriter extends ClassRemapper {

    public static final String PREFIX = "java/lang/invoke/";

    private Set<String> invokerDescriptors = new HashSet<>();

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
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

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
                    Type handleArguments[] = Type.getArgumentTypes(desc);

                    // Static method call: handle + arguments
                    Type wrapperArgumentTypes[] = new Type[handleArguments.length + 1];

                    wrapperArgumentTypes[0] = Type.getType("Lnet/orfjackal/retrolambda/invoke/MethodHandle;");
                    for (int i = 0; i < handleArguments.length; i++) {
                        wrapperArgumentTypes[i + 1] = collapseObjects(handleArguments[i]);
                    }

                    String invokerDescriptor = Type.getMethodDescriptor(collapsedReturnType, wrapperArgumentTypes);
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, className,
                            invokerName(returnType), invokerDescriptor, false);

                    invokerDescriptors.add(invokerDescriptor);

                    if(!returnType.equals(collapsedReturnType)) {
                        visitTypeInsn(Opcodes.CHECKCAST, returnType.getInternalName());
                    }
                } else {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            }
        };
    }

    @Override
    public void visitEnd() {
        writeInvokerMethods();
        super.visitEnd();
    }

    private void writeInvokerMethods() {
        for (String signature : invokerDescriptors) {
            writeInvokerMethod(signature);
        }
    }

    private void writeInvokerMethod(String descriptor) {
        Type returnType = Type.getReturnType(descriptor);
        Type argumentTypes[] = Type.getArgumentTypes(descriptor);
        int numHandleArguments = argumentTypes.length - 1;

        int access = Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC;
        String methodName = invokerName(returnType);
        MethodVisitor mv = visitMethod(access, methodName, descriptor, null, null);
        mv.visitCode();

        GeneratorAdapter adapter = new GeneratorAdapter(mv, access, methodName, descriptor);

        // Load method handle
        adapter.visitVarInsn(Opcodes.ALOAD, 0);

        // Create a new Object[]
        adapter.visitLdcInsn(numHandleArguments);
        adapter.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");

        // Store each argument to the handle, unboxing if necessary
        for (int i = 0; i < numHandleArguments; i++) {
            int parameterIndex = i + 1;
            Type parameterType = argumentTypes[parameterIndex];
            adapter.visitInsn(Opcodes.DUP);
            adapter.visitLdcInsn(i);
            adapter.loadArg(parameterIndex);
            if(isPrimitive(parameterType)) {
                adapter.box(parameterType);
            }
            adapter.visitInsn(Opcodes.AASTORE);
        }

        adapter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/orfjackal/retrolambda/invoke/MethodHandle",
                "invokeWithArguments", "([Ljava/lang/Object;)Ljava/lang/Object;", false);

        if(isPrimitive(returnType)) {
            adapter.unbox(returnType);
        }

        adapter.returnValue();
        adapter.endMethod();
    }

    private boolean isPrimitive(Type parameterType) {
        return parameterType.getSort() != Type.OBJECT && parameterType.getSort() != Type.ARRAY;
    }

    private String invokerName(Type returnType) {
        String name;
        if(returnType.getSort() == Type.OBJECT || returnType.getSort() == Type.ARRAY) {
            name = "$$invoke";
        } else {
            name = "$$invoke" + returnType.getDescriptor();
        }
        return name;
    }

    private Type collapseObjects(Type type) {
        if(type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
            return Type.getType("Ljava/lang/Object;");
        } else {
            return type;
        }
    }
}
