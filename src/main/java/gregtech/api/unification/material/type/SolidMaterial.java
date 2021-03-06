package gregtech.api.unification.material.type;

import com.google.common.collect.ImmutableList;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantment;
import gregtech.api.GTValues;
import gregtech.api.enchants.EnchantmentData;
import gregtech.api.unification.Element;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.Optional.Method;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.unification.material.type.DustMaterial.MatFlags.GENERATE_PLATE;
import static gregtech.api.unification.material.type.SolidMaterial.MatFlags.*;
import static gregtech.api.util.GTUtility.createFlag;

@ZenClass("mods.gregtech.material.SolidMaterial")
@ZenRegister
public abstract class SolidMaterial extends DustMaterial {

    public static final class MatFlags {

        public static final long GENERATE_ROD = createFlag(20);
        public static final long GENERATE_GEAR = createFlag(21);
        public static final long GENERATE_LONG_ROD = createFlag(22);

        /**
         * If this Material is grindable with a simple Mortar
         */
        public static final long MORTAR_GRINDABLE = createFlag(24);

        static {
            Material.MatFlags.registerMaterialFlagsHolder(MatFlags.class, SolidMaterial.class);
        }

    }

    /**
     * Speed of tools made from this material
     * Default value is 1.0f
     */
    @ZenProperty
    public final float toolSpeed;

    /**
     * Durability of tools made from this material
     * Equal to 0 for materials that can't be used for tools
     */
    @ZenProperty
    public final int toolDurability;

    /**
     * Enchantment to be applied to tools made from this material
     */
    @ZenProperty
    public final List<EnchantmentData> toolEnchantments = new ArrayList<>();

    /**
     * Material specified here will be required as handle to make tool
     * from this material
     */
    @ZenProperty
    public SolidMaterial handleMaterial;

    /**
     * Macerating any item of this material will result material
     * specified in this field
     */
    @ZenProperty
    public DustMaterial macerateInto = this;

    public SolidMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags, Element element, float toolSpeed, int toolDurability) {
        super(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element);
        this.toolSpeed = toolSpeed;
        this.toolDurability = toolDurability;
        this.directSmelting = this;
    }

    @Override
    protected long verifyMaterialBits(long generationBits) {
        if((generationBits & GENERATE_GEAR) > 0) {
            generationBits |= GENERATE_PLATE;
            generationBits |= GENERATE_ROD;
        }
        if((generationBits & GENERATE_LONG_ROD) > 0) {
            generationBits |= GENERATE_ROD;
        }
        return super.verifyMaterialBits(generationBits);
    }

    public void setMaceratingInto(DustMaterial macerateInto) {
        this.macerateInto = macerateInto;
    }

    public void addEnchantmentForTools(Enchantment enchantment, int level) {
        toolEnchantments.add(new EnchantmentData(enchantment, level));
    }

    @ZenMethod("addToolEnchantment")
    @Method(modid = GTValues.MODID_CT)
    public void ctAddEnchantmentForTools(IEnchantment enchantment) {
        Enchantment enchantmentType = (Enchantment) enchantment.getDefinition().getInternal();
        toolEnchantments.add(new EnchantmentData(enchantmentType, enchantment.getLevel()));
    }

}
