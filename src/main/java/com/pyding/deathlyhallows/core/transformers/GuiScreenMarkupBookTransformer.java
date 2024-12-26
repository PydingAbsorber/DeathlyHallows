package com.pyding.deathlyhallows.core.transformers;

import airburn.fasmtel.transformers.InsnNodePredicates;
import airburn.fasmtel.transformers.MethodData;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class GuiScreenMarkupBookTransformer extends ClassTransformerBase {

	public GuiScreenMarkupBookTransformer(byte[] basicClass) {
		super(
				basicClass,
				0,
				new MethodData(
						GuiScreenMarkupBookTransformer::constructPage,
						"constructPage"
				)
		);
	}

	public static boolean constructPage(MethodNode mnode) {
		AbstractInsnNode node = getNode(mnode, InsnNodePredicates.Method(INVOKESTATIC, "translateToLocal"));
		mnode.instructions.insert(node, list(
				new VarInsnNode(ALOAD, 0),
				new FieldInsnNode(GETFIELD, "com/emoniph/witchery/client/gui/GuiScreenMarkupBook", "player", "Lnet/minecraft/entity/player/EntityPlayer;"),
				new MethodInsnNode(INVOKESTATIC,
						ClassTransformerBase.HOOKS,
						"witcheryFormatMarkdownBook",
						"(Ljava/lang/String;Lnet/minecraft/entity/player/EntityPlayer;)Ljava/lang/String;",
						false
				)
		));
		return true;
	}

}
