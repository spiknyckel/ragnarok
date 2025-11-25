package com.ragnarok.mixin.client;

import com.ragnarok.RagnarokScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "addNormalWidgets")
    private void addAuthString(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Ragnarök"), button -> this.client.setScreen(new RagnarokScreen()))
                        .dimensions(this.width / 2 - 100 + 205, y - spacingY, 70, 20)
                        .build()
        );
    }
}
