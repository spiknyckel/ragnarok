package com.ragnarok.mixin.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ragnarok.SessionExt;
import com.ragnarok.UUIDHelper;
import com.ragnarok.config.AuthConfig;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

@Mixin(Session.class)
public abstract class SessionMixin implements SessionExt {
    @Shadow
    @Mutable
    private String username;

    @Shadow
    @Mutable
    private UUID uuid;

    public void updateSession(String name) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
                .build();

        Map<String, String> json;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.readValue(response.body(), Map.class);
        } catch (IOException | InterruptedException e) {
            return;
        }
        String id = json.get("id");
        uuid = UUIDHelper.fromDashless(id);
        username = name;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (!AuthConfig.username.isEmpty()) {
            updateSession(AuthConfig.username);
        } else {
            updateSession(username);
        }
    }

}
