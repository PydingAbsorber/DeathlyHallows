package com.pyding.deathlyhallows.core.transformers;

import airburn.fasmtel.transformers.InsnNodePredicates;
import airburn.fasmtel.transformers.MethodData;
import com.pyding.deathlyhallows.core.DHThaumcraftHooks;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class ItemWandCastingTransformer extends ClassTransformerBase {

	public ItemWandCastingTransformer(byte[] basicClass) {
		super(
				basicClass,
				0,
				new MethodData(
						ItemWandCastingTransformer::getConsumptionModifier,
						"getConsumptionModifier"
				)
		);
	}

	public static boolean getConsumptionModifier(MethodNode mnode) {
		AbstractInsnNode node = getNode(mnode, InsnNodePredicates.Method(INVOKESTATIC,"max"));
		if(node == null) {
			return false;
		}
		mnode.instructions.insert(node, list(
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new VarInsnNode(ALOAD, 3),
				new VarInsnNode(ILOAD, 4),
				new MethodInsnNode(INVOKESTATIC,
						getPath(DHThaumcraftHooks.class),
						"thaumcraftWandVisDiscountUnlimited",
						"(FLthaumcraft/common/items/wands/ItemWandCasting;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/Aspect;Z)F",
						false
				)
		));
		node = previous(node, InsnNodePredicates.Var(FLOAD, 5));
		if(node == null) {
			return false;
		}
		mnode.instructions.insert(node, list(
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new VarInsnNode(ALOAD, 3),
				new VarInsnNode(ILOAD, 4),
				new MethodInsnNode(INVOKESTATIC,
						getPath(DHThaumcraftHooks.class),
						"thaumcraftWandVisDiscountPost",
						"(FLthaumcraft/common/items/wands/ItemWandCasting;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/Aspect;Z)F",
						false
				)
		));
		return true;
	}

}
