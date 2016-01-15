package getfluxed.tanks.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fluxedCore.handlers.ClientEventHandler;
import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RenderTank extends TileEntitySpecialRenderer<TileEntityFluidTank> {
    private Minecraft mc = Minecraft.getMinecraft();
    private final float size = 0.0625f;
    private ModelTank model = new ModelTank();

    @Override
    public void renderTileEntityAt(TileEntityFluidTank tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile instanceof TileEntityFluidTank) {
            renderTile((TileEntityFluidTank) tile, x, y, z);
        }
    }

    public void renderTile(TileEntityFluidTank tile, double x, double y, double z) {

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.50f, (float) z + 0.5f);
        GL11.glRotatef(180f, 1, 0, 0);
        GL11.glRotatef(tile.getBlockMetadata() * 90, 0, 1, 0);
        mc.getTextureManager().bindTexture(new ResourceLocation("tanks", "textures/models/modelTank.png"));
        Color col = new Color(tile.colour);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glBlendFunc(770, 771);
        if (tile.colour < 0) {
            GL11.glColor4d(ClientEventHandler.getRed(), ClientEventHandler.getGreen(), ClientEventHandler.getBlue(), 1);
        } else {
            GL11.glColor4d(col.getRed() / 255.0, col.getGreen() / 255.0, col.getBlue() / 255.0, 1);
        }
        model.render(size);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        if (tile.tank.getFluid() != null && tile.tank.getFluidAmount() > 0) {
            GL11.glPushMatrix();

            GL11.glTranslatef((float) x, (float) y + 1f, (float) z + 1);
            GL11.glRotatef(180f, 1f, 0f, 0f);
            FluidStack liquid = tile.tank.getFluid();
            liquid.amount = tile.tank.getFluidAmount();
            renderFluid(liquid, new BlockPos(x, y, z));
            GL11.glPopMatrix();

        }
    }

    public static void renderFluid(FluidStack stack, BlockPos pos) {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
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

        double liquid = stack.amount + 0.0;
        double maxLiquid = 32000.0;
        double height = (liquid / maxLiquid) * 0.76;
        GL11.glRotated(180f, 1, 0, 0);
        TextureUtils.renderFluidCuboid(fluidStack, pos, 0, -0.6, -1, 0.2, 0, 0.2, 1 - 0.2, 0.2 + height, 1 - 0.2);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

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
