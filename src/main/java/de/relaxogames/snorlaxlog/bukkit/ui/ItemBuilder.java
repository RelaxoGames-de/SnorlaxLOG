package de.relaxogames.snorlaxlog.bukkit.ui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {
    private final ItemMeta itemMeta;
    private final ItemStack itemStack;

    /**
     * Constructs a new ItemBuilder object with the specified material.
     *
     * @param mat the material of the item
     */
    public ItemBuilder(Material mat) {
        itemStack = new ItemStack(mat);
        itemMeta = itemStack.getItemMeta();
    }

    /**
     * Sets the Displayname of an Item, the Display Name is the name shown to the
     * user
     *
     * @param s is the String that becomes the new Displayname
     */
    public ItemBuilder setDisplayname(String s) {
        itemMeta.setDisplayName(s);
        return this;
    }

    /**
     * Sets the Localized of an Item, the Localized Name is the name that isn't
     * shown to the user and can only be seen by the Code
     * That's useful for tracking where the item or inventory was created
     *
     * @param s is the String that becomes the new Displayname
     */
    public ItemBuilder setLocalizedName(String s) {
        itemMeta.setLocalizedName(s);
        return this;
    }

    /**
     * Sets the lore for the item using the provided strings.
     *
     * @param s the strings to be set as lore
     * @return the ItemBuilder object with updated lore
     */
    public ItemBuilder setLore(String... s) {
        itemMeta.setLore(Arrays.asList(s));
        return this;
    }

    /**
     * Sets the unbreakable tag for the item. An unbreakable item will not lose
     * durability.
     *
     * @param s true if the item should be set as unbreakable
     * @return the ItemBuilder object with the unbreakable tag set
     */
    public ItemBuilder setUnbreakable(boolean s) {
        itemMeta.setUnbreakable(s);
        return this;
    }

    /**
     * Adds the specified item flags to the item being built.
     *
     * @param s the item flags to be added
     * @return the ItemBuilder object with the updated item flags
     */
    public ItemBuilder addItemFlags(ItemFlag... s) {
        itemMeta.addItemFlags(s);
        return this;
    }

    @Override
    public String toString() {
        return "ItemBuilder{" +
                "itemMeta=" + itemMeta +
                ", itemStack=" + itemStack +
                '}';
    }

    /**
     * Sets the ItemMeta of the ItemStack being built with the specified metadata.
     * This method assigns the provided ItemMeta to the ItemStack and returns the
     * updated ItemStack.
     *
     * @return the ItemStack with the assigned ItemMeta
     */
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}