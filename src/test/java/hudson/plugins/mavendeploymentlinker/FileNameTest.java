package hudson.plugins.mavendeploymentlinker;

import junit.framework.TestCase;

/**
 * @author Dominik Bartholdi (imod)
 */
public class FileNameTest extends TestCase {

    private static String NAME1 = "test_AAA_module1-0.0.6-20120501.152207-3.pom";
    private static String NAME2 = "test-BBB-module1-0.0.6-20120501.152207-3.pom";
    private static String NAME3 = "test-CCC-module1-10.0.6-20120501.152207-3.pom";
    private static String NAME4 = "test-CCC-module1-10.0.6.jar";
    private static String NAME5 = "test_CCC-module1-10-10.0.6.ear";
    private static String NAME6 = "test-1-module1-10.0.6.pom";
    private static String NAME7 = "test_1_module1-10.0.6.pom";
    private static String NAME8 = "test-10-module1-10.0.6-20120501.152207-3.pom";
    private static String NAME9 = "test-10-module1-9-10.0.6-20120501.152207-3.pom";
    private static String NAME10 = "test_multi_module1-0.0.6-20120501.152207-3.pom";

    public void testGetBaseName() throws Exception {
        assertEquals("test_AAA_module1.pom", VersionUtil.stripeVersion(NAME1));
        assertEquals("test-BBB-module1.pom", VersionUtil.stripeVersion(NAME2));
        assertEquals("test-CCC-module1.pom", VersionUtil.stripeVersion(NAME3));
        assertEquals("test-CCC-module1.jar", VersionUtil.stripeVersion(NAME4));
        assertEquals("test_CCC-module1-10.ear", VersionUtil.stripeVersion(NAME5));
        assertEquals("test-1-module1.pom", VersionUtil.stripeVersion(NAME6));
        assertEquals("test_1_module1.pom", VersionUtil.stripeVersion(NAME7));
        assertEquals("test-10-module1.pom", VersionUtil.stripeVersion(NAME8));
        assertEquals("test-10-module1-9.pom", VersionUtil.stripeVersion(NAME9));
        assertEquals("test_multi_module1.pom", VersionUtil.stripeVersion(NAME10));
    }

}
