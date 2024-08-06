package com.pyding.deathlyhallows.core.transformers;

import com.pyding.deathlyhallows.core.DHHooks;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class SymbolEffectTransformer extends ClassTransformerBase {

	public SymbolEffectTransformer(byte[] basicClass) {
		super(
				basicClass,
				0,
				new MethodData(
						SymbolEffectTransformer::getChargeCost,
						"getChargeCost"
				),
				new MethodData(
						SymbolEffectTransformer::cooldownRemaining,
						"cooldownRemaining"
				)
		);
	}

	public static boolean getChargeCost(MethodNode mnode) {
		InsnList list = list(
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new VarInsnNode(ILOAD, 3),
				new MethodInsnNode(INVOKESTATIC,
						DHHooks.classPath,
						"witcherySymbolGetChargeCost",
						"(ILcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffect;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;I)I",
						false
				)
		);
		mnode.instructions.insertBefore(mnode.instructions.getLast().getPrevious(), list);
		return true;
	}

	public static boolean cooldownRemaining(MethodNode mnode) {
		AbstractInsnNode node = getNode(mnode.instructions,
				(n) -> n.getOpcode() == LSTORE && ((VarInsnNode)n).var == 6
		);
		LabelNode skip = new LabelNode();
		InsnList list = list(
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new MethodInsnNode(INVOKESTATIC,
						DHHooks.classPath,
						"witcherySymbolCooldownOverride",
						"(Lcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffect;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/nbt/NBTTagCompound;)Z",
						false
				),
				new JumpInsnNode(IFEQ, skip),
				new VarInsnNode(LLOAD, 4),
				new VarInsnNode(LLOAD, 6),
				new VarInsnNode(ALOAD, 0),
				new FieldInsnNode(GETFIELD, "com/emoniph/witchery/infusion/infusions/symbols/SymbolEffect", "cooldownTicks", "I"),
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new MethodInsnNode(INVOKESTATIC,
						DHHooks.classPath,
						"witcherySymbolCooldownRemaining",
						"(JJILcom/emoniph/witchery/infusion/infusions/symbols/SymbolEffect;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/nbt/NBTTagCompound;)J",
						false
				),
				new InsnNode(LRETURN),
				skip
		);
		mnode.instructions.insert(node, list);
		return true;
	}

}
