package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemElderChalk extends ItemBase {
	private static final int MAX_DAMAGE = 64;
	private static final int DAMAGE_PER_USE = 1;

	public ItemElderChalk() {
		super("chalkelder", 64);
		setMaxDamage(MAX_DAMAGE);
		setNoRepair();
	}

	public int getItemStackLimit(ItemStack stack) {
		return stack.isItemDamaged() ? 1 : maxStackSize;
	}
	
	public static boolean drawGlyph(World world, int posX, int posY, int posZ, int side, Block block, EntityPlayer player) {
		boolean chalkUsed = false;
		if(block != Witchery.Blocks.CIRCLE) {
			Block overBlock = world.getBlock(posX, posY, posZ);
			if(overBlock == block) {
				world.setBlockMetadataWithNotify(posX, posY, posZ, world.rand.nextInt(12), 3);
				chalkUsed = true;
			}
			else if(overBlock != Witchery.Blocks.GLYPH_RITUAL && overBlock != Witchery.Blocks.GLYPH_OTHERWHERE && overBlock != Witchery.Blocks.GLYPH_INFERNAL) {
				if(BlockSide.TOP.isEqual(side) && Witchery.Blocks.GLYPH_RITUAL.canBlockStay(world, posX, posY + 1, posZ) && BlockUtil.isReplaceableBlock(world, posX, posY + 1, posZ, player)) {
					world.setBlock(posX, posY + 1, posZ, block, world.rand.nextInt(12), 3);
					world.markBlockForUpdate(posX, posY + 1, posZ);
					chalkUsed = true;
				}
			}
			else {
				world.setBlock(posX, posY, posZ, block, world.rand.nextInt(12), 3);
				world.markBlockForUpdate(posX, posY, posZ);
				chalkUsed = true;
			}
		}
		else if(world.getBlock(posX, posY, posZ) != block && BlockSide.TOP.isEqual(side) && Witchery.Blocks.CIRCLE.canBlockStay(world, posX, posY + 1, posZ)) {
			world.setBlock(posX, posY + 1, posZ, block);
			world.markBlockForUpdate(posX, posY + 1, posZ);
			chalkUsed = true;
		}

		if(chalkUsed) {
			SoundEffect.WITCHERY_RANDOM_CHALK.playAt(world, posX, posY, posZ, 1.0F, 1.0F);
		}
		return chalkUsed;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int posX, int posY, int posZ, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return false;
		}
		boolean chalkUsed = drawGlyph(world, posX, posY, posZ, side, DHBlocks.elderGlyph, player);
		if(!chalkUsed || player.capabilities.isCreativeMode) {
			return false;
		}
		stack.damageItem(DAMAGE_PER_USE, player);
		if(stack.stackSize > 1) {
			ItemStack temp = ItemStack.copyItemStack(stack);
			--temp.stackSize;
			temp.setItemDamage(0);
			if(!player.inventory.addItemStackToInventory(temp)) {
				world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5, player.posY + 1.5, player.posZ + 0.5, temp));
			}
			else if(player instanceof EntityPlayerMP) {
				((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
			}
			stack.stackSize = 1;
		}
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger("PseudoDamage", stack.getItemDamage());
		return false;
	}
}
