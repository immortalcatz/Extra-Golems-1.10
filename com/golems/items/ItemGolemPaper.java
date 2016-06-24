package com.golems.items;

import java.util.List;

import com.golems.events.GolemPaperAddInfoEvent;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGolemPaper extends Item 
{
	public ItemGolemPaper()
	{
		super();
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	/**
     * allows items to add custom lines of information to the mouseover description
     */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> par3List, boolean advanced)
	{
		if(GuiScreen.isShiftKeyDown())
		{
			//String loreListIntro= TextFormatting.WHITE + trans("tooltip.in_order_of_attack") + ":";
			//par3List.add(loreListIntro);
			final String C = ", "; // C = comma
			String toAdd = trans("tile.blockDiamond.name") + C + trans("tile.blockEmerald.name") + C +
			trans("tile.obsidian.name") + C + trans("material.glass") + C + trans("tile.stainedGlass.name") + C +
			trans("tile.lightgem.name") + C + trans("tile.whiteStone.name") + C + trans("tile.quartzBlock.default.name") + C +
			trans("tile.blockGold.name") + C + trans("tile.prismarine.rough.name") + C + trans("tile.icePacked.name") + C +
			trans("tile.netherBrick.name") + C + trans("tile.blockIron.name") + C + trans("tile.seaLantern.name") + C +
			trans("tile.sandStone.name") + C + trans("tile.redSandStone.default.name") + C + trans("tile.clayHardened.name") + C +
			trans("tile.clayHardenedStained.name") + C + trans("material.shroom_block") + C + trans("material.log") + C +
			trans("tile.tnt.name") + C + trans("tile.blockCoal.name") + C + trans("tile.blockRedstone.name") + C +
			trans("tile.blockLapis.name") + C + trans("tile.slime.name") + C + trans("tile.clay.name") + C +
			trans("tile.bookshelf.name") + C + trans("tile.sponge.dry.name") + C + trans("tile.melon.name") + C + trans("tile.workbench.name") + C +
			trans("tile.cloth.name") + C + trans("tile.hayBlock.name") + C + trans("material.leaf_block") + C + trans("tile.magma.name") + C +
			trans("tile.netherWartBlock.name") + C + trans("tile.redNetherBrick.name") + C + trans("tile.boneBlock.name");		
			par3List.add(toAdd);
			GolemPaperAddInfoEvent event = new GolemPaperAddInfoEvent(stack, player, par3List, advanced);
			MinecraftForge.EVENT_BUS.post(event);
		}
		else
		{		
			String lorePressShift = 
					TextFormatting.GRAY + trans("tooltip.press") + " " + 
					TextFormatting.YELLOW + trans("tooltip.shift").toUpperCase() + " " + 
					TextFormatting.GRAY + trans("tooltip.for_golem_materials");
			par3List.add(lorePressShift);
		}
	}

	@SideOnly(Side.CLIENT)
	private String trans(String s, Object... p)
	{
		return I18n.format(s, p);
	}
}
