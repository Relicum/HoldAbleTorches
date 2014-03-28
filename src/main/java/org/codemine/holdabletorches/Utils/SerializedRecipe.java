package org.codemine.holdabletorches.Utils;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: SerializedRecipe.java Created: 26 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class SerializedRecipe implements ConfigurationSerializable {

    public enum Rows{
        TOP,
        MIDDLE,
        BOTTOM

    }

    private ItemStack[] shape;

    private String name;

    public SerializedRecipe(String name,ItemStack[] stacks){
        Validate.isTrue(name.length() < 32,"Serialized Recipe name is to long");
        this.name=name;
        this.shape = stacks;

    }

    public void addRow(Rows row,int pos){
        int c=0;
        if(pos==1){

        }

    }

    protected static SerializedRecipe deserialize(Map<String,Object> map){

        Object oName=map.get("name"), oStacks =map.get("items");

        if(oName==null || oStacks==null || !(oName instanceof String || !(oStacks instanceof ItemStack[]))){
            return null;
        }
        ItemStack[] stack;
        String n = (String)oName.toString();
           stack  = (ItemStack[]) oStacks;


        return new SerializedRecipe(n,stack);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map = new HashMap<>(2);
        map.put("name",this.name);
        map.put("items",this.shape);

        return map;
    }
}
