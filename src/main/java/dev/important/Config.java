package dev.important;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
import java.util.*;

public final class Config {
    public static final List<Module> MODULES = new ArrayList<>();
    public static final Module WATERMARK = add("Watermark", true);
    public static final Module INFO      = add("Info", true);
    public static final Module EFFECTS   = add("Effects", true);
    public static final Module ARMOR     = add("Armor", true);
    public static final Module KEYSTROKES = add("Keystrokes", false);

    public static final String[] THEME_NAMES = {"Violet", "Cyan", "Green", "Red", "White"};
    public static final int[] THEME_COLORS = {0xFFA78BFA, 0xFF22D3EE, 0xFF4ADE80, 0xFFF87171, 0xFFE5E7EB};
    public static int theme = 0;

    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("important.json");

    private static Module add(String n, boolean d) { Module m = new Module(n, d); MODULES.add(m); return m; }
    public static int accent() { return THEME_COLORS[theme]; }
    public static void cycleTheme() { theme = (theme + 1) % THEME_COLORS.length; save(); }

    public static void load() {
        try {
            if (!Files.exists(FILE)) return;
            JsonObject o = JsonParser.parseString(Files.readString(FILE)).getAsJsonObject();
            for (Module m : MODULES) if (o.has(m.name)) m.enabled = o.get(m.name).getAsBoolean();
            if (o.has("theme")) theme = Math.floorMod(o.get("theme").getAsInt(), THEME_COLORS.length);
        } catch (Exception ignored) {}
    }

    public static void save() {
        try {
            JsonObject o = new JsonObject();
            for (Module m : MODULES) o.addProperty(m.name, m.enabled);
            o.addProperty("theme", theme);
            Files.writeString(FILE, new GsonBuilder().setPrettyPrinting().create().toJson(o));
        } catch (Exception ignored) {}
    }
}
