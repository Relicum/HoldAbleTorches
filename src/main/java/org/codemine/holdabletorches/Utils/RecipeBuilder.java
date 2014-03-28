package org.codemine.holdabletorches.Utils;


import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.codemine.holdabletorches.Torches;

import java.util.*;

/**
 * Name: RecipeBuilder.java Created: 22 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class RecipeBuilder {

    private static RecipeBuilder instance;
    private boolean isBuilt = false;
    private final char COLOR_CHAR = '\u00A7';
    private final char SAFE_CHAR = '&';

    private String[] keyLineFormat;
    private Material material;
    private boolean topRow = false;
    private boolean middleRow = false;
    private boolean bottomRow = false;
    private String[] shape = {"", "", ""};
    private StrBuilder page = new StrBuilder();
    private char empty = 'X';
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private ShapedRecipe shapedRecipe;
    private String disPlayName;
    private List<String> lore;
    private Map<Character, Material> ingredients = new HashMap<>();
    private Map<String, Enchantment> enchantmentMap = new HashMap<>();
    public ItemStack[] result = new ItemStack[9];
    public List<Character> keys = new ArrayList<>(9);

    /**
     * Instantiates a new Recipe builder.
     *
     * @param material {@link org.bukkit.Material} used to make the ItemStack for the crafting Item
     */
    public RecipeBuilder(Material material) {
        instance = this;
        this.material = material;
        this.itemMeta = Bukkit.getItemFactory().getItemMeta(this.material);


    }

    /**
     * Get Recipe builder.
     * Returns itself to start of the build chain
     *
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder}
     */
    public RecipeBuilder getBuilder() {
        return this;
    }

    /**
     * Gets static instance of {@link org.codemine.holdabletorches.Utils.RecipeBuilder}
     *
     * @return the instance to start of chaining
     */
    public static RecipeBuilder getInstance() {
        return instance;
    }

    /**
     * Get custom item.
     *
     * @return the {@link org.bukkit.inventory.ItemStack} of the Custom crafted Item
     */
    public ItemStack getCustomItem() {
        return this.itemStack;
    }


    /**
     * Sets top row.
     *
     * @param first the first row of the custom recipe MUST be 3 char in length
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setTopRow(String first) {
        Validate.notNull(first);
        Validate.isTrue(first.length() == 3);

        this.shape[0] = first;
        for(int i=0;i<3;i++){
            this.keys.add(i,first.charAt(i));
        }

        this.topRow = true;
        return this;
    }

    /**
     * Sets middle row of the crafting recipe MUST be 3 char in length
     *
     * @param middleRow the middle row
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setMiddleRow(String middleRow) {
        Validate.isTrue(middleRow.length() == 3);

        this.shape[1] = middleRow;
        for(int i=0;i<3;i++){
            this.keys.add(i+3,middleRow.charAt(i));
        }
        this.middleRow = true;
        return this;
    }

    /**
     * Sets bottom row of the crafting recipe MUST be 3 char in length
     * Must must set at least one row but that must have 3 chars in
     *
     * @param bottomRow the bottom row
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setBottomRow(String bottomRow) {
        Validate.isTrue(bottomRow.length() == 3);
        this.shape[2] = bottomRow;
        for(int i=0;i<3;i++){
            this.keys.add(i+6,bottomRow.charAt(i));
        }
        this.bottomRow = true;
        return this;
    }

    /**
     * Get the complete crafting recipe in rows
     * Do not call this until the custom ItemStack has been created to make sure the result is complete
     *
     * @return the recipe shape as string[]
     */
    public String[] getShape() {
        return shape;
    }


    /**
     * Set the Custom Recipe Shape
     * Example would be {'DTD',' T ',' S '}
     * Each line MUST be 3 char long. A Space signifies that slot is empty
     * The fist char in the first line is the top left slot in crafting bench
     *
     * @param lines String[]
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setShape(String[] lines) {
        Validate.notNull(lines, "Argument can not be null");
        Validate.isTrue(lines.length != 3, "The must be 3 lines for recipe");

        int co = 0;
        for (String line : lines) {
            this.shape[co] = addColor(line.replace(" ", "X"));
            co++;
        }

        return this;
    }

    /**
     * Set empty char. This is used when displaying the
     * Crafting recipe to the user.
     * Defaults to 'X'
     *
     * @param c the {@link java.lang.Character} to use to signify any empty slot defaults to 'X'
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setEmptyChar(char c) {
        Validate.notNull(c, "Argument can not be null");
        this.empty = c;
        return this;
    }

    /**
     * Set ItemMeta display name.
     * This is Optional and is not required to be used
     *
     * @param disPlayName {@link java.lang.String} DisplayName to set for the ItemMeta
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setItemDisplayName(String disPlayName) {
        Validate.isTrue(disPlayName.length() < 64, "Display Name Must be less than 65 characters in length: " + disPlayName);
        this.disPlayName = this.addColor(disPlayName);

        return this;
    }

    /**
     * Set item lore.
     * This is optional and is not required to be used
     *
     * @param lore {@link org.bukkit.inventory.meta.ItemMeta} that is applied to the Custom item
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setItemLore(List<String> lore) {
        Validate.notEmpty(lore, "Lore can't be empty");

        ListIterator<String> listIterator = lore.listIterator();

        int c = 0;
        while (listIterator.hasNext()) {
            this.keyLineFormat[c] = addColor(listIterator.next());
            c++;
        }

        return this;

    }

    /**
     * Add unsafe enchant to the ItemStack of the Custom recipe
     *
     * @param enchantment            the enchantment
     * @param level                  the level
     * @param ignoreLevelRestriction if true safe levels of enchant will be ignored and you can enchant to up to 1000
     * @return the recipe builder
     */
    public RecipeBuilder addUnSafeEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        return this;
    }

    /**
     * Sets the material that a character in the recipe shape refers to.
     *
     * @param key        the character that represents the ingredient in the shape.
     * @param ingredient The ingredient. {@link org.bukkit.Material}
     * @return the {@link org.codemine.holdabletorches.Utils.RecipeBuilder} for chaining
     */
    public RecipeBuilder setIngredient(Character key, Material ingredient) {
        this.ingredients.put(key, ingredient);
        return this;
    }

    /**
     * Build the Recipe and Register it with Bukkit
     * Pass false if your only want to Build the Shaped recipe.
     * If you do pass false just run {@code registerRecipe} when you require
     *
     * @param register the register
     */
    public void Build(boolean register, boolean save) {
        Validate.notNull(this.material, "Material can not be null");
        Validate.notNull(register, "Build arg must not be null");

        for(int i=0;i<9;i++){

            result[i]=new ItemStack(Material.AIR);
        }
        this.itemStack = new ItemStack(this.material, 1);
        this.itemMeta = Bukkit.getItemFactory().getItemMeta(this.material);
        this.itemMeta.setDisplayName(this.disPlayName);

        this.itemStack.setItemMeta(this.itemMeta);

        this.shapedRecipe = new ShapedRecipe(this.itemStack);

        this.shapedRecipe.shape(shape);

        int c = 0;
        for (Map.Entry<Character, Material> entry : this.ingredients.entrySet()) {

            this.shapedRecipe.setIngredient(entry.getKey(), entry.getValue());

            c++;
        }


        int tc=0;
        for(Character ch: keys){
            result[tc] = mapIngredientToKey(ch);
            tc++;
        }

/*        for(ItemStack itg: result){
            MessageUtil.logServereFormatted("The shape item stack has " + itg.getType());
        }*/

        this.page.appendNewLine();

        //EXPLAIN THE USAGE AND DIFFERENT TIME LIMITS
        this.page.append(ChatColor.BLUE + "You have 10 seconds left of " + disPlayName + " night vision left");
        this.page.append(ChatColor.BLUE + "Hold the " + disPlayName + " redstone torch and right click!");

        this.isBuilt = true;
        if (register) {
            this.registerRecipe();
        }

        saveMatrix(result,ChatColor.stripColor(disPlayName));

       // System.out.println(this.shapedRecipe.getResult());
       // System.out.println(Arrays.toString(this.shapedRecipe.getShape()));
      //  System.out.println(Arrays.toString(result));
    }

    /**
     * Register recipe with Bukkit
     * Only need to call this if Build method was passed false.
     * Don't call it twice or more or your register multiple instances
     */
    public void registerRecipe() {
        Validate.isTrue(this.isBuilt, "You need to run Build before you can register the new Recipe");

        Bukkit.addRecipe(this.shapedRecipe);

        MessageUtil.logInfoFormatted("Successfully Registered new Recipe: " + this.shapedRecipe.getResult().getItemMeta().getDisplayName());


    }

    public void saveMatrix(ItemStack[] stacks,String name){
       if(!Torches.getInstance().getConfig().contains("recipes." + name)){
          Torches.getInstance().getConfig().set("recipes." + name,stacks);
           Torches.getInstance().saveConfig();
       }



    }

    private ItemStack mapIngredientToKey(Character character){

        if(ingredients.containsKey(character)){
            return new ItemStack(ingredients.get(character));
        }

        return new ItemStack(Material.AIR);

    }


    private String generateRecipeTitle() {

        return addColor(String.format(new StrBuilder().append(COLOR_CHAR).append('a').append("-[").appendPadding(3, '-').appendPadding(2, ' ').append(COLOR_CHAR).append('6').append("%s").appendPadding(2, ' ').append(COLOR_CHAR).append('a').appendPadding(3, '-').append("]-").append(COLOR_CHAR).append('r').toString(), this.disPlayName));

    }

    public String setRecipeFormat(String line) {


        return String.format(new StrBuilder().append(COLOR_CHAR).append('a').append('|').appendPadding(2, ' ').append(COLOR_CHAR).append('d').append("%s").appendPadding(2, ' ').append("%s").appendPadding(2, ' ').append("%s").appendPadding(2, ' ').append(COLOR_CHAR).append('a').append('|').append(COLOR_CHAR).append('r').appendNewLine().toString(), line.charAt(0), line.charAt(1), line.charAt(2));


    }

    private String formatIngredientLine(Character key, String val) {
        if (key.equals(empty)) val = "EMPTY";
        return String.format(new StrBuilder().append(COLOR_CHAR)
                .append('b')
                .append('[')
                .append(COLOR_CHAR)
                .append('d')
                .append("%s")
                .append(COLOR_CHAR)
                .append('b')
                .append(']')
                .append(' ')
                .append(COLOR_CHAR)
                .append('f')
                .append(": ")
                .append(COLOR_CHAR)
                .append('c')
                .append("%s")
                .append(COLOR_CHAR)
                .append('r')
                .appendNewLine()
                .toString(), key, val);
    }

    /**
     * Convert color.
     * Shade from Bukkit Api
     *
     * @param s {@link String} {@link String} the text to convert color chars to correct format
     * @return the {@link String} line of text which has color formatted
     */
    private String addColor(String s) {
        char[] b = s.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == SAFE_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }

        }
        return new String(b);
    }

    public boolean beenBuilt() {


        return this.isBuilt;
    }

    public ItemStack[] getShapeAsStack() {
        int num = 9;
        for (int i = 0; i < 3; i++) {
            char[] chars = shape[1].toCharArray();
            num--;
            for (int c = 0; c < 3; c++) {

                Material mat = Material.valueOf(shape[chars[c]]);
                result[c] = new ItemStack(mat);

            }
        }
        return null
                ;
    }

    public ItemStack[] setMatr() {

        return null;

    }

}
