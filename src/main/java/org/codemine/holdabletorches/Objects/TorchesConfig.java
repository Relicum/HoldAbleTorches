package org.codemine.holdabletorches.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Comments;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.Yamler.Config.Path;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;

/**
 * Name: TorchesConfig.java Created: 09 May 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
public class TorchesConfig extends Config {

    @Comment("General Plugin Settings")
    @Path("settings.general")
    private HashMap<String,Object> general = new HashMap<>();
    @Comment("If true the Plugin will install and initialize all defaults")
    @Path("settings.general.firstload")
    private Boolean firstload = true;
    @Comment("Enable or Disable the Plugin. If false the plugin will not run")
    @Path("settings.general.enable")
    private Boolean enable = true;
    @Comments({"The language and Locale the plugin will use." , "Currently English is the only language supported." , "It is not had to translate it into any language." ,
               "Please contact the Developer relicum on Bukkit Dev to find out how"
    })
    @Path("settings.general.locale")
    private String locale = "en_US";

    @Comment("Settings for the Non Craftable HoldAbleTorch")
    @Path("settings.flashlight")
    private HashMap<String,Object> flashlight = new HashMap<>();
    @Comment("Enable or disable the use or the non craftable torch")
    @Path("settings.flashlight.enabled")
    private Boolean enabled = true;
    @Comment("Should players require permission to use the Holdable Torch. Default is true, permission is required")
    @Path("settings.flashlight.usepermission")
    private Boolean usepermission = true;
    @Comment("Can the torch be dropped or removed from their inventory, default to false")
    @Path("settings.flashlight.candrop")
    private Boolean candrop = false;
    @Comment("Will the torch drop on death")
    @Path("settings.flashlight.dropondeath")
    private Boolean dropondeath = false;
    @Comment("Should a sound be played when turning the torch on or off, this is only heard by the player")
    @Path("settings.flashlight.playsound")
    private Boolean playsound = true;

    public TorchesConfig(Plugin pl)
    {

        CONFIG_FILE = new File(pl.getDataFolder(), "configs.yml");
        CONFIG_HEADER = new String[]{"Stores Config Settings for Holdable Torches," +
                                       "Becareful if editing this file, NO TABS allowed," +
                                       "The contents should be in valid YML format"
        };
    }

    public void saveConfig()
    {

        try
        {
            this.save();
        }
        catch(InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void reloadConfig()
    {

        try
        {
            this.reload();
        }
        catch(InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void updateConfig()
    {

        this.update(root);
    }

}
