package dev.important;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public final class Hud {
    private static final int BG = 0xB0101418;

    public static void render(DrawContext ctx, RenderTickCounter tick) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden || mc.currentScreen != null) return;
        TextRenderer tr = mc.textRenderer;
        int accent = Config.accent();

        if (Config.WATERMARK.enabled) {
            String t = "Important  |  " + mc.getCurrentFps() + " fps";
            int w = tr.getWidth(t) + 16;
            int x = (ctx.getScaledWindowWidth() - w) / 2;
            Draw.round(ctx, x, 4, x + w, 18, BG);
            ctx.fill(x + 1, 4, x + w - 1, 5, accent);
            ctx.drawText(tr, t, x + 8, 8, 0xFFFFFFFF, true);
        }

        if (Config.INFO.enabled) {
            int ping = 0;
            if (mc.getNetworkHandler() != null) {
                PlayerListEntry e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
                if (e != null) ping = e.getLatency();
            }
            String[] lines = {
                "XYZ " + mc.player.getBlockX() + " " + mc.player.getBlockY() + " " + mc.player.getBlockZ(),
                "Ping " + ping + " ms"
            };
            panel(ctx, tr, "Info", lines, 6, 6, accent);
        }

        if (Config.EFFECTS.enabled) {
            var effects = mc.player.getStatusEffects();
            if (!effects.isEmpty()) {
                String[] lines = effects.stream().map(Hud::fmt).toArray(String[]::new);
                panel(ctx, tr, "Effects", lines, 6, Config.INFO.enabled ? 48 : 6, accent);
            }
        }

        if (Config.ARMOR.enabled) {
            int x = ctx.getScaledWindowWidth() / 2 + 10;
            int y = ctx.getScaledWindowHeight() - 56;
            EquipmentSlot[] s = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            for (int i = 0; i < 4; i++) {
                ItemStack st = mc.player.getEquippedStack(s[i]);
                if (!st.isEmpty()) {
                    ctx.drawItem(st, x + i * 18, y);
                    ctx.drawStackOverlay(tr, st, x + i * 18, y);
                }
            }
        }

        if (Config.KEYSTROKES.enabled) {
            int cx = 40, y = ctx.getScaledWindowHeight() - 70;
            key(ctx, tr, "W", cx, y, mc.options.forwardKey.isPressed(), accent);
            key(ctx, tr, "A", cx - 20, y + 20, mc.options.leftKey.isPressed(), accent);
            key(ctx, tr, "S", cx, y + 20, mc.options.backKey.isPressed(), accent);
            key(ctx, tr, "D", cx + 20, y + 20, mc.options.rightKey.isPressed(), accent);
        }
    }

    private static String fmt(StatusEffectInstance i) {
        int s = i.getDuration() / 20;
        String time = i.isInfinite() ? "∞" : String.format("%d:%02d", s / 60, s % 60);
        return i.getEffectType().value().getName().getString() + " " + (i.getAmplifier() + 1) + "  " + time;
    }

    private static void panel(DrawContext c, TextRenderer tr, String title, String[] lines, int x, int y, int accent) {
        int w = tr.getWidth(title);
        for (String l : lines) w = Math.max(w, tr.getWidth(l));
        w += 14;
        int h = 16 + lines.length * 10;
        Draw.round(c, x, y, x + w, y + h, BG);
        c.fill(x + 1, y, x + w - 1, y + 1, accent);
        c.drawText(tr, title, x + 7, y + 4, accent, true);
        for (int i = 0; i < lines.length; i++) c.drawText(tr, lines[i], x + 7, y + 16 + i * 10 - 2, 0xFFE5E7EB, true);
    }

    private static void key(DrawContext c, TextRenderer tr, String k, int x, int y, boolean down, int accent) {
        Draw.round(c, x, y, x + 18, y + 18, down ? Draw.alpha(accent, 0xC0) : BG);
        c.drawText(tr, k, x + 9 - tr.getWidth(k) / 2, y + 5, 0xFFFFFFFF, true);
    }
}
