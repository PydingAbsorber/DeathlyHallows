package com.pyding.deathlyhallows.core.transformers;

import com.google.common.collect.Lists;
import com.pyding.deathlyhallows.DeathlyHallows;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ClassTransformerBase {

	private final byte[] basicClass;
	private final int flags;
	private final List<MethodData> methods;

	public ClassTransformerBase(byte[] basicClass, int flags, MethodData... methods) {
		this.basicClass = basicClass;
		this.flags = flags;
		this.methods = Lists.newArrayList(methods);
	}

	public ClassTransformerBase(byte[] basicClass, MethodData... methods) {
		this(basicClass, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES, methods);
	}

	public final byte[] transform() {
		ClassNode cnode = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cnode, 0);
		for(MethodNode mnode: cnode.methods) {
			for(MethodData method: methods) {
				if(method.isVisited() || !method.isRightMethod(mnode)) {
					continue;
				}
				if(!method.apply(mnode)) {
					DeathlyHallows.LOG.error("Error transforming method '" + mnode.name + "' in class '" + cr.getClassName() + "', transformers will not be applied.");
					return basicClass;
				}
				method.setVisited();
				DeathlyHallows.LOG.info("Method '" + mnode.name + "' in class '" + cr.getClassName() + "' transformed successfully.");
			}
		}
		for(MethodData method: methods) {
			if(!method.isVisited()) {
				DeathlyHallows.LOG.warn("Method '" + method.deobfName + "' was defined for transform for class '" + cr.getClassName() + "' but was not visited.");
			}
		}
		ClassWriter cw = new ClassWriter(flags);
		cnode.accept(cw);

		return cw.toByteArray();
	}

	protected static AbstractInsnNode getNode(InsnList list, Predicate<AbstractInsnNode> condition) {
		for(AbstractInsnNode node: new InsnListIterable(list)) {
			if(condition.test(node)) {
				return node;
			}
		}
		return null;
	}

	protected static AbstractInsnNode getNode(Iterator<AbstractInsnNode> iterator, Predicate<AbstractInsnNode> condition) {
		AbstractInsnNode node;
		while(iterator.hasNext()) {
			node = iterator.next();
			if(condition.test(node)) {
				return node;
			}
		}
		return null;
	}

	protected static InsnList list(AbstractInsnNode... nodes) {
		InsnList list = new InsnList();
		for(AbstractInsnNode node: nodes) {
			list.add(node);
		}
		return list;
	}

	protected static AbstractInsnNode previous(AbstractInsnNode node, int skips) {
		for(int i = 0; i < skips; ++i) {
			node = node.getPrevious();
		}
		return node;
	}

	protected static AbstractInsnNode next(AbstractInsnNode node, int skips) {
		for(int i = 0; i < skips; ++i) {
			node = node.getNext();
		}
		return node;
	}

	// Helper aggregator class, cuz InsnList HAS iterator, but does not implement Iterable<?>
	private static class InsnListIterable implements Iterable<AbstractInsnNode> {

		private final InsnList list;

		InsnListIterable(InsnList list) {
			this.list = list;
		}

		@Override
		public Iterator<AbstractInsnNode> iterator() {
			return list.iterator();
		}

	}

}
