package com.golems.entity;

import java.util.ArrayList;
import java.util.List;

import com.golems.entity.ai.EntityAIPlaceRandomBlocks;
import com.golems.main.Config;
import com.golems.util.WeightedItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityMelonGolem extends GolemBase 
{	
	public static final String ALLOW_SPECIAL = "Allow Special: Plant Flowers";
	public static final String FREQUENCY = "Flower Frequency";

	private IBlockState[] flowers;
	private final Block[] soils = {Blocks.DIRT, Blocks.GRASS, Blocks.MYCELIUM, Blocks.FARMLAND};

	public EntityMelonGolem(World world) 
	{
		super(world, Config.MELON.getBaseAttack(), Blocks.MELON_BLOCK);	
		this.setCanSwim(true);
		this.tasks.addTask(2, this.makeFlowerAI());
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();	
	}

	@Override
	protected ResourceLocation applyTexture()
	{
		return this.makeGolemTexture("melon");
	}

	@Override
	protected void applyAttributes() 
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.MELON.getMaxHealth());
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
	}

	@Override
	public void addGolemDrops(List<WeightedItem> dropList, boolean recentlyHit, int lootingLevel)
	{
		int size = 6 + this.rand.nextInt(6 + lootingLevel * 4);
		this.addDrop(dropList, new ItemStack(Items.MELON, size), 100);
		this.addDrop(dropList, Items.MELON_SEEDS, 0, 1, 6 + lootingLevel, 20 + lootingLevel * 10);
		this.addDrop(dropList, Items.SPECKLED_MELON, 0, 1, 1, 2 + lootingLevel * 10);
	}

	@Override
	public SoundEvent getGolemSound() 
	{
		return SoundEvents.BLOCK_STONE_STEP;
	}

	/** Create an EntityAIPlaceRandomBlocks **/
	protected EntityAIBase makeFlowerAI()
	{
		// init list and AI for planting flowers
		List<IBlockState> lFlowers = new ArrayList();
		for(EnumFlowerType e : BlockFlower.EnumFlowerType.values())
		{
			lFlowers.add(e.getBlockType().getBlock().getStateFromMeta(e.getMeta()));
		}
		for(BlockTallGrass.EnumType e : BlockTallGrass.EnumType.values())
		{
			lFlowers.add(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, e));
		}
		this.flowers = lFlowers.toArray(new IBlockState[lFlowers.size()]);
		// get other parameters for the AI
		int freq = Config.MELON.getInt(FREQUENCY);
		boolean allowed = Config.MELON.getBoolean(ALLOW_SPECIAL);
		return new EntityAIPlaceRandomBlocks(this, freq, flowers, soils, allowed);
	}
}
