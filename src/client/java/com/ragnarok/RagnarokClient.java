package com.ragnarok;

import com.ragnarok.config.AuthConfig;
import net.fabricmc.api.ClientModInitializer;

public class RagnarokClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		AuthConfig.load();
	}
}