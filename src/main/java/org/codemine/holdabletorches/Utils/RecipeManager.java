package org.codemine.holdabletorches.Utils;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: RecipeManager.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class RecipeManager {

    private List<String> names = new ArrayList<>();

    public RecipeManager() {
    }

    public RecipeBuilder getRecipeBuilder(Material material) {
        Validate.notNull(material);
        return new RecipeBuilder(material);
    }

    public void addRecipeName(String name) {
        Validate.notNull(name, "Must pass a name for recipe");
        if (!isValidName(name)) {
            names.add(ChatColor.stripColor(name));
        }
    }

    public boolean isValidName(String name) {
        Validate.notNull(name);

        return names.contains(name);
    }

    public List<String> getValidNames() {
        return names;
    }
}
