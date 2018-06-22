package gregtech.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

public class BlockMetalCasing extends VariantBlock<BlockMetalCasing.MetalCasingType> {

    public BlockMetalCasing() {
        super(Material.IRON);
        setUnlocalizedName("metal_casing");
        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("wrench", 2);
        setDefaultState(getState(MetalCasingType.BRONZE_BRICKS));
    }

    public enum MetalCasingType implements IStringSerializable {

        BRONZE_BRICKS("bronze_bricks"),
        PRIMITIVE_BRICKS("primitive_bricks"),
        INVAR_HEATPROOF("invar_heatproof"),
        ALUMINIUM_FROSTPROOF("aluminium_frostproof"),
        STEEL_SOLID("steel_solid"),
        STAINLESS_CLEAN("stainless_clean"),
        TITANIUM_STABLE("titanium_stable"),
        TUNGSTENSTEEL_ROBUST("tungstensteel_robust");

        private final String name;

        MetalCasingType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

    }
    
}
