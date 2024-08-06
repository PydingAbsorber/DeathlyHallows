package com.pyding.deathlyhallows.core.transformers;

import com.pyding.deathlyhallows.core.DHHooks;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class ItemMysticBranchTransformer extends ClassTransformerBase {

	public ItemMysticBranchTransformer(byte[] basicClass) {
		super(
				basicClass,
				0,
				new MethodData(
						ItemMysticBranchTransformer::onPlayerStoppedUsing,
						"onPlayerStoppedUsing"
				)
		);
	}

	public static boolean onPlayerStoppedUsing(MethodNode mnode) {
		AbstractInsnNode node = getNode(mnode.instructions,
				(n) -> n.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode)n).name.equals("perform")
		);
		if(node == null) {
			return false;
		}
		LabelNode skip = new LabelNode();
		InsnList list = list(
				new VarInsnNode(ALOAD, 8),
				new VarInsnNode(ALOAD, 2),
				new VarInsnNode(ALOAD, 3),
				new VarInsnNode(ILOAD, 7),
				new MethodInsnNode(INVOKESTATIC,
						DHHooks.classPath,
						"witcheryBranchPerform",
						"(Lcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffect;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;I)I",
						false
				),
				new VarInsnNode(ISTORE, 7),
				new VarInsnNode(ILOAD, 7),
				new JumpInsnNode(IFNE, skip),
				new InsnNode(RETURN),
				skip
		);

		mnode.instructions.insertBefore(previous(node, 4), list);
		return true;
	}

}
