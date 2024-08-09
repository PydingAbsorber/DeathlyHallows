package com.pyding.deathlyhallows.core.transformers;

import com.pyding.deathlyhallows.core.DHHooks;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ISTORE;

public class InfusionTransformer extends ClassTransformerBase {

	public InfusionTransformer(byte[] basicClass) {
		super(
				basicClass,
				0,
				new MethodData(
						InfusionTransformer::getMaxEnergy,
						"getMaxEnergy"
				),
				new MethodData(
						InfusionTransformer::infuse,
						"infuse"
				)
		);
	}

	public static boolean getMaxEnergy(MethodNode mnode) {
		InsnList list = list(
				new VarInsnNode(ALOAD, 0),
				new MethodInsnNode(INVOKESTATIC,
						DHHooks.classPath,
						"witcheryMaxInfusion",
						"(ILnet/minecraft/entity/player/EntityPlayer;)I",
						false
				)
		);
		mnode.instructions.insertBefore(mnode.instructions.getLast().getPrevious(), list);
		return true;
	}

	public static boolean infuse(MethodNode mnode) {
		InsnList list = list(
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ILOAD, 2),
				new MethodInsnNode(INVOKESTATIC,
						DHHooks.classPath,
						"witcheryInfuse",
						"(Lcom/emoniph/witchery/infusion/Infusion;Lnet/minecraft/entity/player/EntityPlayer;I)I",
						false
				),
				new VarInsnNode(ISTORE, 2)
		);
		mnode.instructions.insert(list);
		return true;
	}
	
}
