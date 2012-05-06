package hudson.plugins.mavendeploymentlinker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * some utilities to handle maven version.
 * 
 * @author Dominik Bartholdi (imod)
 * 
 */
public class VersionUtil {
    private static final String SNAPSHOT_FILE_PATTERN_STR = "^(.*)-([0-9.]{0,10})-([0-9]{8}.[0-9]{6})-(.*)(\\.(?i)(.*))$";
    private static final String VERSION_FILE_PATTERN_STR = "^(.*)-([0-9]{0,10})(-)*?(.*)(-)?(.*)(\\.(?i)(.*))$";
    private static final Pattern SNAPSHOT_FILE_PATTERN = Pattern.compile(SNAPSHOT_FILE_PATTERN_STR);
    private static final Pattern VERSION_FILE_PATTERN = Pattern.compile(VERSION_FILE_PATTERN_STR);

    private VersionUtil() {
    }

    /**
     * Uses the given pattern to remove the version of the file name.
     * 
     * @param fileName
     *            the name to remove the version from
     * @param stripPattern
     *            a regex pattern used to remove the version. first group is used as the new file name and the last group is used as the file extension.
     * @return the striped name, if the pattern does not match, the original name is returned
     */
    public static String stripeVersion(String fileName, String stripPattern) {
        Matcher m = Pattern.compile(stripPattern).matcher(fileName);
        if (m.matches()) {
            return m.group(1) + "." + m.group(m.groupCount());
        }
        return fileName;
    }

    /**
     * Uses the default patterns to remove the version of the file name.
     * 
     * @param fileName
     *            the name to remove the version from
     * 
     * @return the striped name, if the pattern does not match, the original name is returned
     */
    public static String stripeVersion(String name) {
        if (isSnapshot(name)) {
            Matcher m = SNAPSHOT_FILE_PATTERN.matcher(name);
            if (m.matches()) {
                return m.group(1) + "." + m.group(m.groupCount());
            }
        } else {
            Matcher m = VERSION_FILE_PATTERN.matcher(name);
            if (m.matches()) {
                return m.group(1) + "." + m.group(m.groupCount());
            }
        }
        return name;
    }

    static boolean isSnapshot(String baseVersion) {
        if (baseVersion != null) {
            Matcher m = SNAPSHOT_FILE_PATTERN.matcher(baseVersion);
            if (m.matches()) {
                // for (int i = 0; i < m.groupCount(); i++) {
                // System.out.println("group " + i + ": " + m.group(i));
                // }
                return true;
            }
        }
        return false;
    }
}
