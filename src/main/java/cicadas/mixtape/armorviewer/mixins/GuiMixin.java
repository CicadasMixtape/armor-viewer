package cicadas.mixtape.armorviewer.mixins;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow protected abstract Player getCameraPlayer();
    @Shadow protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k);

    @Unique private final Identifier HOTBAR_IDENTIFIER = Identifier.fromNamespaceAndPath("armorviewer", "textures/hotbar.png");

    @Inject(method = "renderItemHotbar", at = @At("HEAD"))
    public void renderItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        var player = getCameraPlayer();

        if (player != null) {
            var width = guiGraphics.guiWidth() / 2;
            var arm = player.getMainArm().getOpposite();
            var offset = arm == HumanoidArm.LEFT ? 100 : -(100 + 82);

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HOTBAR_IDENTIFIER, width + offset, guiGraphics.guiHeight() - 22, 82, 22, 82, 22, 82, 22);

            for (int i = 0; i < 4; i++) {
                int x = (width + offset) + (i * 20) + 3;
                int y = guiGraphics.guiHeight() - 16 - 3;
                renderSlot(guiGraphics, x, y, deltaTracker, player, player.getInventory().getItem(39 - i), 1488 + i);
            }
        }
    }
}