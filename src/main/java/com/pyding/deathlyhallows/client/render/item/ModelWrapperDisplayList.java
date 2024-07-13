package com.pyding.deathlyhallows.client.render.item;

import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModelWrapperDisplayList implements IModelCustom {
	// Для каждой группы свой лист
	private final Map<String, Integer> lists = new HashMap<>();
	// Буффер, который будет содержать все листы. Для более быстрого рендера всей модели.
	private final IntBuffer bufAll;
	private final String type;

	public ModelWrapperDisplayList(WavefrontObject model) {
		type = model.getType();
		int list = GL11.glGenLists(model.groupObjects.size());
		for(GroupObject obj: model.groupObjects) {
			GL11.glNewList(list, GL11.GL_COMPILE);
			model.renderPart(obj.name);
			GL11.glEndList();
			lists.put(obj.name, list++);
		}
		bufAll = initBuffer();
	}

	private IntBuffer initBuffer() {
		IntBuffer buf = BufferUtils.createIntBuffer(lists.size());
		for(int i: lists.values()) {
			buf.put(i);
		}
		buf.flip();
		return buf;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void renderAll() {
		GL11.glCallLists(bufAll);
	}

	@Override
	public void renderOnly(String... groupNames) {
		if(groupNames == null) {
			return;
		}

		for(String group: groupNames) {
			renderPart(group);
		}
	}

	@Override
	public void renderPart(String partName) {
		Integer list = lists.get(partName);
		if(list != null) {
			GL11.glCallList(list);
		}
	}

	@Override
	public void renderAllExcept(String... groupNames) {
		if(groupNames == null || groupNames.length == 0) {
			renderAll();
			return;
		}

		for(Map.Entry<String, Integer> it: lists.entrySet()) {
			if(Arrays.binarySearch(groupNames, it.getKey(), String::compareTo) < 0) {
				GL11.glCallList(it.getValue());
			}
		}
	}
}
