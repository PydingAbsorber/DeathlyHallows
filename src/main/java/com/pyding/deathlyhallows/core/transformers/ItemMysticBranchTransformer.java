package com.pyding.deathlyhallows.core.transformers;

import airburn.fasmtel.transformers.InsnNodePredicates;
import airburn.fasmtel.transformers.MethodData;
import org.objectweb.asm.tree.AbstractInsnNode;
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
		AbstractInsnNode node = getNode(mnode, InsnNodePredicates.Method(INVOKEVIRTUAL, "perform"));
		if(node == null) {
			return false;
		}
		LabelNode skip = new LabelNode();
		mnode.instructions.insertBefore(previous(node, 4), list(
				new VarInsnNode(ALOAD, 8),
				new VarInsnNode(ALOAD, 2),
				new VarInsnNode(ALOAD, 3),
				new VarInsnNode(ILOAD, 7),
				new MethodInsnNode(INVOKESTATIC,
						ClassTransformerBase.HOOKS,
						"witcheryBranchCanPerform",
						"(Lcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffect;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;I)Z",
						false
				),
				new JumpInsnNode(IFNE, skip),
				new InsnNode(RETURN),
				skip
		));
		mnode.instructions.insertBefore(node, list(
				new VarInsnNode(ALOAD, 8),
				new VarInsnNode(ALOAD, 2),
				new VarInsnNode(ALOAD, 3),
				new MethodInsnNode(INVOKESTATIC,
						ClassTransformerBase.HOOKS,
						"witcheryBranchPerformLevel",
						"(ILcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffect;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)I",
						false
				)
		));
		return true;
	}

}
