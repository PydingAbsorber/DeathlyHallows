package com.pyding.deathlyhallows.items;

import baubles.api.BaublesApi;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemVanillaPotion;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.common.handler.EventHandler;
import com.pyding.deathlyhallows.integration.Integration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.pyding.deathlyhallows.DeathHallowsMod.*;

public class TrickOrTreat extends Item {
    public static List witcheryItems = new ArrayList<>();
    public static List itemList = new ArrayList<>();

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        stack.splitStack(1);
        Object o;
        EventHandler eventHandler = new EventHandler();
        double chance = 0.98;
        if (eventHandler.hasDeathlyHallow(player))
            chance = 0.96;
        if (Math.random() < chance)
            o = randomObjectFromList(getWitcheryItems(player));
        else o = randomObjectFromList(getDeathlyHallowItems());
        ItemStack itemStack;
        if (o instanceof Item) {
            itemStack = new ItemStack((Item) o);
        } else if (o instanceof ItemGeneral.SubItem) {
            itemStack = ((ItemGeneral.SubItem) o).createStack();
        } else if (o instanceof ItemBase) {
            itemStack = new ItemStack((ItemBase) o);
        } else if (o instanceof Block) {
            itemStack = new ItemStack((Block) o);
        } else if (o instanceof ItemVanillaPotion) {
            itemStack = new ItemStack((ItemVanillaPotion) o);
        } else if (o instanceof ItemStack) {
            itemStack = (ItemStack) o;
        } else itemStack = new ItemStack(Items.rotten_flesh);
        SoundEffect effect1 = SoundEffect.WITCHERY_MOB_BABA_DEATH;
        SoundEffect effect2 = SoundEffect.WITCHERY_MOB_BABA_LIVING;
        if (!world.isRemote) {
            if (hasInventorySpace(player)) {
                player.inventory.addItemStackToInventory(itemStack);
            } else {
                player.entityDropItem(itemStack, 1);
            }
        }
        if (Math.random() < 0.5)
            effect1.playAtPlayer(world, player, 1, 1);
        else effect2.playAtPlayer(world, player, 1, 1);
        ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, player, 1.0, 2.0, 8);
        return super.onItemRightClick(stack, world, player);
    }

    public boolean hasInventorySpace(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
            if (player.inventory.getStackInSlot(i) == null) {
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
        if (hasRing(player) != null) {
            ResurrectionStone stone = new ResurrectionStone();
            ItemStack itemStack = new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1);
            Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(itemStack, player, stone.getPlayer(hasRing(player)), false, 1);
            if (!witcheryItems.contains(itemStack))
                witcheryItems.add(itemStack);
            else witcheryItems.set(witcheryItems.size(), itemStack);
        }
        return witcheryItems;
    }

    public List getDeathlyHallowItems() {
        return itemList;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        if (Integration.thaumcraft) {
            if (I18n.format("dh.util.language").equals("Ru")) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                    list.add("§6Даёт при открытии случайный предмет из Витчери 98% или Даров Смерти 2%");
                    list.add("§6Шанс удваивается, если ты обладаешь Даром Смерти");
                    list.add("§6Можно получить во время Хэллоуина при убийстве монстров и боссов. Период ивента с 18 октября по 8 ноября");
                    list.add("§6Падает с шансом 0.01% * макс хп монстра/босса");
                } else list.add("§6Нажми §eshift §6для дополнительной информации");
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                    list.add("§6Gives random item from Witchery 98% or Deathly Hallows 2% when opened");
                    list.add("§6Chance doubles if you have Deathly Hallow");
                    list.add("§6Can be obtained via Halloween event from killing monsters and bosses. Event period is from October 18 to November 8");
                    list.add("§6Drops with 0.01% * max hp of monster/boss chance");
                } else list.add("§6Press §eshift §6for additional information");
            }
        }
    }

    public ItemStack hasRing(EntityPlayer player) {
        for (int i = 1; i < 4; i++) {
            if (BaublesApi.getBaubles(player).getStackInSlot(i) != null) {
                ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(i);
                if (stack.getItem() == resurrectionStone) {
                    return stack;
                }
            }
        }
        return null;
    }

    public static void initList() {
        itemList.add(invisibilityMantle);
        itemList.add(elderWand);
        itemList.add(resurrectionStone);
        itemList.add(tabItem);
        itemList.add(creativeItem);
        itemList.add(bertieBots);
        itemList.add(gastronomicTemptation);
        itemList.add(soupWithSawdust);
        itemList.add(viscousSecretions);
        itemList.add(hobgoblinChains);
        itemList.add(deadlyPrism);
        itemList.add(hobgoblinSoul);
        itemList.add(nimbus);
        itemList.add(deathShard);
        itemList.add(cards);
        if (Integration.thaumcraft)
            itemList.add(inferioisMutandis);
        itemList.add(monsterBook);
        itemList.add(trickOrTreat);
        for (Object o : Witchery.Items.GENERIC.subItems) {
            witcheryItems.add(o);
        }
        for (Object o : Witchery.Items.POTIONS.subItems) {
            witcheryItems.add(o);
        }
    }
}
