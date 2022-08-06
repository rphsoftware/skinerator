package space.rph.skinerator.client;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import space.rph.skinerator.CustomSkinDescriptor;

public class PlayerHeadRenderer extends WWidget {
    private ItemIcon icon;
    private NbtCompound nbtCompound;

    public PlayerHeadRenderer(CustomSkinDescriptor d) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        NbtCompound nbt = new NbtCompound();
        NbtCompound owner = new NbtCompound();
        NbtCompound properties = new NbtCompound();

        NbtCompound texture = new NbtCompound();
        NbtList textures = new NbtList();
        texture.putString("Value", d.getData());
        texture.putString("Signature", d.getSignature());
        textures.add(0, texture);
        properties.put("textures", textures);

        owner.put("Properties",properties);
        owner.putUuid("Id", d.getOwner());
        nbt.put("SkullOwner", owner);
        stack.setNbt(nbt);

        this.nbtCompound = nbt;
        this.icon = new ItemIcon(stack);
    }
    @Override
    public boolean canResize() {
        return false;
    }

    @Override
    public int getWidth() {
        return 36;
    }

    @Override
    public int getHeight() {
        return 36;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        this.icon.paint(matrices, x, y, 36);
    }

    public String getSNbtForItemStack() {
        return this.nbtCompound.toString();
    }
}
