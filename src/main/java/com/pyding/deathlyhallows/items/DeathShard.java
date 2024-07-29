package com.pyding.deathlyhallows.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;
import java.util.Random;

public class DeathShard extends Item {
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String randomName = generateRandomString(10);
		return getRainbowString(randomName);
	}


	private String getRainbowString(String text) {
		StringBuilder coloredText = new StringBuilder();
		EnumChatFormatting[] colors = EnumChatFormatting.values();

		for(char letter: text.toCharArray()) {
			EnumChatFormatting color;
			if(Math.random() > 0.5) {
				color = EnumChatFormatting.BLACK;
			}
			else {
				if(Math.random() > 0.5) {
					color = EnumChatFormatting.DARK_GRAY;
				}
				else {
					color = EnumChatFormatting.GRAY;
				}
			}
			coloredText.append(color).append(letter);
		}

		return coloredText.toString();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		list.add(StatCollector.translateToLocal("dh.desc.death"));
	}

	public static String generateRandomString(int length) {
		String characters = "≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗⋘⋙⋚⋛⋜⋝⋞⋟▲△▴▵▶▷▸▹►▻▼▽▾▿◀◁◂◃◄◅◆◇◈◉◊○◌◍◎●◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯☀☁☂☃☄★☆☇☈☉☊☋☌☍☎☏☐☑☒☓☖☗☚☛☜☝☞☟☠☡☢☣☤☥☦☧☨☩☪☫☬☭☮☯✁✂✃✄✆✇✈✉✌✍✎✏✐✑✒✓✔✕✖✗✘✙✚✛✜✝✞✟✠✡✢✣✤✥✦✧✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷✸✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋❍❏❐❑❒❖❡❢❣❤❥❦❧❘❙❚❛❜❝❞➱➲➳➴➵➶➷➸➘➙➚➛➜➝➞➟➠➡➢➣➤➥➦➧➨➩➪➫➬➭➮➯➉➔➹➺➻➼➽➾➿ൠൡ•‣‑‒–—―‖‗‘’‚‛“”„‟†‡‰′″‴‵‶‷‸‹›※‼‽‾‿⁀⁁⁂⁃⁄⁅⁆⁐⁑₰₱₲₳₴₵⃒⃓⃘⃙⃚⃑⃔⃕⃖⃗⃛⃜⃝⃞⃟⃠⃡⃢⃣℀℁ℂ℃℄℅℆ℇ℈℉ℊℋℌℍℎℏℐℑℒℓ℔ℕ№℗℘ℙℚℛℜℝ℞℟℠℡™℣ℤ℥Ω℧ℨ℩KÅℬℭ℮ℯℰℱℲℳℴ⅓⅔⅕⅖⅗⅘⅙⅚⅛⅜⅝⅞⅟ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅬℵℶℷℸℹ℻ⅅⅆⅇⅈⅉ⅍ⅎⅭⅮⅯⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻⅼⅽⅾⅿↀↁↂↄ←↑→↓↔↕↖↗↘↙↚↛↜↝↞↟↠↡↢↣↤↥↦↧↨↩↪↫↬↭↮↯↰↱↲↳↴↵↶↷↸↹↺↻↼↽↾↿⇀⇁⇂⇃⇄⇅⇆⇇⇈⇉⇊⇋⇌⇍⇎⇏⇐⇑⇒⇓⇔⇕⇖⇗⇘⇙⇚⇛⇜⇝⇞⇟⇠⇡⇢⇣⇤⇥⇦⇧⇨⇩⇪∀∁∂∃∄∅∆∇∈∉∊∋∌∍∎∏∐∑−∓∔∕∖∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟≠≡≢";

		Random random = new Random();
		StringBuilder result = new StringBuilder();

		for(int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			result.append(characters.charAt(randomIndex));
		}

		return result.toString();
	}
}
