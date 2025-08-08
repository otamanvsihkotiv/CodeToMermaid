package org.codetomermaid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ToSequenceDiagram {
    public static void notImplementedYet(Path startClass, String entryMethod, Path resultPath){
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, "Not implemented");
        } catch (IOException e) {
            System.out.println("something went terribly wrong: " + e.getMessage());
        }

    }
}
