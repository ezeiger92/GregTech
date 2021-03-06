package gregtech.common.multipart;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.TileMultipart;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.unification.material.type.Material;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.pipelike.cable.BlockCable;
import gregtech.common.pipelike.cable.Insulation;
import gregtech.common.pipelike.cable.WireProperties;
import gregtech.common.pipelike.cable.tile.CableEnergyContainer;
import gregtech.common.render.CableRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CableMultiPart extends PipeMultiPart<Insulation, WireProperties> implements ICapabilityProvider {

    private CableEnergyContainer energyContainer;

    CableMultiPart() {}

    public CableMultiPart(IBlockState blockState, TileEntity tile) {
        super(blockState, tile);
    }

    @Override
    public ResourceLocation getType() {
        return GTMultipartFactory.CABLE_PART_KEY;
    }


    public CableEnergyContainer getEnergyContainer() {
        if (energyContainer == null) {
            this.energyContainer = new CableEnergyContainer(this);
        }
        return energyContainer;
    }

    @Override
    public boolean hasCapability(Capability capability, EnumFacing facing) {
        return capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
            return (T) getEnergyContainer();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vector3 pos, BlockRenderLayer layer, CCRenderState ccrs) {
        TileMultipart tileMultipart = tile();
        ccrs.setBrightness(tileMultipart.getWorld(), tileMultipart.getPos());
        CableRenderer.INSTANCE.renderCableBlock(getPipeBlock().material, getPipeType(), insulationColor, ccrs,
            new IVertexOperation[] {new Translation(tileMultipart.getPos())},
            activeConnections & ~getBlockedConnections());
        return true;
    }

    @Override
    public void load(NBTTagCompound tag) {
        if(tag.hasKey("CableMaterial")) {
            String materialName = tag.getString("CableMaterial");
            BlockCable blockCable = MetaBlocks.CABLES.get(Material.MATERIAL_REGISTRY.getObject(materialName));
            //noinspection ConstantConditions
            tag.setString("PipeBlock", blockCable.getRegistryName().toString());
        }
        super.load(tag);
    }
}
