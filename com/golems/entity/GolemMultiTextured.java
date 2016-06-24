package com.golems.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class GolemMultiTextured extends GolemBase
{
	protected static final DataParameter<Byte> DATA_TEXTURE = EntityDataManager.<Byte>createKey(GolemMultiTextured.class, DataSerializers.BYTE);
	protected static final String NBT_TEXTURE = "GolemTextureData";
	
	/** ResourceLocation array - cannot exceed 256 in length **/
	public final ResourceLocation[] textures;
	
	/**
	 * This is a base class for golems that change texture when player interacts.
	 * Pass Strings that will be used to construct a ResourceLocation array of textures<br/>
	 * <b>Example call to this constructor:</b><br/><br/><code>
	 * public EntityExampleGolem(World world) {<br/>
	 *	super(world, 1.0F, Blocks.AIR, "example", new String[] {"one","two","three"});<br/>
	 * }</code><br/>
	 * This will initialize textures for <code>golem_example_one.png</code>, 
	 * <code>golem_example_two.png</code> and <code>golem_example_three.png</code>
	 **/
	public GolemMultiTextured(World world, float attack, ItemStack pick, String prefix, String[] textureNames)
	{
		super(world, attack, pick);
		this.textures = new ResourceLocation[textureNames.length];
		for(int n = 0, len = textureNames.length; n < len; n++)
		{
			String s = textureNames[n];
			this.textures[n] = GolemBase.makeGolemTexture(getModId(), prefix + "_" + s);
		}
	}
	
	@Override
	protected ResourceLocation applyTexture()
	{
		// apply TEMPORARY texture to avoid NPE. Actual texture is first applied in onLivingUpdate
		return this.makeGolemTexture("clay");
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(DATA_TEXTURE, Byte.valueOf((byte)0));
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		// only change texture when player has empty hand
		if(stack != null)
		{
			return super.processInteract(player, hand, stack);
		}
		else
		{
			int incremented = (this.getTextureNum() + 1) % this.textures.length;
			this.setTextureNum((byte)incremented);
			this.updateTexture();
			//this.writeEntityToNBT(this.getEntityData());
			player.swingArm(hand);
			return true;
		}
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		// since textureNum is correct, update texture AFTER loading from NBT and init
		if(this.ticksExisted == 2)
		{
			this.updateTexture();
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
    {
		super.writeEntityToNBT(nbt);
		nbt.setByte(NBT_TEXTURE, (byte)this.getTextureNum());
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.setTextureNum(nbt.getByte(NBT_TEXTURE));
		this.updateTexture();
	}
	
	@Override
	public boolean doesInteractChangeTexture()
	{
		return true;
	}
	
	public void setTextureNum(byte toSet)
	{
		this.getDataManager().set(DATA_TEXTURE, new Byte(toSet));
	}

	public int getTextureNum() 
	{
		return this.getDataManager().get(DATA_TEXTURE).byteValue();
	}
	
	public int getNumTextures()
	{
		return this.textures != null ? this.textures.length : null;
	}
	
	public int getMaxTextureNum()
	{
		return getNumTextures() - 1;
	}
	
	public ResourceLocation[] getTextureArray()
	{
		return this.textures;
	}
	
	public void updateTexture()
	{
		this.setTextureType(this.getTextureFromArray(this.getTextureNum()));
	}
	
	public ResourceLocation getTextureFromArray(int index)
	{
		return this.textures[index % this.textures.length];
	}
	
	public abstract String getModId();
}

