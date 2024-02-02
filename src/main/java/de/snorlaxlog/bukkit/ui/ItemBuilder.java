package de.snorlaxlog.bukkit.ui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    private ItemMeta itemMeta;
    private ItemStack itemStack;


    /**
     * Constructs a new ItemBuilder object with the specified material.
     *
     * @param mat the material of the item
     */
    public ItemBuilder(Material mat){
        itemStack = new ItemStack(mat);
        itemMeta = itemStack.getItemMeta();
    }

    /**
     * Sets the Displayname of an Item, the Display Name is the name shown to the user
     *
     * @param s is the String that becomes the new Displayname
     */
    public ItemBuilder setDisplayname(String s){
        itemMeta.setDisplayName(s);
        return this;
    }

    /** 
     * Sets the Localized of an Item, the Localized Name is the name that isn't shown to the user and can only be seen by the Code
     * That's useful for tracking where the item or inventory was created
     *
     * @param s is the String that becomes the new Displayname
     */
    public ItemBuilder setLocalizedName(String s){
        itemMeta.setLocalizedName(s);
        return this;
    }

    /**
     * Constructs a new ItemBuilder object with the specified material and amount.
     *
     * @param mat the material of the item
     * @param amount the amount of the item
     */
    public ItemBuilder setLore(String... s){
        itemMeta.setLore(Arrays.asList(s));
        return this;
    }

    /**
     * Constructs a new ItemBuilder object with the specified material and amount.
     *
     * @param mat the material of the item
     * @param amount the amount of the item
     */
    public ItemBuilder setUnbreakable(boolean s){
        itemMeta.setUnbreakable(s);
        return this;
    }

    /**
     * Constructs a new ItemBuilder object with the specified material and amount.
     *
     * @param mat the material of the item
     * @param amount the amount of the item
     */
    public ItemBuilder addItemFlags(ItemFlag... s){
        itemMeta.addItemFlags(s);
        return this;
    }

    /**
     * Constructs a new ItemBuilder object with the specified material and amount.
     *
     * @param mat the material of the item
     * @param amount the amount of the item
     */
    @Override
    public String toString() {
        return "ItemBuilder{" +
                "itemMeta=" + itemMeta +
                ", itemStack=" + itemStack +
                '}';
    }
    
    /**
     * Constructs a new ItemBuilder object with the specified material and amount.
     *
     * @param mat the material of the item
     * @param amount the amount of the item
     */
    public ItemStack build(){
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}