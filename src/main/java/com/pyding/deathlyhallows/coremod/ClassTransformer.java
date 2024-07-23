package com.pyding.deathlyhallows.coremod;

import com.emoniph.witchery.client.gui.GuiScreenWitchcraftBook;
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
import static org.objectweb.asm.Opcodes.RETURN;

public class ClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		switch(name){
			case "com.emoniph.witchery.client.gui.GuiScreenWitchcraftBook": {
				return basicClass;
			}
		}
		return basicClass;
	}
	
	public byte[] drawGui(byte[] basicClass) {
		ClassNode cnode = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cnode, 0);

		for(MethodNode mnode: cnode.methods) {
			if(mnode.name.equals("drawScreen")) {
				vzlomDraw(mnode);
			}
		}
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cnode.accept(cw);

		return cw.toByteArray();
	}
	
	public void vzlomDraw(MethodNode mnode){
		InsnList list = new InsnList(); 
		list.add(new VarInsnNode(ALOAD,0));
		list.add(new VarInsnNode(ILOAD,1));
		list.add(new VarInsnNode(ILOAD,2));
		list.add(new VarInsnNode(FLOAD,3)); 
		list.add(new MethodInsnNode(INVOKESTATIC,"com/pyding/deathlyhallows/coremod/VzlomJop","gui","(Lcom/emoniph/witchery/client/gui/GuiScreenWitchcraftBook;IIF)V",false));
		list.add(new InsnNode(RETURN));
		mnode.instructions.insertBefore(mnode.instructions.getFirst(),list);
	}
}
