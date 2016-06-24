package com.golems.renders;

import com.golems.entity.GolemBase;
import com.golems.entity.GolemColorized;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * RenderColoredGolem is the same as RenderGolem but with casting to GolemColorized instead of GolemBase
 */
public class RenderColoredGolem extends RenderLiving<GolemColorized> 
{
	private static final ResourceLocation fallbackTexture = GolemBase.makeGolemTexture("clay");
	private ResourceLocation texture;
	
	public RenderColoredGolem(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelGolem(), 0.5F);
	}
	
	public void doRender(GolemColorized golem, double x, double y, double z, float f0, float f1)
	{
		long color = golem.getColor();
		if((color & -67108864) == 0) 
		{
			color |= -16777216;
		}

		float colorRed = (float) (color >> 16 & 255) / 255.0F;
		float colorGreen = (float) (color >> 8 & 255) / 255.0F;
		float colorBlue = (float) (color & 255) / 255.0F;
		float colorAlpha = (float) (color >> 24 & 255) / 255.0F;

		// debug:
		//if(golem.ticksExisted % 20 == 0) System.out.print("golem.getColor() returns " + golem.getColor() + "; colorRed=" + colorRed + "; colorGreen=" + colorGreen + "; colorBlue=" + colorBlue + "\n");

		// render first pass of golem texture (usually eyes and other opaque, pre-colored features)
		if(golem.hasBase())
		{
			this.texture = golem.getTextureBase();
			if(this.texture != null)
			{
				super.doRender(golem, x, y, z, f0, f1);
			}
		}

		// prepare to render the complicated layer
		GlStateManager.pushMatrix();
		// enable transparency if needed
		GlStateManager.color(colorRed, colorGreen, colorBlue, colorAlpha);
		if(golem.hasTransparency())
		{
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);       
		}

		// render second pass of golem texture, enabling colorizing and transparency if specified
		this.texture = golem.getTextureToColor();
		if(this.texture != null)
		{
			super.doRender(golem, x, y, z, f0, f1);
		}

		// return GL11 settings to normal
		if(golem.hasTransparency())
		{
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
		GlStateManager.popMatrix();   
	}

	@Override
	protected void rotateCorpse(GolemColorized golem, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		super.rotateCorpse(golem, p_77043_2_, p_77043_3_, partialTicks);

		if ((double)golem.limbSwingAmount >= 0.01D)
		{
			float f = 13.0F;
			float f1 = golem.limbSwing - golem.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
			float f2 = (Math.abs(f1 % f - f * 0.5F) - f * 0.25F) / (f * 0.25F);
			GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(GolemColorized golem)
	{
		return this.texture != null ? this.texture : this.fallbackTexture;
	}
}