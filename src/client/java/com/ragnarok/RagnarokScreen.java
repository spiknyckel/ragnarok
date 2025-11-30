package com.ragnarok;

import com.ragnarok.config.AuthConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class RagnarokScreen extends Screen {
    private TextFieldWidget authString;
    private TextFieldWidget authServer;
    private TextFieldWidget username;


    private ButtonWidget saveButton;


    public RagnarokScreen() {
        super(Text.literal("Ragnarök Config"));
    }



    @Override
    protected void init() {
        this.username = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 66, 200, 20, Text.literal("UUID"));
        this.username.setMaxLength(36);
        this.username.setText(AuthConfig.getUsername());
        this.authString = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 66 + 37, 200, 20, Text.literal("Auth String"));
        this.authString.setMaxLength(128);
        this.authString.setText(AuthConfig.getAuthString());
        this.addSelectableChild(authString);
        this.authServer = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 66 + 37 * 2, 200, 20, Text.literal("Auth String"));
        this.authServer.setText(AuthConfig.getAuthServer());
        this.addSelectableChild(authServer);
        this.addSelectableChild(username);
        this.saveButton = this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Save"), button -> this.saveAndClose())
                        .dimensions(this.width / 2 - 100, 127 + 37, 200, 20)
                        .build()
        );
    }

    void saveAndClose() {
        AuthConfig.setUsername(username.getText());
        AuthConfig.setAuthString(authString.getText());
        AuthConfig.setAuthServer(authServer.getText());
        assert client != null;
        SessionExt session = (SessionExt) client.getSession();
        session.updateSession(AuthConfig.getUsername());
        close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawTextWithShadow(this.textRenderer, "Username", this.width / 2 - 100 + 1, 53, Colors.LIGHT_GRAY);
        context.drawTextWithShadow(this.textRenderer, "Auth String (DO NOT SHARE!)", this.width / 2 - 100 + 1, 53 + 37, Colors.LIGHT_RED);
        context.drawTextWithShadow(this.textRenderer, "Auth Server", this.width / 2 - 100 + 1, 53 + 37 * 2, Colors.LIGHT_GRAY);

        this.username.render(context, mouseX, mouseY, deltaTicks);
        this.authString.render(context, mouseX, mouseY, deltaTicks);
        this.authServer.render(context, mouseX, mouseY, deltaTicks);

    }
}
