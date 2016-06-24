package com.golems.entity;

import java.util.List;

import com.golems.blocks.BlockPowerProvider;
import com.golems.main.Config;
import com.golems.main.GolemItems;
import com.golems.util.WeightedItem;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRedstoneGolem extends GolemBase 
{	
	public static final String ALLOW_SPECIAL = "Allow Special: Redstone Power";
	public static final String POWER = "Redstone Power Level";

	protected boolean CAN_POWER;
	protected int powerOutput;
	protected int tickDelay;

	/** Default constructor for Redstone Golem **/
	public EntityRedstoneGolem(World world) 
	{
		this(world, Config.REDSTONE.getBaseAttack(), Blocks.REDSTONE_BLOCK, Config.REDSTONE.getInt(POWER), Config.REDSTONE.getBoolean(ALLOW_SPECIAL));
	}

	/**
	 * Flexible constructor to allow child classes to customize.
	 **/
	public EntityRedstoneGolem(World world, float attack, Block pick, int power, boolean CONFIG_ALLOWS_POWERING) 
	{
		super(world, attack, pick);
		this.setPowerOutput(power);
		this.CAN_POWER = CONFIG_ALLOWS_POWERING;
		this.tickDelay = 2;
	}

	/**
	 * Flexible constructor to allow child classes to customize.
	 **/
	public EntityRedstoneGolem(World world, float attack, int power, boolean CONFIG_ALLOWS_POWERING)
	{
		this(world, attack, GolemItems.golemHead, power, CONFIG_ALLOWS_POWERING);
	}

	@Override
	protected ResourceLocation applyTexture()
	{
		return this.makeGolemTexture("redstone");
	}

	@Override
	protected void applyAttributes() 
	{
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.REDSTONE.getMaxHealth());
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		// calling every other tick reduces lag by 50%
		if(CAN_POWER && (this.tickDelay <= 1 || this.ticksExisted % this.tickDelay == 0))
		{
			placePowerNearby();
		}
	}

	/** Finds air blocks nearby and replaces them with BlockMovingPowerSource **/
	protected boolean placePowerNearby() 
	{
		int numPlaced = 0;
		// power 3 layers at golem location
		for(int k = -1; k < 3; ++k)
		{	
			BlockPos at = this.getPosition().up(k);
			// if the block is air, make it a power block
			if(this.worldObj.isAirBlock(at))
			{
				this.worldObj.setBlockState(at, GolemItems.blockPowerSource.getDefaultState().withProperty(BlockPowerProvider.POWER, this.getPowerOutput(at)));
				numPlaced++;
			}
		}
		return numPlaced > 0;
	}

	public void setPowerOutput(int toSet)
	{
		this.powerOutput = toSet % 16;
	}

	/**
	 * Override this to check conditions and return correct power level
	 **/
	public int getPowerOutput(BlockPos toPlaceAt)
	{
		return this.powerOutput;
	}

	@Override
	public void addGolemDrops(List<WeightedItem> dropList, boolean recentlyHit, int lootingLevel)
	{
		int size = 8 + rand.nextInt(14 + lootingLevel * 4);
		this.addDrop(dropList, new ItemStack(Items.REDSTONE, size > 36 ? 36 : size), 100);
	}

	@Override
	public SoundEvent getGolemSound() 
	{
		return SoundEvents.BLOCK_STONE_STEP;
	}
}
