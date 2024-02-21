package me.opkarol.opforts.characters.levelling;

import org.bukkit.inventory.ItemStack;

public record CharacterReward(CharacterReward.Type type, Object object) {

    public enum Type {
        MONEY("Money"),
        EMPTY("Empty"),
        ITEM("Item"),
        ;

        private final String name;
        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getInformation() {
            return switch (this) {
                case MONEY -> "$%s";
                case EMPTY -> "";
                case ITEM -> "%s %sx";
            };
        }

        public String getFormattedInformation(Object object) {
            return switch (this) {
                case MONEY -> "$" + object;
                case EMPTY -> "";
                case ITEM -> {
                    ItemStack item = (ItemStack) object;
                    yield item.getType() + " " + item.getAmount() + "x";
                }
            };
        }
    }
}
