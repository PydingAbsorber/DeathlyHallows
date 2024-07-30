package com.pyding.deathlyhallows.items;

import baubles.api.BaublesApi;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemVanillaPotion;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemTrickOrTreat extends Item {

	private static final List<ItemStack> witcheryItems = new ArrayList<>();
	private static final List<ItemStack> deathlyHallowItems = new ArrayList<>();

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.splitStack(1);
		double chance = 0.98;
		if(DHUtils.hasDeathlyHallow(player)) {
			chance = 0.96;
		}
		ItemStack loot = randomObjectFromList(Math.random() > chance ? getDeathlyHallowItems() : getWitcheryItems(player));
		if(!world.isRemote && !player.inventory.addItemStackToInventory(loot)) {
			player.entityDropItem(loot, 1);
		}
		// lolwhat?
		(Math.random() < 0.5 ? SoundEffect.WITCHERY_MOB_BABA_DEATH : SoundEffect.WITCHERY_MOB_BABA_LIVING).playAtPlayer(world, player, 1, 1);
		ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, player, 1.0, 2.0, 8);
		return super.onItemRightClick(stack, world, player);
	}

	public ItemStack randomObjectFromList(List<ItemStack> list) {
		Random random = new Random();
		int randomIndex = random.nextInt(list.size());
		return list.get(randomIndex);
	}

	public List<ItemStack> getWitcheryItems(EntityPlayer player) {
		if(hasRing(player) != null) {
			ResurrectionStone stone = new ResurrectionStone();
			ItemStack stack = new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1);
			Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(stack, player, stone.getPlayer(hasRing(player)), false, 1);
			if(!witcheryItems.contains(stack)) {
				witcheryItems.add(stack);
			}
			else {
				witcheryItems.set(witcheryItems.size(), stack);
			}
		}
		return witcheryItems;
	}

	public List<ItemStack> getDeathlyHallowItems() {
		return deathlyHallowItems;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if(!DHIntegration.thaumcraft) {
			return;
		}
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

	public ItemStack hasRing(EntityPlayer player) {
		IInventory baubles = BaublesApi.getBaubles(player);
		for(int i = 1; i < baubles.getSizeInventory(); i++) {
			ItemStack stack = baubles.getStackInSlot(i);
			if(stack == null || stack.getItem() != DHItems.resurrectionStone) {
				continue;
			}
			return stack;
		}
		return null;
	}

	public static void initList() {
		deathlyHallowItems.add(new ItemStack(DHItems.invisibilityMantle));
		deathlyHallowItems.add(new ItemStack(DHItems.elderWand));
		deathlyHallowItems.add(new ItemStack(DHItems.resurrectionStone));
		deathlyHallowItems.add(new ItemStack(DHItems.tabItem));
		deathlyHallowItems.add(new ItemStack(DHItems.creativeItem));
		deathlyHallowItems.add(new ItemStack(DHItems.bertieBots));
		deathlyHallowItems.add(new ItemStack(DHItems.gastronomicTemptation));
		deathlyHallowItems.add(new ItemStack(DHItems.soupWithSawdust));
		deathlyHallowItems.add(new ItemStack(DHItems.viscousSecretions));
		deathlyHallowItems.add(new ItemStack(DHItems.hobgoblinChains));
		deathlyHallowItems.add(new ItemStack(DHItems.deadlyPrism));
		deathlyHallowItems.add(new ItemStack(DHItems.hobgoblinSoul));
		deathlyHallowItems.add(new ItemStack(DHItems.nimbus));
		deathlyHallowItems.add(new ItemStack(DHItems.deathShard));
		deathlyHallowItems.add(new ItemStack(DHItems.cards));
		deathlyHallowItems.add(new ItemStack(DHItems.monsterBook));
		deathlyHallowItems.add(new ItemStack(DHItems.trickOrTreat));
		deathlyHallowItems.add(new ItemStack(DHItems.lightningInBag));
		if(DHIntegration.thaumcraft) {
			deathlyHallowItems.add(new ItemStack(DHItems.inferioisMutandis));
		}
		for(ItemGeneral.SubItem bottom: Witchery.Items.GENERIC.subItems) {
			witcheryItems.add(bottom.createStack());
		}
		for(ItemVanillaPotion.SubItem submissive: Witchery.Items.POTIONS.subItems) {
			witcheryItems.add(submissive.createStack());
		}
	}
}
