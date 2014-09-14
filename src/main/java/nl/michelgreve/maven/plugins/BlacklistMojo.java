package nl.michelgreve.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/*
 * @goal blacklist
 * @requiresProject false
 * @phase verify
 */
public class BlacklistMojo extends AbstractMojo {

    private final List<Pair<String, String>> files = new ArrayList<Pair<String, String>>();
    private final UnZipper unZipper;

    /*
     * @parameter
     */
    private final List<String> extensions = new ArrayList<String>();

    /*
     * @parameter default-value="false"
     */
    private boolean isWarningError;

    /*
     * @parameter default-value="true"
     */
    private boolean isDeployError;

    /*
     * @parameter expression="${blacklist.location}" default-value="blacklist.txt"
     * 
     * @required
     */
    private String location;

    /*
     * @parameter expression="${project.build.directory}"
     */
    private File buildDirectory;

    /*
     * @parameter expression="${project.build.defaultGoal}"
     */
    private String lifecycle;

    private final FilePatternReader reader = new FilePatternReader(getLog());

    private final List<Pattern> patterns = new ArrayList<Pattern>();

    public BlacklistMojo() {
        super();
        if (extensions.isEmpty()) {
            extensions.add("war");
            extensions.add("ear");
        }
        unZipper = new UnZipper(getLog(), extensions);
    }

    /**
     * Start the execution of the plugin.
     * 
     * @MojoExecutionException When a blacklisted item is detected and the configuration is set to error for this maven
     *                         lifecycle.
     */
    public void execute() throws MojoExecutionException {
        getLog().info("Blacklist location: " + location.toString());
        final File blackListFile = new File(location.toString());
        if (blackListFile.exists()) {
            final List<String> lines = reader.getLines(blackListFile);

            for (String line : lines) {
                patterns.add(Pattern.compile(line));
            }

        } else {
            getLog().error("Can't find file at locattion: '" + blackListFile.getAbsolutePath() + "'");
        }

        final File[] buildFiles = buildDirectory.listFiles();
        getFiles(buildFiles, "");
        files.addAll(unZipper.getFiles());
        
        getLog().debug("=======================================================================");
        getLog().debug("Blacklist patterns:");
        getLog().debug("=======================================================================");
        final List<Pair<String, String>> blackListedFiles = new ArrayList<Pair<String, String>>();
        for (Pair<String, String> pair : files) {
            getLog().debug(pair.getFirst() + pair.getSecond());
            for (Pattern pattern : patterns) {
                if (pattern.matcher(pair.getSecond()).matches()) {
                    blackListedFiles.add(pair);
                }
            }
        }
        getLog().debug("=======================================================================");

        if (!blackListedFiles.isEmpty()) {
            getLog().warn("=======================================================================");
            getLog().warn("Blacklisted files:");
            getLog().warn("=======================================================================");
            for (Pair<String, String> pair : blackListedFiles) {
                getLog().warn("\t" + pair.getFirst() + pair.getSecond());
            }
            getLog().warn("=======================================================================");
            getLog().debug("lifecycle: " + lifecycle);
            if (isError(lifecycle)) {
                throw new MojoExecutionException("Black listed files detected: " + blackListedFiles.toString());
            }
        }
    }

    private boolean isError(final String lifeCycle) {
        boolean result = false;
        if ("deploy".equals(lifeCycle)) {
            result = isDeployError;
        } else {
            result = isWarningError;
        }
        return result;
    }

    private void getFiles(final File[] fileList, final String prefix) throws MojoExecutionException {
        for (File file : fileList) {
            if (file.isDirectory()) {
                getFiles(file.listFiles(), file.getName()); // Calls same method again.
            } else {
                files.add(new Pair<String, String>(prefix, file.getName()));
                if (unZipper.isZipExtension(file.getName())) {
                    ZipInputStream zipIn = null;
                    try {
                        zipIn = new ZipInputStream(new FileInputStream(file));

                        unZipper.unzip(zipIn, prefix + "/" + file.getName() + "/");
                    } catch (final IOException exception) {
                        throw new MojoExecutionException("Can't unzip archive", exception);
                    } finally {
                        try {
                            zipIn.close();
                        } catch (final IOException exception) {
                            getLog().warn("Can't free resource: zipIn", exception);
                        }
                    }
                }
            }
        }
    }
}
