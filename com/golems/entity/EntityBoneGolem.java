package com.golems.entity;

import java.util.List;

import com.golems.main.Config;
import com.golems.util.WeightedItem;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityBoneGolem extends GolemBase 
{			
	public static final Block BONE = Blocks.field_189880_di;
	
	public EntityBoneGolem(World world) 
	{
		super(world, Config.BONE.getBaseAttack(), BONE);
		this.setCanTakeFallDamage(true);
	}
	
	protected ResourceLocation applyTexture()
	{
		return this.makeGolemTexture("bone");
	}
		
	@Override
	protected void applyAttributes() 
	{
	 	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.BONE.getMaxHealth());
	  	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
	}
	
	@Override
	public void addGolemDrops(List<WeightedItem> dropList, boolean recentlyHit, int lootingLevel)
	{
		this.addDrop(dropList, BONE, 0, lootingLevel + rand.nextInt(2), 4, 90);
	}
	
	@Override
	public SoundEvent getGolemSound() 
	{
		return SoundEvents.BLOCK_STONE_STEP;
	}
}
