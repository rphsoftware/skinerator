package space.rph.skinerator.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.texture.PlayerSkinProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import space.rph.skinerator.CustomSkinDescriptor;
import space.rph.skinerator.Skinerator;
import space.rph.skinerator.client.SkineratorClient;

import java.util.HashMap;
import java.util.HashSet;

@Mixin(PlayerSkinProvider.class)
public class PlayerSkinProviderMixin {

    @Inject(at = @At("HEAD"), method = "loadSkin(Lcom/mojang/authlib/GameProfile;Lnet/minecraft/client/texture/PlayerSkinProvider$SkinTextureAvailableCallback;Z)V")
    public void getTextures(
            GameProfile profile,
            PlayerSkinProvider.SkinTextureAvailableCallback callback,
            boolean requireSecure,
            CallbackInfo ci
    ) {
        if (profile != null) {
            profile.getProperties().forEach((s, property) -> {
                if (s.equals("textures")) {
                    if (!SkineratorClient.knownSkins.containsKey(property.getValue())) {
                        SkineratorClient.foundSkins++;

                        CustomSkinDescriptor descriptor = new CustomSkinDescriptor(
                                property.getSignature(),
                                profile.getId(),
                                property.getValue()
                        );

                        SkineratorClient.knownSkins.put(property.getValue(), descriptor);
                    }
                }
            });
        }
    }
}
