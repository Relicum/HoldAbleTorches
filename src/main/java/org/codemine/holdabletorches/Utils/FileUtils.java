package org.codemine.holdabletorches.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Name: FileUtils.java Created: 15 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class FileUtils {

    private static boolean debug = false;

    private static String prefix = "[DEBUG] from " + FileUtils.class.getSimpleName() + " : ";

    /**
     * Create directory a new directory at the specified path
     *
     * @param path the path
     * @param name the name
     */
    public static void createDirectory(String path, String name)
    {

        if(debug)
        {
            System.out.println(prefix + "String for path is " + path);
            System.out.println(prefix + "String for name is " + name);
        }

        boolean f = new File(path + "/" + name).exists();
        if(!f)
        {
            boolean fi = new File(path + "/" + name).mkdirs();

            if(!fi && debug) System.out.println(prefix + "Error: Failed to directory at " + path + "/" + name);
        }
    }

    /**
     * String to Path.
     *
     * @param stringPath the string instance that ius to be converted to a Path object
     * @return the path The converted string as a Path
     */
    public static Path stringToPath(String stringPath)
    {

        if(debug)
        {
            System.out.println(prefix + "String for path is " + stringPath);
        }
        return Paths.get(stringPath);
    }

    /**
     * Num files in directory.
     *
     * @param path the path to the directory as a string
     * @return the int the number of files in the directory if 0 is returned this may be down to an error
     */
    @SuppressWarnings("ConstantConditions")
    public static int numFilesInDirectory(String path)
    {

        if(debug)
        {
            System.out.println(prefix + " path to check is " + path);
        }
        boolean f = new File(path).exists();
        if(f && new File(path).isDirectory())
        {
            try
            {
                if(debug) System.out.println(prefix + "The path used was " + path);
                return new File(path).listFiles().length;
            }
            catch(NullPointerException e)
            {
                e.printStackTrace();
                return 0;
            }
        }
        if(debug) System.out.println(prefix + "0 files were found");
        return 0;

    }

    /**
     * Gets all files in directory that match the extensions.
     * <p>The Extensions are passed pseudo glob eg {yml} or {yml,txt} for multiples
     *
     * @param dir the dir the directory to search as a string
     * @param ext the pseudo glob containing the extensions to match eg {yml,txt}
     * @return the all files in directory that match the extensions in the Array of strings;
     * @throws java.io.IOException the iO exception
     */
    public static List<File> getAllFilesInDirectory(String dir, final String ext) throws IOException
    {

        if(debug)
        {
            System.out.println(prefix + " Directory passed is " + dir + " and file extensions to test for is " + ext);
        }
        List<File> result = new ArrayList<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(stringToPath(dir), "*." + ext))
        {
            for(Path entry : stream)
            {
                if(debug)
                {
                    System.out.println(prefix + "Match found " + entry.toString());
                    result.add(entry.toFile());
                }
            }
        }
        catch(DirectoryIteratorException ex)
        {

            throw ex.getCause();
        }
        return result;

    }

    /**
     * Toggle debug. Default it is set to false (Eg debugging is off)
     *
     * @param de boolean to toggle debug mode
     */
    public static void toggleDebug(boolean de)
    {

        debug = de;
        if(debug)
        {
            System.out.println("Debug Mode Activated for " + FileUtils.class.getSimpleName());
        }
        else
        {
            System.out.println("Debug Mode Deactivated for " + FileUtils.class.getSimpleName());
        }
    }

}
