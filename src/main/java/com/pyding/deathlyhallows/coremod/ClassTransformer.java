package com.pyding.deathlyhallows.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class ClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		switch(name){
			case "com.emoniph.witchery.client.gui.GuiScreenWitchcraftBook": {
				return meth(basicClass);
			}
		}
		return basicClass;
	}
	
	public byte[] meth(byte[] basicClass) {
		ClassNode cnode = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cnode, 0);

		for(MethodNode mnode: cnode.methods) {
			if(mnode.name.equals("drawScreen")) {
				drawPenis(mnode);
				return basicClass;
			}
		}
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cnode.accept(cw);

		return cw.toByteArray();
	}
	
	public void drawPenis(MethodNode mnode){
		InsnList list = new InsnList(); 
		list.add(new VarInsnNode(ALOAD,0));
		list.add(new VarInsnNode(ILOAD,1));
		list.add(new VarInsnNode(ILOAD,2));
		list.add(new VarInsnNode(FLOAD,3)); 
		list.add(new MethodInsnNode(INVOKESTATIC,"com/pyding/deathlyhallows/coremod/VzlomJop","gui","(Lcom/emoniph/witchery/client/gui/GuiScreenWitchcraftBook;IIF)V",false));
		mnode.instructions.insert(list);
	}
}
