package me.faustovaz.plugin.gitlab.snippets;

public class SnippetUtils {
    
    public static final String fileType(String fileName) {
        if(fileName != null) {
            String[] parts = fileName.split("\\.");
            if (parts.length > 1) {
                String extension = parts[parts.length - 1];
                return extension;
            }
        }
        return "";
    }
}
