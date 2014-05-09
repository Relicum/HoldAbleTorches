package org.codemine.holdabletorches.Objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: SerializedSettings.java Created: 15 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@SerializableAs("SerializedSettings")
public class SerializedSettings implements ConfigurationSerializable {

    private SerializedCraftSettings ironSight;
    private SerializedCraftSettings goldSight;
    private SerializedCraftSettings diamondSight;
    private SerializedCraftSettings emeraldSight;
    private boolean enable;
    private int id;

    public SerializedSettings(int id, boolean enable, SerializedCraftSettings ironSight, SerializedCraftSettings goldSight, SerializedCraftSettings diamondSight,
                              SerializedCraftSettings emeraldSight)
    {

        this.id = id;
        this.enable = enable;
        this.ironSight = ironSight;
        this.goldSight = goldSight;
        this.diamondSight = diamondSight;
        this.emeraldSight = emeraldSight;
    }

    public static SerializedSettings deserialize(Map<String,Object> map)
    {

        Object objI = map.get("ironsight"),
          objG = map.get("goldsight"),
          objD = map.get("diamondsight"),
          objE = map.get("emeraldsight"),
          objEnable = map.get("enable"),
          objId = map.get("id");

        return new SerializedSettings((int) objId,
                                       (Boolean) objEnable,
                                       (SerializedCraftSettings) objI,
                                       (SerializedCraftSettings) objG,
                                       (SerializedCraftSettings) objD,
                                       (SerializedCraftSettings) objE);
    }

    @Override
    public Map<String,Object> serialize()
    {

        Map<String,Object> map = new HashMap<>();
        map.put("enable", isEnable());
        map.put("id", getId());
        map.put("ironsight", getIronSight());
        map.put("goldsight", getGoldSight());
        map.put("diamondsight", getDiamondSight());
        map.put("emeraldsight", getEmeraldSight());
        return map;
    }

    public SerializedCraftSettings getIronSight()
    {

        return ironSight.get();
    }

    public SerializedCraftSettings getEmeraldSight()
    {

        return emeraldSight.get();
    }

    public SerializedCraftSettings getDiamondSight()
    {

        return diamondSight.get();
    }

    public SerializedCraftSettings getGoldSight()
    {

        return goldSight.get();
    }

    public void setIronSight(SerializedCraftSettings ironSight)
    {

        this.ironSight = ironSight;
    }

    public void setGoldSight(SerializedCraftSettings goldSight)
    {

        this.goldSight = goldSight;
    }

    public void setDiamondSight(SerializedCraftSettings diamondSight)
    {

        this.diamondSight = diamondSight;
    }

    public void setEmeraldSight(SerializedCraftSettings emeraldSight)
    {

        this.emeraldSight = emeraldSight;
    }

    public boolean isEnable()
    {

        return enable;
    }

    public void setEnable(boolean enable)
    {

        this.enable = enable;
    }

    public int getId()
    {

        return id;
    }

    public void setId(int id)
    {

        this.id = id;
    }

    public void incrementId()
    {

        this.id++;
    }

    @Override
    public String toString()
    {

        return "SerializedSettings{" +
                 "ironSight=" + ironSight.get().toString() +
                 ", goldSight=" + goldSight.get().toString() +
                 ", diamondSight=" + diamondSight.get().toString() +
                 ", emeraldSight=" + emeraldSight.get().toString() +
                 ", enable=" + enable +
                 ", id=" + id +
                 '}';
    }
}
