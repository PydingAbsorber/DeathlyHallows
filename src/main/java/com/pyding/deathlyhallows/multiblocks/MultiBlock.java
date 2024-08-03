package com.pyding.deathlyhallows.multiblocks;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MultiBlock {

	public List<MultiBlockComponent> components = new ArrayList<>();
	public List<ItemStack> materials = new ArrayList<>();

	public int minX, minY, minZ, maxX, maxY, maxZ, offX, offY, offZ;

	public HashMap<List<Integer>, MultiBlockComponent> locationCache = new HashMap<List<Integer>, MultiBlockComponent>();

	/**
	 Adds a MultiBlock component to this MultiBlock. The component's x y z
	 coords should be pivoted to the center of the structure.
	 */
	public void addComponent(MultiBlockComponent component) {
		if(getComponentForLocation(component.relPos.posX, component.relPos.posY, component.relPos.posZ) != null) {
			throw new IllegalArgumentException("Location in MultiBlock already occupied");
		}
		components.add(component);
		changeAxisForNewComponent(component.relPos.posX, component.relPos.posY, component.relPos.posZ);
		calculateCostForNewComponent(component);
		addComponentToLocationCache(component);
	}

	public void addComponent(int x, int y, int z, Block block, int meta) {
		addComponent(new MultiBlockComponent(new ChunkCoordinates(x, y, z), block, meta));
	}
	
	public void addComponent(int x, int y, int z, Block block, int meta, NBTTagCompound tag) {
		addComponent(new MultiBlockComponent(new ChunkCoordinates(x, y, z), block, meta, tag));
	}

	public void mergeMultiBlocks(MultiBlock mb) {
		for(MultiBlockComponent component: mb.components) {
			try {
				addComponent(component);
			}
			catch(Exception ignored) {

			}
		}
	}

	private void changeAxisForNewComponent(int x, int y, int z) {
		if(x < minX) {
			minX = x;
		}
		else if(x > maxX) {
			maxX = x;
		}

		if(y < minY) {
			minY = y;
		}
		else if(y > maxY) {
			maxY = y;
		}

		if(z < minZ) {
			minZ = z;
		}
		else if(z > maxZ) {
			maxZ = z;
		}
	}

	private void calculateCostForNewComponent(MultiBlockComponent comp) {
		ItemStack[] materials = comp.getMaterials();
		if(materials != null) {
			for(ItemStack stack: materials) {
				addStack(stack);
			}
		}
	}

	private void addStack(ItemStack stack) {
		if(stack == null) {
			return;
		}

		for(ItemStack oStack: materials) {
			if(oStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(oStack, stack)) {
				oStack.stackSize += stack.stackSize;
				return;
			}
		}

		materials.add(stack);
	}

	public void setRenderOffset(int x, int y, int z) {
		offX = x;
		offY = y;
		offZ = z;
	}

	public List<MultiBlockComponent> getComponents() {
		return components;
	}

	public void rotate(double angle) {
		for(MultiBlockComponent comp: getComponents()) {
			comp.rotate(angle);
		}
		updateLocationCache();
	}

	public MultiBlock copy() {
		MultiBlock mb = new MultiBlock();
		for(MultiBlockComponent comp: getComponents()) {
			mb.addComponent(comp.copy());
		}

		return mb;
	}

	public MultiBlock[] createRotations() {
		MultiBlock[] blocks = new MultiBlock[4];
		blocks[0] = this;
		blocks[1] = blocks[0].copy();
		blocks[1].rotate(Math.PI / 2);
		blocks[2] = blocks[1].copy();
		blocks[2].rotate(Math.PI / 2);
		blocks[3] = blocks[2].copy();
		blocks[3].rotate(Math.PI / 2);

		return blocks;
	}
	
	public MultiBlockSet makeSet() {
		return new MultiBlockSet(this);
	}

	public int getXSize() {
		return Math.abs(minX) + Math.abs(maxX) + 1;
	}

	public int getYSize() {
		return Math.abs(minY) + Math.abs(maxY) + 1;
	}

	public int getZSize() {
		return Math.abs(minZ) + Math.abs(maxZ) + 1;
	}
	
	public void updateLocationCache() {
		locationCache.clear();
		for(MultiBlockComponent comp: components) {
			addComponentToLocationCache(comp);
		}
	}
	
	private void addComponentToLocationCache(MultiBlockComponent comp) {
		ChunkCoordinates pos = comp.getRelativePosition();
		locationCache.put(Arrays.asList(pos.posX, pos.posY, pos.posZ), comp);
	}
	
	public MultiBlockComponent getComponentForLocation(int x, int y, int z) {
		return locationCache.get(Arrays.asList(x, y, z));
	}

	public boolean matchAndRemove(World world, int x, int y, int z, boolean toRemove) {
		MultiBlockSet set = this.makeSet();
		for(int i = 0; i < 4; i++) {
			MultiBlock mb = set.getForIndex(i);
			MultiBlockComponent heartGlyph = mb.getHeartGlyph();
			int relX = x, relY = y, relZ = z;
			if(heartGlyph != null && heartGlyph.relPos != null) {
				relX -= heartGlyph.relPos.posX;
				relY -= heartGlyph.relPos.posY;
				relZ -= heartGlyph.relPos.posZ;
			}
			boolean matches = true;
			for(MultiBlockComponent component: mb.components) {
				if(!component.matches(world, relX + component.relPos.posX, relY + component.relPos.posY, relZ + component.relPos.posZ)) {
					matches = false;
				}
			}
			if(matches && toRemove) {
				for(MultiBlockComponent component: mb.components) {
					if(!component.equals(getHeartGlyph())) {
						world.setBlockToAir(relX + component.relPos.posX, relY + component.relPos.posY, relZ + component.relPos.posZ);
					}
				}
			}
			if(matches) {
				return true;
			}
		}
		return false;
	}

	public MultiBlockComponent getHeartGlyph() {
		for(MultiBlockComponent component: components) {
			if(component.getBlock().getUnlocalizedName().equals(DHBlocks.elderRitualRune.getUnlocalizedName())) {
				return component;
			}
		}
		return null;
	}

	public boolean equals(MultiBlock other) {
		if(other == null) {
			return false;
		}
		if(other.components.size() != components.size()) {
			return false;
		}
		for(int i = 0; i < components.size(); i++) {
			if(!components.get(i).equals(other.components.get(i))) {
				return false;
			}
		}
		return true;
	}


}
