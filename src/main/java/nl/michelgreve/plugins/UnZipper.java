package nl.michelgreve.plugins;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.maven.plugin.logging.Log;

class UnZipper {

    private static final int BUFFER_SIZE = 4096; // Size of sector on disk

    private final Log log;

    private final List<String> extensions = new ArrayList<String>();

    private final List<Pair<String, String>> files = new ArrayList<Pair<String, String>>();

    public UnZipper(final Log log, final List<String> extensions) {
        this.log = log;
        for (String extension : extensions) {
            this.extensions.add(extension.toLowerCase(Locale.ROOT));
        }
    }

    public boolean isZipExtension(final String filename) {
        final String name = filename.toLowerCase(Locale.ROOT);
        boolean result = false;
        for (String zipExtension : extensions) {
            if (name.endsWith(zipExtension)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public List<Pair<String, String>> getFiles() {
        return files;
    }

    public void unzip(final ZipInputStream zipIn, final String prefix) throws IOException {
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                files.add(new Pair<String, String>(prefix, entry.getName()));
                log.debug(prefix + entry.getName());
                if (isZipExtension(entry.getName())) {
                    unzipNestedFile(zipIn, entry);
                }
            } else {
                files.add(new Pair<String, String>(prefix, entry.getName()));
                log.debug(prefix + entry.getName() + "/");
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
    }

    private void unzipNestedFile(final ZipInputStream zipIn, ZipEntry entry) throws IOException {
        log.info("Zippable file detected!");
        final byte[] bytes = readZipFile(zipIn);
        final ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes));
        try {
            log.debug("Unzipping the: " + entry.getName());
            unzip(zip, entry.getName() + "/");
        } finally {
            try {
                zip.close();
            } catch (final IOException exception) {
                log.warn("Can't free resource: zip [" + entry.getName() + "]");
            }
        }
    }

    private byte[] readZipFile(final ZipInputStream zipIn) throws IOException {
        final ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
        int bytesRead;
        final byte[] buffer = new byte[BUFFER_SIZE];
        try {
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                streamBuilder.write(buffer, 0, bytesRead);
            }
        } finally {
            try {
                streamBuilder.close();
            } catch (final IOException exception) {
                log.warn("Can't free resource: streamBuilder", exception);
            }
        }
        return streamBuilder.toByteArray();
    }

}
