package nl.michelgreve.maven.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import nl.michelgreve.maven.plugins.UnZipper;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;

public class UnZipperTest {
    private static final Log LOG = new org.apache.maven.plugin.logging.SystemStreamLog();

    private static final List<String> EXTENSIONS = new ArrayList<String>();

    private UnZipper unZipper;

    @Before
    public void setUp() {
        EXTENSIONS.add("war");
        EXTENSIONS.add("EAR");
        unZipper = new nl.michelgreve.maven.plugins.UnZipper(LOG, EXTENSIONS);
    }

    @Test
    public void isZipExtension() {

        assertTrue(unZipper.isZipExtension("abcd.war"));
        assertTrue(unZipper.isZipExtension("abcd.WAR"));
        assertTrue(unZipper.isZipExtension("abcdwar"));
        assertFalse(unZipper.isZipExtension("abcd.jar"));
        assertTrue(unZipper.isZipExtension("abcd.ear"));
    }

    @Test
    public void unzip() throws IOException {
        final InputStream inputStream = ClassLoader.getSystemResourceAsStream("TestEar-1.0.0-SNAPSHOT.ear");
        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        final List<String> extensions = new ArrayList<String>();
        extensions.add("war");
        extensions.add("ear");
        final UnZipper unZipper = new nl.michelgreve.maven.plugins.UnZipper(LOG, extensions);
        unZipper.unzip(zipInputStream, "");
        assertEquals(27, unZipper.getFiles().size());
    }

}
