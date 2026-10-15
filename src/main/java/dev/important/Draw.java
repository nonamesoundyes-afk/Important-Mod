package dev.important;

import net.minecraft.client.gui.DrawContext;

public final class Draw {
    /** Прямокутник зі «зрізаними» кутами (імітація заокруглення). */
    public static void round(DrawContext c, int x1, int y1, int x2, int y2, int color) {
        c.fill(x1 + 1, y1, x2 - 1, y2, color);
        c.fill(x1, y1 + 1, x1 + 1, y2 - 1, color);
        c.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
    }
    public static int alpha(int color, int a) { return (a << 24) | (color & 0xFFFFFF); }
}
