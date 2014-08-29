package nl.michelgreve.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

class FilePatternReader {
    private final Log log;

    public FilePatternReader(final Log log) {
        this.log = log;
    }

    public List<String> getLines(final File file) {
        List<String> lines = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            lines = readLines(reader);
        } catch (final FileNotFoundException exception) {
            log.error("Can't find file in location: '" + file.getAbsolutePath() + "'", exception);
        } catch (final IOException exception) {
            log.error("Can't read from file", exception);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (final IOException exception) {
                    log.error("Can't close resource: 'reader'", exception);
                }
            }
        }
        return lines;
    }

    private List<String> readLines(final BufferedReader reader) throws IOException {
        final List<String> lines = new ArrayList<String>();
        String line;
        line = reader.readLine();
        while (null != line) {
            log.debug(line);
            if (!line.trim().isEmpty()) {
                lines.add(line);
            }
            line = reader.readLine();
        }
        return lines;
    }

}
