
package de.abring.helfer;

import java.io.File;

/**
 *
 * @author Andreas
 */
public class PathFinder {
    public static String getAbsolutePath(){
        String AbsolutePath = "";
        File Path = new File(".");
        AbsolutePath = Path.getAbsolutePath();
        if (AbsolutePath.endsWith(".")) 
            AbsolutePath = AbsolutePath.substring(0, AbsolutePath.length() - 1);
        if (AbsolutePath.endsWith(File.separator))
            AbsolutePath = AbsolutePath.substring(0, AbsolutePath.length() - 1);
        return AbsolutePath;
    }
}
