package getfluxed.tanks.client.render;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RenderTank extends TileEntitySpecialRenderer {
    private Random random = new Random();
    private Minecraft mc = Minecraft.getMinecraft();
    private final float size = 0.0625f;
    private ModelTank model = new ModelTank();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile instanceof TileEntityFluidTank) {
            renderTile((TileEntityFluidTank) tile, x, y, z);
        }
    }

    public void renderTile(TileEntityFluidTank tile, double x, double y, double z) {

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.50f, (float) z + 0.5f);
        GL11.glRotatef(180f, 1, 0, 0);
        GL11.glRotatef(tile.getBlockMetadata() * 90, 0, 1, 0);
        mc.getTextureManager().bindTexture(new ResourceLocation("tanks", "textures/models/modelTank.png"));
        GL11.glColor4d(0.7, 0.7, 0.7, 1);
        model.render(size);
        GL11.glPopMatrix();
        if (tile.tank.getFluid() != null && tile.tank.getFluidAmount() > 0) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glTranslatef((float) x, (float) y + 1f, (float) z + 1);
            GL11.glRotatef(180f, 1f, 0f, 0f);
            FluidStack liquid = tile.tank.getFluid();
            liquid.amount = tile.tank.getFluidAmount();
            renderFluid(liquid, new BlockPos(x, y, z));
            GL11.glPopMatrix();

        }
    }

    public static void renderFluid(FluidStack stack, BlockPos pos) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1, 1, 1, 1);
        FluidStack fluidStack = stack;
        final Fluid fluid = fluidStack.getFluid();

        ResourceLocation textureRL = fluid.getStill();
        TextureAtlasSprite texture = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().getAtlasSprite(textureRL.getResourceDomain() + ":" + textureRL.getResourcePath());// getgetItemModel(new
                                                                                                                                                                                                                       // ItemStack(Blocks.stained_glass)).getParticleTexture();

        // TextureAtlasSprite texture = TextureAtlasSprite.
        final int color;

        if (texture != null) {
            TextureUtils.bindTextureToClient(getFluidSheet(fluid));
            color = fluid.getColor(fluidStack);
        } else {
            TextureUtils.bindDefaultTerrainTexture();
            // texture = Minecraft.getMinecraft().getTextureMapBlocks().get;
            color = 0xFFFFFFFF;
        }

        WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
        double liquid = stack.amount + 0.0;
        double maxLiquid = 32000.0;
        double height = (liquid / maxLiquid) * 0.76;
        GL11.glRotated(180f, 1, 0, 0);
        TextureUtils.renderFluidCuboid(fluidStack, pos, 0, -0.6, -1, 0.2, 0, 0.2, 1 - 0.2, 0.2 + height, 1 - 0.2);
        // final double se = height;
        // final double ne = height;
        // final double sw = height;
        // final double nw = height;
        //
        // final double center = height;
        // final double uMin = texture.getMinU();
        // final double uMax = texture.getMaxU();
        // final double vMin = texture.getMinV();
        // final double vMax = texture.getMaxV();
        //
        // final double vHeight = vMax - vMin;
        //
        // final float r = (color >> 16 & 0xFF) / 255.0F;
        // final float g = (color >> 8 & 0xFF) / 255.0F;
        // final float b = (color & 0xFF) / 255.0F;
        // t.begin(7, DefaultVertexFormats.POSITION_TEX);
        //// t.color(r, g, b, 1);
        //// t.putBrightness4(1, 1, 1, 1);// (0xF000F0);
        // t.pos(-0.8, 0, 0).tex(uMax, vMin);
        // t.pos(0, 0, 0).tex(uMin, vMin);
        // t.pos(0, nw, 0).tex(uMin, vMin + (vHeight * nw));
        // t.pos(-0.8, ne, 0).tex(uMax, vMin + (vHeight * ne));
        //
        // t.pos(-0.8, 0, -0.8).tex(uMin, vMin);
        // t.pos(-0.8, se, -0.8).tex(uMin, vMin + (vHeight * se));
        // t.pos(0, sw, -0.8).tex(uMax, vMin + (vHeight * sw));
        // t.pos(0, 0, -0.8).tex(uMax, vMin);
        //
        // t.pos(-0.8, 0, 0).tex(uMin, vMin);
        // t.pos(-0.8, ne, 0).tex(uMin, vMin + (vHeight * ne));
        // t.pos(-0.8, se, -0.8).tex(uMax, vMin + (vHeight * se));
        // t.pos(-0.8, 0, -0.8).tex(uMax, vMin);
        //
        // t.pos(0, 0, -0.8).tex(uMin, vMin);
        // t.pos(0, sw, -0.8).tex(uMin, vMin + (vHeight * sw));
        // t.pos(0, nw, 0).tex(uMax, vMin + (vHeight * nw));
        // t.pos(0, 0, 0).tex(uMax, vMin);
        //
        // final double uMid = (uMax + uMin) / 2;
        // final double vMid = (vMax + vMin) / 2;
        //
        // t.pos(-0.8, center, -0.8).tex( uMid, vMid);
        // t.pos(-0.8, se, -0.8).tex(uMax, vMin);
        // t.pos(-0.8, ne, 0).tex(uMin, vMin);
        // t.pos(0, nw, 0).tex(uMin, vMax);
        //
        // t.pos(0, sw, -0.8).tex(uMax, vMax);
        // t.pos(-0.8, se, -0.8).tex(uMax, vMin);
        // t.pos(-0.8, center, -0.8).tex(uMid, vMid);
        // t.pos(0, nw, 0).tex(uMin, vMax);
        //
        // t.pos(-0.8, 0, 0).tex(uMax, vMin);
        // t.pos(-0.8, 0, -0.8).tex(uMin, vMin);
        // t.pos(0, 0, -0.8).tex(uMin, vMax);
        // t.pos(0, 0, 0).tex(uMax, vMax);
        // Tessellator.getInstance().draw();
        // GL11.glEnable(GL11.GL_LIGHTING);
    }

    public static ResourceLocation getFluidSheet(FluidStack liquid) {
        if (liquid == null)
            return TextureMap.locationBlocksTexture;
        return getFluidSheet(liquid.getFluid());
    }

    /**
     * @param liquid
     */
    public static ResourceLocation getFluidSheet(Fluid liquid) {
        return TextureMap.locationBlocksTexture;
    }

}
