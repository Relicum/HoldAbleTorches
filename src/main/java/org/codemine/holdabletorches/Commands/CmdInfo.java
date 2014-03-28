package org.codemine.holdabletorches.Commands;

import java.lang.annotation.*;

/**
 * Name: CmdInfo.java Created: 28 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CmdInfo {

    /**
     * Description of the command string.
     *
     * @return the string representing the command description
     */
    public abstract String description();

    /**
     * Command Usage string.
     *
     * @return the string representing the command usage.
     */
    public abstract String usage();

    /**
     * Command Permission in string format.
     *
     * @return the string permission.
     */
    public abstract String permission();

    /**
     * Can the command only be run in game.
     * <p>
     * True: and command is in game only
     * False: and command can also be run from console
     * </p>
     *
     * @return the boolean
     */
    public abstract boolean playerOnly();

    /**
     * Minimum arguments the command can accept.
     *
     * @return the int minimum arguments
     */
    public abstract int minArgs();

    /**
     * Maximum arguments the command can accept.
     *
     * @return the int the max arguments
     */
    public abstract int maxArgs();


    /**
     * Aliases for the command
     * <p/>
     * Lowercase only. eg {"alias1", "alias2"}
     *
     * @return the string []
     */
    public abstract String[] aliases();
}
