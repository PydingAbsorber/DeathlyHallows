package com.pyding.deathlyhallows.items;

import baubles.api.BaublesApi;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemVanillaPotion;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.events.DHEvents;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrickOrTreat extends Item {
	public static List witcheryItems = new ArrayList<>();
	public static List itemList = new ArrayList<>();

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.splitStack(1);
		Object o;
		DHEvents eventHandler = new DHEvents();
		double chance = 0.98;
		if(eventHandler.hasDeathlyHallow(player)) {
			chance = 0.96;
		}
		if(Math.random() < chance) {
			o = randomObjectFromList(getWitcheryItems(player));
		}
		else {
			o = randomObjectFromList(getDeathlyHallowItems());
		}
		ItemStack itemStack;
		if(o instanceof Item) {
			itemStack = new ItemStack((Item)o);
		}
		else if(o instanceof ItemGeneral.SubItem) {
			itemStack = ((ItemGeneral.SubItem)o).createStack();
		}
		else if(o instanceof ItemBase) {
			itemStack = new ItemStack((ItemBase)o);
		}
		else if(o instanceof Block) {
			itemStack = new ItemStack((Block)o);
		}
		else if(o instanceof ItemVanillaPotion) {
			itemStack = new ItemStack((ItemVanillaPotion)o);
		}
		else if(o instanceof ItemStack) {
			itemStack = (ItemStack)o;
		}
		else {
			itemStack = new ItemStack(Items.rotten_flesh);
		}
		SoundEffect effect1 = SoundEffect.WITCHERY_MOB_BABA_DEATH;
		SoundEffect effect2 = SoundEffect.WITCHERY_MOB_BABA_LIVING;
		if(!world.isRemote) {
			if(hasInventorySpace(player)) {
				player.inventory.addItemStackToInventory(itemStack);
			}
			else {
				player.entityDropItem(itemStack, 1);
			}
		}
		if(Math.random() < 0.5) {
			effect1.playAtPlayer(world, player, 1, 1);
		}
		else {
			effect2.playAtPlayer(world, player, 1, 1);
		}
		ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, player, 1.0, 2.0, 8);
		return super.onItemRightClick(stack, world, player);
	}

	public boolean hasInventorySpace(EntityPlayer player) {
		for(int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
			if(player.inventory.getStackInSlot(i) == null) {
				return true;
			}
		}
		return false;
	}

	public Object randomObjectFromList(List list) {
		Random random = new Random();
		int randomIndex = random.nextInt(list.size());
		return list.get(randomIndex);
	}

	public List getWitcheryItems(EntityPlayer player) {
		if(hasRing(player) != null) {
			ResurrectionStone stone = new ResurrectionStone();
			ItemStack itemStack = new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1);
			Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(itemStack, player, stone.getPlayer(hasRing(player)), false, 1);
			if(!witcheryItems.contains(itemStack)) {
				witcheryItems.add(itemStack);
			}
			else {
				witcheryItems.set(witcheryItems.size(), itemStack);
			}
		}
		return witcheryItems;
	}

	public List getDeathlyHallowItems() {
		return itemList;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if(DHIntegration.thaumcraft) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				list.add(StatCollector.translateToLocal("dh.desc.trick1"));
				list.add(StatCollector.translateToLocal("dh.desc.trick2"));
				list.add(StatCollector.translateToLocal("dh.desc.trick3"));
				list.add(StatCollector.translateToLocal("dh.desc.trick4"));
			}
			else {
				list.add(StatCollector.translateToLocal("dh.desc.trick5"));
			}
		}
	}

	public ItemStack hasRing(EntityPlayer player) {
		for(int i = 1; i < 4; i++) {
			if(BaublesApi.getBaubles(player).getStackInSlot(i) != null) {
				ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(i);
				if(stack.getItem() == DHItems.resurrectionStone) {
					return stack;
				}
			}
		}
		return null;
	}

	public static void initList() {
		itemList.add(DHItems.invisibilityMantle);
		itemList.add(DHItems.elderWand);
		itemList.add(DHItems.resurrectionStone);
		itemList.add(DHItems.tabItem);
		itemList.add(DHItems.creativeItem);
		itemList.add(DHItems.bertieBots);
		itemList.add(DHItems.gastronomicTemptation);
		itemList.add(DHItems.soupWithSawdust);
		itemList.add(DHItems.viscousSecretions);
		itemList.add(DHItems.hobgoblinChains);
		itemList.add(DHItems.deadlyPrism);
		itemList.add(DHItems.hobgoblinSoul);
		itemList.add(DHItems.nimbus);
		itemList.add(DHItems.deathShard);
		itemList.add(DHItems.cards);
		if(DHIntegration.thaumcraft) {
			itemList.add(DHItems.inferioisMutandis);
		}
		itemList.add(DHItems.monsterBook);
		itemList.add(DHItems.trickOrTreat);
		for(Object o: Witchery.Items.GENERIC.subItems) {
			witcheryItems.add(o);
		}
		for(Object o: Witchery.Items.POTIONS.subItems) {
			witcheryItems.add(o);
		}
	}
}
