package com.pyding.deathlyhallows.core.transformers;

import org.objectweb.asm.tree.MethodNode;

import java.util.function.Function;

public final class MethodData {

	private final Function<MethodNode, Boolean> methodTransformer;
	public final String
			deobfName,
			obfName,
			desc;
	private boolean visited = false;

	MethodData(Function<MethodNode, Boolean> methodTransformer, String deobfName, String obfName, String desc) {
		this.methodTransformer = methodTransformer;
		this.deobfName = deobfName;
		this.obfName = obfName;
		this.desc = desc;
		if(desc != null && (!desc.contains("(") || !desc.contains(")"))) {
			throw new IllegalArgumentException("Args must contain (), but was: " + desc);
		}
	}

	MethodData(Function<MethodNode, Boolean> methodTransformer, String deobfName) {
		this(methodTransformer, deobfName, null, null);
	}

	void setVisited() {
		visited = true;
	}

	boolean isVisited() {
		return visited;
	}

	boolean apply(MethodNode mnode) {
		return methodTransformer.apply(mnode);
	}

	boolean isRightMethod(MethodNode mnode) {
		if(mnode.name.equals(deobfName) || mnode.name.equals(obfName)) {
			return desc == null || mnode.desc.equals(desc);
		}
		return false;
	}

}
