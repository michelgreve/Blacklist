package nl.michelgreve.maven.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import nl.michelgreve.maven.plugins.FilePatternReader;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

public class FilePatternReaderTest {

    private static final Log LOG = new org.apache.maven.plugin.logging.SystemStreamLog();

    @Test
    public void getLines() {
        final FilePatternReader reader = new FilePatternReader(LOG);
        final List<String> lines = reader.getLines(new File("./src/test/resources/blacklist.txt"));
        assertNotNull(lines);
        assertEquals(5, lines.size());
    }

}
