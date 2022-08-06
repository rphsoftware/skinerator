package space.rph.skinerator.client;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import space.rph.skinerator.CustomSkinDescriptor;

import java.util.HashMap;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
public class SkineratorClient implements ClientModInitializer {
    public static int foundSkins = 0;
    private static KeyBinding keybind;
    public static HashMap<String, CustomSkinDescriptor> knownSkins = new HashMap<>();
    public static HashSet<String> ignoredSkins = new HashSet<>();
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new HudRenderCallback() {
            @Override
            public void onHudRender(MatrixStack matrixStack, float tickDelta) {
                TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                renderer.draw(matrixStack, String.format("Skins found: %d", foundSkins), 0, 0, 0xffffff);
                renderer.draw(matrixStack, String.format("Ignored skins: %d", ignoredSkins.size()), 0, 12, 0xffffff);
            }
        });

        keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.skinerator.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F13,
                "category.skinerator.gui"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keybind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new CollectedSkinsGui()));
            }
        });
    }
}
