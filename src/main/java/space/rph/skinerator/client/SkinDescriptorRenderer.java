package space.rph.skinerator.client;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SkinDescriptorRenderer extends WPlainPanel {
    public PlayerHeadRenderer headRenderer;
    public WLabel name;
    public WLabel uuid;
    public WButton viewJson;
    public WButton viewSkin;
    public WLabel type;
    public WButton ignore;
    public WButton copyNbt;
    public String jsonPayload;

    public String skinUrl;

    public SkinDescriptorRenderer() {
        this.setSize(15*18, 3*18);

        name = new WLabel(Text.literal("name"));
        name.setVerticalAlignment(VerticalAlignment.CENTER);
        this.add(name, 36, 0, 13*18, 18);

        uuid = new WLabel(Text.literal("uuid"));
        uuid.setVerticalAlignment(VerticalAlignment.CENTER);
        this.add(uuid, 36, 18, 13*18, 18);

        type = new WLabel(Text.literal("normal"));
        type.setVerticalAlignment(VerticalAlignment.CENTER);
        type.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.add(type, 0, 36, 2*18, 18);

        viewJson = new WButton(Text.literal("Copy JSON"));
        this.add(viewJson, 36, 36, 4*18, 18);

        viewJson.setOnClick(() -> {
            MinecraftClient.getInstance().keyboard.setClipboard(new String(Base64.getDecoder().decode(this.jsonPayload), StandardCharsets.UTF_8));
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.literal("Copied payload to clipboard."));
        });

        viewSkin = new WButton(Text.literal("View Skin"));

        viewSkin.setOnClick(() -> {
            if (this.skinUrl == null) {
                MinecraftClient.getInstance().player.sendMessage(Text.literal("Could not locate URL, sorry."));
            } else {
                Util.getOperatingSystem().open(this.skinUrl);
            }
        });
        this.add(viewSkin, 108, 36, 3*18, 18);

        ignore = new WButton(Text.literal("X"));
        this.add(ignore, 162, 36, 18, 18);
        ignore.setOnClick(() -> {
            SkineratorClient.ignoredSkins.add(this.jsonPayload);
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new CollectedSkinsGui()));
        });

        copyNbt = new WButton(Text.literal("Copy sNBT"));
        this.add(copyNbt, 180, 36, 3*18, 18);
        copyNbt.setOnClick(() -> {
            MinecraftClient.getInstance().keyboard.setClipboard(this.headRenderer.getSNbtForItemStack());
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.literal("Copied sNBT for skull to clipboard."));
        });
    }
}
