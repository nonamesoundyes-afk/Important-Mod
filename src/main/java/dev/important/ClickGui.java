package dev.important;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;

public class ClickGui extends Screen {
    private static final int PW = 112, ROW = 16, HEAD = 20, GAP = 10;
    private static final int PANEL_BG = 0xE0101418;

    public ClickGui() { super(Text.literal("Important")); }

    @Override public boolean shouldPause() { return false; }

    private int baseX() { return (width - (PW * 2 + GAP)) / 2; }
    private int baseY() { return height / 2 - 90; }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        super.render(ctx, mx, my, delta); // фон/блюр екрана
        int accent = Config.accent();
        int x = baseX(), y = baseY();

        // --- Панель Visuals ---
        int h = HEAD + Config.MODULES.size() * ROW + 4;
        Draw.round(ctx, x, y, x + PW, y + h, PANEL_BG);
        ctx.drawText(textRenderer, "Visuals", x + 8, y + 7, 0xFFFFFFFF, true);
        ctx.fill(x + 6, y + HEAD - 2, x + PW - 6, y + HEAD - 1, 0x33FFFFFF);
        for (int i = 0; i < Config.MODULES.size(); i++) {
            Module m = Config.MODULES.get(i);
            int ry = y + HEAD + i * ROW;
            boolean hover = mx >= x && mx < x + PW && my >= ry && my < ry + ROW;
            if (hover) ctx.fill(x + 3, ry, x + PW - 3, ry + ROW, 0x22FFFFFF);
            ctx.drawText(textRenderer, m.enabled ? "✔" : " ", x + 8, ry + 4, accent, true);
            ctx.drawText(textRenderer, m.name, x + 22, ry + 4, m.enabled ? 0xFFFFFFFF : 0xFF9CA3AF, true);
        }

        // --- Панель Client ---
        int cx = x + PW + GAP;
        Draw.round(ctx, cx, y, cx + PW, y + HEAD + 2 * ROW + 4, PANEL_BG);
        ctx.drawText(textRenderer, "Client", cx + 8, y + 7, 0xFFFFFFFF, true);
        ctx.fill(cx + 6, y + HEAD - 2, cx + PW - 6, y + HEAD - 1, 0x33FFFFFF);
        int ty = y + HEAD;
        boolean hov = mx >= cx && mx < cx + PW && my >= ty && my < ty + ROW;
        if (hov) ctx.fill(cx + 3, ty, cx + PW - 3, ty + ROW, 0x22FFFFFF);
        ctx.drawText(textRenderer, "Theme", cx + 8, ty + 4, 0xFFFFFFFF, true);
        String tn = Config.THEME_NAMES[Config.theme];
        ctx.drawText(textRenderer, tn, cx + PW - 8 - textRenderer.getWidth(tn), ty + 4, accent, true);
        ctx.drawText(textRenderer, "Important I", cx + 8, ty + ROW + 4, 0xFF6B7280, false);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mx = click.x(), my = click.y();
        int x = baseX(), y = baseY();
        for (int i = 0; i < Config.MODULES.size(); i++) {
            int ry = y + HEAD + i * ROW;
            if (mx >= x && mx < x + PW && my >= ry && my < ry + ROW) { Config.MODULES.get(i).toggle(); return true; }
        }
        int cx = x + PW + GAP, ty = y + HEAD;
        if (mx >= cx && mx < cx + PW && my >= ty && my < ty + ROW) { Config.cycleTheme(); return true; }
        return super.mouseClicked(click, doubled);
    }
}
