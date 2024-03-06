package me.opkarol.opforts.forts.models.vault;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.opkarol.oporm.SerializableFieldOrm;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FortVault implements Serializable, SerializableFieldOrm {
    private final static Function<Integer, Integer> getMaxSizeFromLevelToFortItemsAmount = (level) -> level * 100;
    private final static Function<Integer, Integer> getMaxSizeFromLevelToVaultItemsAmount = (level) -> level * 500;
    private final Map<String, Integer> itemsInVaults;

    public FortVault(Map<String, Integer> itemsInVaults) {
        this.itemsInVaults = itemsInVaults;
    }

    public FortVault() {
        itemsInVaults = new HashMap<>();
    }

    public double getMaxSizeFromLevelToFortItemsAmount(int level){
        return getMaxSizeFromLevelToFortItemsAmount.apply(level);
    }

    public double getMaxSizeFromLevelToVaultItemsAmount(int level){
        return getMaxSizeFromLevelToVaultItemsAmount.apply(level);
    }

    public void addItem(ItemStack itemStack) {
        if (isValidItem(itemStack) && isEnoughSpaceInVault(itemStack)) {
            String item = itemStack.getType().name();
            int amount = itemStack.getAmount();
            itemsInVaults.put(item, itemsInVaults.getOrDefault(item, 0) + amount);
        }
    }

    public @Nullable ItemStack retrieveItem(@NotNull Material type) {
        int amount = 64;
        while (itemsInVaults.getOrDefault(type.name(), 0) < amount) {
            amount--;
        }

        int totalAmount = itemsInVaults.get(type.name()) - amount;
        if (totalAmount == 0) {
            itemsInVaults.remove(type.name());
        } else {
            itemsInVaults.put(type.name(), totalAmount);
        }
        return new ItemStack(type, amount);
    }

    public boolean isEnoughSpaceInVault(@NotNull ItemStack itemStack) {
        //TODO implement levels
        return getTotalItemsAmount() + itemStack.getAmount() <= getMaxSizeFromLevelToVaultItemsAmount(1);
    }

    public boolean isValidItem(@NotNull ItemStack itemStack) {
        return itemStack.getAmount() != 0 && itemStack.getEnchantments().size() == 0 && !itemStack.hasItemMeta() && itemStack.getType().isBlock();
    }

    public Map<String, Integer> getItemsInVaults() {
        return itemsInVaults.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public int getTotalItemsAmount() {
        return itemsInVaults.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int getItemsAmount(@NotNull Material material) {
        return itemsInVaults.getOrDefault(material.name(), 0);
    }

    public boolean hasEnoughItems(Material material, int amount) {
        return getItemsAmount(material) >= amount;
    }

    @Override
    public String serialize() {
        return new Gson().toJson(itemsInVaults);
    }

    @Override
    public Object deserialize(String s) {
        Type type = new TypeToken<Map<String, Integer>>(){}.getType();
        return new FortVault(new Gson().fromJson(s, type));
    }
}
