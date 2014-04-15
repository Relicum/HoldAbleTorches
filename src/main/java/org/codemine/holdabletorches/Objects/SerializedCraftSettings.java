package org.codemine.holdabletorches.Objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: SerializedCraftSettings.java Created: 15 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@SerializableAs("SerializedCraftSettings")
public class SerializedCraftSettings implements ConfigurationSerializable {

    private String name;
    private boolean enable;
    private boolean usepermission;
    private boolean craftpermission;
    private boolean candrop;
    private boolean dropondeath;
    private int maxLight;

    /**
     * Instantiates a new Serialized craft settings.
     *
     * @param name the name
     * @param enable the enable
     * @param usepermission the usepermission
     * @param craftpermission the craftpermission
     * @param candrop the candrop
     * @param dropondeath the dropondeath
     * @param maxLight the max light
     */
    public SerializedCraftSettings(String name, boolean enable, boolean usepermission, boolean craftpermission, boolean candrop, boolean dropondeath, int maxLight)
    {

        this.name = name;
        this.enable = enable;
        this.usepermission = usepermission;
        this.craftpermission = craftpermission;
        this.candrop = candrop;
        this.dropondeath = dropondeath;
        this.maxLight = maxLight;
    }

    /**
     * Deserialize serialized craft settings.
     *
     * @param map the map
     * @return the serialized craft settings
     */
    public static SerializedCraftSettings deserialize(Map<String,Object> map)
    {

        Object objName = map.get("name"),
          objEnable = map.get("enable"),
          objUsepermission = map.get("usepermission"),
          objCraftpermission = map.get("craftpermission"),
          objCandrop = map.get("candrop"),
          objDropondeath = map.get("dropondeath"),
          objMaxLight = map.get("maxLight");

        try
        {
            if(objName == null || objEnable == null || objUsepermission == null || objCraftpermission == null
                 || objCandrop == null || objDropondeath == null || objMaxLight == null)
            {
                return null;
            }
        }
        catch(NullPointerException ne)
        {
            ne.printStackTrace();
        }

        String oName = null;
        Boolean oEnable = null;
        Boolean oUseperm = null;
        Boolean oUseCraftperm = null;
        Boolean oCan = null;
        Boolean oDeath = null;
        int oMax = 0;
        try
        {
            oName = (String) objName;
            oEnable = (Boolean) objEnable;
            oUseperm = (Boolean) objUsepermission;
            oUseCraftperm = (Boolean) objCraftpermission;
            oCan = (Boolean) objCandrop;
            oDeath = (Boolean) objDropondeath;
            oMax = (Integer) objMaxLight;
        }
        catch(ClassCastException e)
        {
            e.printStackTrace();
        }

        return new SerializedCraftSettings(oName, oEnable, oUseperm, oUseCraftperm, oCan, oDeath, oMax);

    }

    @Override
    public Map<String,Object> serialize()
    {

        Map<String,Object> map = new HashMap<>(8);
        map.put("name", getName());
        map.put("enable", isEnable());
        map.put("usepermission", isUsepermission());
        map.put("craftpermission", isCraftpermission());
        map.put("candrop", isCandrop());
        map.put("dropondeath", isDropondeath());
        map.put("maxLight", getMaxLight());
        return map;
    }

    public SerializedCraftSettings get()
    {

        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName()
    {

        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name)
    {

        this.name = name;
    }

    /**
     * Is enable.
     *
     * @return the boolean
     */
    public boolean isEnable()
    {

        return enable;
    }

    /**
     * Sets enable.
     *
     * @param enable the enable
     */
    public void setEnable(boolean enable)
    {

        this.enable = enable;
    }

    /**
     * Is usepermission.
     *
     * @return the boolean
     */
    public boolean isUsepermission()
    {

        return usepermission;
    }

    /**
     * Sets usepermission.
     *
     * @param usepermission the usepermission
     */
    public void setUsepermission(boolean usepermission)
    {

        this.usepermission = usepermission;
    }

    /**
     * Is craftpermission.
     *
     * @return the boolean
     */
    public boolean isCraftpermission()
    {

        return craftpermission;
    }

    /**
     * Sets craftpermission.
     *
     * @param craftpermission the craftpermission
     */
    public void setCraftpermission(boolean craftpermission)
    {

        this.craftpermission = craftpermission;
    }

    /**
     * Is candrop.
     *
     * @return the boolean
     */
    public boolean isCandrop()
    {

        return candrop;
    }

    /**
     * Sets candrop.
     *
     * @param candrop the candrop
     */
    public void setCandrop(boolean candrop)
    {

        this.candrop = candrop;
    }

    /**
     * Is dropondeath.
     *
     * @return the boolean
     */
    public boolean isDropondeath()
    {

        return dropondeath;
    }

    /**
     * Sets dropondeath.
     *
     * @param dropondeath the dropondeath
     */
    public void setDropondeath(boolean dropondeath)
    {

        this.dropondeath = dropondeath;
    }

    /**
     * Gets max light.
     *
     * @return the max light
     */
    public int getMaxLight()
    {

        return maxLight;
    }

    /**
     * Sets max light.
     *
     * @param maxLight the max light
     */
    public void setMaxLight(int maxLight)
    {

        this.maxLight = maxLight;
    }

    @Override
    public String toString()
    {

        return "SerializedCraftSettings{" +
                 "name='" + name + '\'' +
                 ", enable=" + enable +
                 ", usepermission=" + usepermission +
                 ", craftpermission=" + craftpermission +
                 ", candrop=" + candrop +
                 ", dropondeath=" + dropondeath +
                 ", maxLight=" + maxLight +
                 '}';
    }
}
