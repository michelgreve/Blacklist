package nl.michelgreve.plugins;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

public class BlacklistMojoTest {

    @Test
    public void execute() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, MojoExecutionException,
            MojoFailureException {
        final BlacklistMojo mojo = new BlacklistMojo();
        final Class<? extends BlacklistMojo> ftClass = mojo.getClass();

        final Field f1 = ftClass.getDeclaredField("location");
        f1.setAccessible(true);
        f1.set(mojo, "./src/test/resources/blacklist.txt");

        final Field buildDirectory = ftClass.getDeclaredField("buildDirectory");
        buildDirectory.setAccessible(true);
        buildDirectory.set(mojo, new File("./src/test/resources/"));

        mojo.execute();
    }

}
