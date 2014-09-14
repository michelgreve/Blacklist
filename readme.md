blacklist-maven-plugin
======================

Usage
-----
There are a few options for the plugin. See the example

    <project>  
      [...]  
      <build>  
        <plugins>  
          <plugin>  
            <artifactId>maven-blacklist-plugin</artifactId>  
            <version>1.0.0</version>  
            <configuration>  
              <extensions>  
                <extension>war</extension>  
                <extension>ear</extension>  
              </extensions>  
              <isWarningError>true</isWarningError>  
              <isDeployError>true</isDeployError>  
              <location>blacklist.txt</location>  
            </configuration>  
          </plugin>  
        </plugins>  
      </build>  
    </project>  
    
* Extensions - The plugin will search in these extension for blacklisted files (only the zip format without password is currently supported).
* isWarningError - If set to true, a simple compile will break the build if a blacklisted file is found. If set to false, only a warning is printed on the screen.
* isDeployError - If set to true, a simple deploy will break the build if a blacklisted file is found. If set to false, only a warning is printed on the screen.
* location - The location that contains the file with the blacklisted files. This blacklisted files may be given in java regular expression format.
 


