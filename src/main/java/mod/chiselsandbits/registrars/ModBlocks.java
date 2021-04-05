package mod.chiselsandbits.registrars;

import com.google.common.collect.Maps;
import mod.chiselsandbits.bitstorage.BlockBitStorage;
import mod.chiselsandbits.bitstorage.ItemBlockBitStorage;
import mod.chiselsandbits.bitstorage.ItemStackSpecialRendererBitStorage;
import mod.chiselsandbits.block.ChiseledBlock;
import mod.chiselsandbits.legacy.chiseledblock.BlockBitInfo;
import mod.chiselsandbits.legacy.chiseledblock.BlockChiseled;
import mod.chiselsandbits.legacy.chiseledblock.ItemBlockChiseled;
import mod.chiselsandbits.legacy.chiseledblock.MaterialType;
import mod.chiselsandbits.core.ChiselsAndBits;
import mod.chiselsandbits.printer.ChiselPrinterBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static mod.chiselsandbits.registrars.ModItemGroups.CHISELS_AND_BITS;

public final class ModBlocks
{

    private static final DeferredRegister<Block> BLOCK_REGISTRAR = DeferredRegister.create(ForgeRegistries.BLOCKS, ChiselsAndBits.MODID);
    private static final DeferredRegister<Item> ITEM_REGISTRAR = DeferredRegister.create(ForgeRegistries.ITEMS, ChiselsAndBits.MODID);

    public static final Map<Material, RegistryObject<ChiseledBlock>>     MATERIAL_TO_BLOCK_CONVERSIONS = Maps.newHashMap();
    public static final Map<Material, RegistryObject<ChiseledBlockItem>> MATERIAL_TO_ITEM_CONVERSIONS  = Maps.newHashMap();

    public static final RegistryObject<BlockBitStorage> BIT_STORAGE_BLOCK = BLOCK_REGISTRAR.register("bit_storage", () -> new BlockBitStorage(AbstractBlock.Properties.create(Material.IRON)
                                                                                                                                    .hardnessAndResistance(1.5F, 6.0F)
                                                                                                                                    .harvestTool(ToolType.AXE)
                                                                                                                                    .harvestLevel(1)
                                                                                                                                    .setRequiresTool()
                                                                                                                                    .variableOpacity()
                                                                                                                                    .notSolid()
                                                                                                                                    .setAllowsSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
                                                                                                                                    .setOpaque((p_test_1_, p_test_2_, p_test_3_) -> false)
                                                                                                                                    .setSuffocates((p_test_1_, p_test_2_, p_test_3_) -> false)
                                                                                                                                    .setBlocksVision((p_test_1_, p_test_2_, p_test_3_) -> false)));

    public static final RegistryObject<BlockItem>          BIT_STORAGE_BLOCK_ITEM = ITEM_REGISTRAR.register("bit_storage", () -> new ItemBlockBitStorage(BIT_STORAGE_BLOCK.get(), new Item.Properties()
                                                                                                                                                               .group(CHISELS_AND_BITS)
                                                                                                                                                               .setISTER(() -> ItemStackSpecialRendererBitStorage::new)));
    public static final RegistryObject<ChiselPrinterBlock> CHISEL_PRINTER_BLOCK   = BLOCK_REGISTRAR.register("chisel_printer", () -> new ChiselPrinterBlock(AbstractBlock.Properties.create(Material.ROCK)
      .hardnessAndResistance(1.5f, 6f)
      .harvestLevel(1)
      .harvestTool(ToolType.PICKAXE)
      .notSolid()
      .setOpaque((p_test_1_, p_test_2_, p_test_3_) -> false)
      .setBlocksVision((p_test_1_, p_test_2_, p_test_3_) -> false)
    ));

    public static final RegistryObject<BlockItem> CHISEL_PRINTER_ITEM = ITEM_REGISTRAR.register("chisel_printer", () -> new BlockItem(ModBlocks.CHISEL_PRINTER_BLOCK.get(), new Item.Properties().group(CHISELS_AND_BITS)));

    public static final MaterialType[] VALID_CHISEL_MATERIALS = new MaterialType[] {
      new MaterialType( "wood", Material.WOOD ),
      new MaterialType( "rock", Material.ROCK ),
      new MaterialType( "iron", Material.IRON ),
      new MaterialType( "cloth", Material.CARPET ),
      new MaterialType( "ice", Material.ICE ),
      new MaterialType( "packed_ice", Material.PACKED_ICE ),
      new MaterialType( "clay", Material.CLAY ),
      new MaterialType( "glass", Material.GLASS ),
      new MaterialType( "sand", Material.SAND ),
      new MaterialType( "ground", Material.EARTH ),
      new MaterialType( "grass", Material.EARTH ),
      new MaterialType( "snow", Material.SNOW_BLOCK ),
      new MaterialType( "fluid", Material.WATER ),
      new MaterialType( "leaves", Material.LEAVES ),
    };

    private ModBlocks()
    {
        throw new IllegalStateException("Tried to initialize: ModBlocks but this is a Utility class.");
    }

    public static void onModConstruction() {
        BLOCK_REGISTRAR.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEM_REGISTRAR.register(FMLJavaModLoadingContext.get().getModEventBus());

        Arrays.stream(VALID_CHISEL_MATERIALS).forEach(materialType -> {
            MATERIAL_TO_BLOCK_CONVERSIONS.put(
              materialType.getType(),
              BLOCK_REGISTRAR.register("chiseled" + materialType.getName(), () -> new BlockChiseled("chiseled_" + materialType.getName(), AbstractBlock.Properties
                                                                                                                                            .create(materialType.getType())
                                                                                                                                            .hardnessAndResistance(1.5f, 6f)
                                                                                                                                            .setBlocksVision((p_test_1_, p_test_2_, p_test_3_) -> false)
                                                                                                                                            .setOpaque((p_test_1_, p_test_2_, p_test_3_) -> false)
                                                                                                                                            .notSolid()))
            );
            MATERIAL_TO_ITEM_CONVERSIONS.put(
              materialType.getType(),
              ITEM_REGISTRAR.register("chiseled" + materialType.getName(), () -> new ItemBlockChiseled(MATERIAL_TO_BLOCK_CONVERSIONS.get(materialType.getType()).get(), new Item.Properties()))
            );
          }
        );
    }

    public static Map<Material, RegistryObject<ItemBlockChiseled>> getMaterialToItemConversions()
    {
        return MATERIAL_TO_ITEM_CONVERSIONS;
    }

    public static Map<Material, RegistryObject<BlockChiseled>> getMaterialToBlockConversions()
    {
        return MATERIAL_TO_BLOCK_CONVERSIONS;
    }

    public static MaterialType[] getValidChiselMaterials()
    {
        return VALID_CHISEL_MATERIALS;
    }

    @Nullable
    public static BlockState getChiseledDefaultState() {
        final Iterator<RegistryObject<BlockChiseled>> blockIterator = getMaterialToBlockConversions().values().iterator();
        if (blockIterator.hasNext())
            return blockIterator.next().get().getDefaultState();

        return null;
    }

    public static BlockChiseled convertGivenStateToChiseledBlock(
      final BlockState state )
    {
        final Fluid f = BlockBitInfo.getFluidFromBlock( state.getBlock() );
        return convertGivenMaterialToChiseledBlock(f != null ? Material.WATER : state.getMaterial());
    }

    public static BlockChiseled convertGivenMaterialToChiseledBlock(
      final Material material
    ) {
        final RegistryObject<BlockChiseled> materialBlock = getMaterialToBlockConversions().get( material );
        return materialBlock != null ? materialBlock.get() : convertGivenMaterialToChiseledBlock(Material.ROCK);
    }

    public static RegistryObject<BlockChiseled> convertGivenStateToChiseledRegistryBlock(
      final BlockState state )
    {
        final Fluid f = BlockBitInfo.getFluidFromBlock( state.getBlock() );
        return convertGivenMaterialToChiseledRegistryBlock(f != null ? Material.WATER : state.getMaterial());
    }

    public static RegistryObject<BlockChiseled> convertGivenMaterialToChiseledRegistryBlock(
      final Material material
    ) {
        final RegistryObject<BlockChiseled> materialBlock = getMaterialToBlockConversions().get( material );
        return materialBlock != null ? materialBlock : convertGivenMaterialToChiseledRegistryBlock(Material.ROCK);
    }

    public static boolean convertMaterialTo(
      final Material source,
      final Material target
    ) {
        final RegistryObject<BlockChiseled> sourceRegisteredObject = convertGivenMaterialToChiseledRegistryBlock(source);
        return getMaterialToBlockConversions().put(target, sourceRegisteredObject) != null;
    }
}
