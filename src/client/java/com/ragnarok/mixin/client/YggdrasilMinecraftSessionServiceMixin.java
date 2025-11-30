package com.ragnarok.mixin.client;

import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.ragnarok.RagnarokRequest;
import com.ragnarok.config.AuthConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URL;
import java.util.UUID;

@Mixin(value = YggdrasilMinecraftSessionService.class, remap = false)
public abstract class YggdrasilMinecraftSessionServiceMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "joinServer", at = @At(value = "HEAD"), cancellable = true)
    private void joinServer(UUID profileId, String authenticationToken, String serverId, CallbackInfo ci) {
        if (!AuthConfig.getAuthString().isEmpty() && !AuthConfig.getAuthServer().isEmpty()) {
            RagnarokRequest request = new RagnarokRequest();
            request.accessToken = authenticationToken;
            request.selectedProfile = profileId;
            request.serverId = serverId;
            request.authString = AuthConfig.getAuthString();
            URL j = HttpAuthenticationService.constantURL(AuthConfig.getAuthServer() + "/session/minecraft/join");
            this.client.post(j, request, Void.class);
            ci.cancel();
        }
    }
}
