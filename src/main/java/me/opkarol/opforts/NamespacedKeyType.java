package me.opkarol.opforts;

import me.opkarol.oplibrary.Plugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum NamespacedKeyType {
    FORTS,
    ;

    @Contract(" -> new")
    public @NotNull NamespacedKey get() {
        return new NamespacedKey(Plugin.getInstance(), name().toLowerCase());
    }
}
