package dev.important;

public class Module {
    public final String name;
    public boolean enabled;
    public Module(String name, boolean enabled) { this.name = name; this.enabled = enabled; }
    public void toggle() { enabled = !enabled; Config.save(); }
}
