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
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemTrickOrTreat extends ItemBase {

	private static final List<ItemStack>
			witcheryItems = new ArrayList<>(),
			deathlyHallowItems = new ArrayList<>();

	public ItemTrickOrTreat() {
		super("trickOrTreat", 64);
	}


	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		stack.splitStack(1);
		if(world.isRemote) {
			return stack;
		}
		double chance = DHUtils.hasDeathlyHallow(p) ? 0.96 : 0.98;
		ItemStack loot = randomObjectFromList(Math.random() > chance ? getDeathlyHallowItems() : getWitcheryItems(p));
		if(p.inventory.addItemStackToInventory(loot)) {
			p.inventoryContainer.detectAndSendChanges();
		}
		else {
			p.entityDropItem(loot, 1);
		}
		(Math.random() < 0.5 ? SoundEffect.WITCHERY_MOB_BABA_DEATH : SoundEffect.WITCHERY_MOB_BABA_LIVING).playAtPlayer(world, p, 1, 1); // lolwhat?
		ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, p, 1.0, 2.0, 8);
		return stack;
	}

	public ItemStack randomObjectFromList(List<ItemStack> list) {
		int randomIndex = new Random().nextInt(list.size());
		return list.get(randomIndex).copy();
	}

	public List<ItemStack> getWitcheryItems(EntityPlayer p) {
		ItemStack ring = hasRing(p);
		if(ring == null) {
			return witcheryItems;
		}
		ItemStack stack = new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1);
		Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(stack, p, ItemBaubleResurrectionStone.getPlayer(ring), false, 1);
		// TODO check if there actually any cases when it isn't -1
		int index = witcheryItems.indexOf(stack);
		if(index == -1) {
			witcheryItems.add(stack);
			return witcheryItems;
		}
		witcheryItems.set(index, stack);
		return witcheryItems;
	}

	public List<ItemStack> getDeathlyHallowItems() {
		return deathlyHallowItems;
	}

	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			l.add(StatCollector.translateToLocal("dh.desc.trick5"));
			return;
		}
		l.add(StatCollector.translateToLocal("dh.desc.trick1"));
		l.add(StatCollector.translateToLocal("dh.desc.trick2"));
		l.add(StatCollector.translateToLocal("dh.desc.trick3"));
		l.add(StatCollector.translateToLocal("dh.desc.trick4"));
	}

	public ItemStack hasRing(EntityPlayer player) {
		IInventory baubles = BaublesApi.getBaubles(player);
		for(int i = 0; i < baubles.getSizeInventory(); i++) {
			ItemStack stack = baubles.getStackInSlot(i);
			if(stack != null && stack.getItem() == DHItems.resurrectionStone) {
				return stack;
			}
		}
		return null;
	}

	public static void initList() {
		deathlyHallowItems.add(new ItemStack(DHItems.invisibilityMantle));
		deathlyHallowItems.add(new ItemStack(DHItems.elderWand));
		deathlyHallowItems.add(new ItemStack(DHItems.resurrectionStone));
		deathlyHallowItems.add(new ItemStack(DHItems.itemLogo));
		deathlyHallowItems.add(new ItemStack(DHItems.elfToken));
		deathlyHallowItems.add(new ItemStack(DHItems.bertieBots));
		deathlyHallowItems.add(new ItemStack(DHItems.gastronomicTemptation));
		deathlyHallowItems.add(new ItemStack(DHItems.soupWithSawdust));
		deathlyHallowItems.add(new ItemStack(DHItems.viscousSecretions));
		deathlyHallowItems.add(new ItemStack(DHItems.hobgoblinChains));
		deathlyHallowItems.add(new ItemStack(DHItems.deadlyPrism));
		deathlyHallowItems.add(new ItemStack(DHItems.hobgoblinSoul));
		deathlyHallowItems.add(new ItemStack(DHItems.nimbus));
		deathlyHallowItems.add(new ItemStack(DHItems.deathShard));
		deathlyHallowItems.add(new ItemStack(DHItems.tarotCards));
		deathlyHallowItems.add(new ItemStack(DHItems.monsterBook));
		deathlyHallowItems.add(new ItemStack(DHItems.trickOrTreat));
		deathlyHallowItems.add(new ItemStack(DHItems.lightningInBag));
		if(DHIntegration.thaumcraft) {
			deathlyHallowItems.add(new ItemStack(DHItems.inferioisMutandis));
		}
		for(ItemGeneral.SubItem bottom: Witchery.Items.GENERIC.subItems) {
			if(bottom == null) {
				continue;
			}
			witcheryItems.add(bottom.createStack());
		}
		for(ItemVanillaPotion.SubItem submissive: Witchery.Items.POTIONS.subItems) {
			if(submissive == null) {
				continue;
			}
			witcheryItems.add(submissive.createStack());
		}
	}
}
