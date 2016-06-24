package com.golems.entity.ai;

import com.golems.entity.GolemBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIPlaceRandomBlocks extends EntityAIBase
{
	public final GolemBase golem;
	public final int tickDelay;
	public final IBlockState[] plantables;
	public final Block[] plantSupports;
	public final boolean checkSupports;
	public final boolean canExecute;
	
	public EntityAIPlaceRandomBlocks(GolemBase golemBase, int ticksBetweenPlanting, IBlockState[] plants, Block[] soils, boolean configAllows)
	{
		this.setMutexBits(8);
		this.golem = golemBase;
		this.tickDelay = ticksBetweenPlanting;
		this.plantables = plants;
		this.plantSupports = soils;
		this.canExecute = configAllows;
		this.checkSupports = (soils != null);
	}
	
	public EntityAIPlaceRandomBlocks(GolemBase golemBase, int ticksBetweenPlanting, IBlockState[] plants, boolean configAllows)
	{
		this(golemBase, ticksBetweenPlanting, plants, null, configAllows);
	}
	
	@Override
	public boolean shouldExecute() 
	{
		return canExecute && golem.worldObj.rand.nextInt(tickDelay) == 0;
	}
	
	@Override
	public void startExecuting()
	{
		int x = MathHelper.floor_double(golem.posX);
		int y = MathHelper.floor_double(golem.posY - 0.20000000298023224D - (double)golem.getYOffset());
		int z = MathHelper.floor_double(golem.posZ);
		BlockPos below = new BlockPos(x, y, z);
		Block blockBelow = golem.worldObj.getBlockState(below).getBlock();
		
		if(golem.worldObj.isAirBlock(below.up(1)) && isPlantSupport(golem.worldObj, below))
		{
			setToPlant(golem.worldObj, below.up(1));
			return;
		}
	}
	
	@Override
	public boolean continueExecuting()
	{
		return false;
	}
	
	public boolean setToPlant(World world, BlockPos pos)
	{
		IBlockState state = this.plantables[world.rand.nextInt(this.plantables.length)];
		return world.setBlockState(pos, state, 2);	
	}
	
	public boolean isPlantSupport(World world, BlockPos pos)
	{
		if(!this.checkSupports) return true;
		
		Block at = world.getBlockState(pos).getBlock();
		if(this.plantSupports != null && this.plantSupports.length > 0)
		{
			for(Block b : this.plantSupports)
			{
				if(at == b) return true;
			}
		}
		
		return false;
	}
}