package space.rph.skinerator.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.util.UUIDTypeAdapter;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import space.rph.skinerator.CustomSkinDescriptor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;

public class CollectedSkinsGui extends LightweightGuiDescription {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    public CollectedSkinsGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);

        WLabel label = new WLabel(Text.literal("Known skins"));
        root.setSize(270, 252);
        root.setInsets(Insets.ROOT_PANEL);

        Object[] keys = SkineratorClient.knownSkins.keySet().toArray();
        ArrayList<CustomSkinDescriptor> data = new ArrayList<>();
        for (Object key : keys) {
            if (!SkineratorClient.ignoredSkins.contains(key))
                data.add(SkineratorClient.knownSkins.get(key));
        }

        BiConsumer<CustomSkinDescriptor, SkinDescriptorRenderer> configurator = (CustomSkinDescriptor s, SkinDescriptorRenderer r) -> {
            r.headRenderer = new PlayerHeadRenderer(s);
            r.add(r.headRenderer, 0, 0);

            r.uuid.setText(Text.literal(s.getOwner().toString()));
            r.jsonPayload = s.getData();

            String json = new String(Base64.getDecoder().decode(s.getData()), StandardCharsets.UTF_8);
            MinecraftTexturesPayload p = gson.fromJson(json, MinecraftTexturesPayload.class);
            if (p != null) {
                r.name.setText(Text.literal(p.getProfileName()));
            } else {
                r.name.setText(Text.literal("=== THERE IS NO CUSTOM SKIN ==="));
            }

            if (p.getTextures().containsKey(MinecraftProfileTexture.Type.SKIN)) {
                r.skinUrl = p.getTextures().get(MinecraftProfileTexture.Type.SKIN).getUrl();
                if (p.getTextures().get(MinecraftProfileTexture.Type.SKIN).getMetadata("model") != null)
                    r.type.setText(Text.literal(p.getTextures().get(MinecraftProfileTexture.Type.SKIN).getMetadata("model")));
            }
        };

        WListPanel<CustomSkinDescriptor, SkinDescriptorRenderer> list = new WListPanel<>(data, SkinDescriptorRenderer::new, configurator);
        list.setListItemHeight(3*18);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);
        WButton clearKnown = new WButton(Text.literal("Clear known"));
        root.add(clearKnown, 7, 0, 4, 1);
        WButton clearIgnored = new WButton(Text.literal("Clear ignored"));
        root.add(clearIgnored, 11, 0, 4, 1);
        clearKnown.setOnClick(() -> {
            SkineratorClient.foundSkins = 0;
            SkineratorClient.knownSkins.clear();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new CollectedSkinsGui()));
        });
        clearIgnored.setOnClick(() -> {
            SkineratorClient.ignoredSkins.clear();
            MinecraftClient.getInstance().setScreen(new CottonClientScreen(new CollectedSkinsGui()));
        });
        root.add(label, 0, 0, 7, 1);
        root.add(list, 0, 1, 15, 14);
        root.validate(this);
    }
}
