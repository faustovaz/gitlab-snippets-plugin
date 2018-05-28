package me.faustovaz.plugin.gitlab.snippets;

import static org.junit.Assert.*;

import org.gitlab4j.api.models.Snippet;
import org.junit.Test;

public class SnippetUtilsTests {

    @Test
    public void fileTypeShouldBeBlankIfFileNameNotSpecified() {
        Snippet snippet = new Snippet();
        assertEquals("", SnippetUtils.fileType(snippet.getFileName()));
    }

    @Test
    public void fileTypeShoudBeJavaType() {
        Snippet snippet = new Snippet("Java file", "MyClass.java", "Java Code");
        assertEquals("java", SnippetUtils.fileType(snippet.getFileName()));
    }

    @Test
    public void fileTypeShoudBeXml() {
        Snippet snippet = new Snippet("Xml file", "MyXml.file.xml", "xml content");
        assertEquals("xml", SnippetUtils.fileType(snippet.getFileName()));
    }

    @Test
    public void fileTypeShouldBeBlankIfFileNameExtensionNotDefined() {
        Snippet snippet = new Snippet("My Dockerfile", "Dockerfile", "Docker instructions");
        assertEquals("", SnippetUtils.fileType(snippet.getFileName()));
    }

    @Test
    public void fileTypeShoudBeReturnedIfFileIsHidden() {
        Snippet snippet = new Snippet("Hidden file", ".git", "git file");
        assertEquals("git", SnippetUtils.fileType(snippet.getFileName()));
    }

    @Test
    public void fileTypeShouldBeBlankIfNonConventionalFileName() {
        Snippet snippet = new Snippet("Strange file", "file...", "file content");
        assertEquals("", SnippetUtils.fileType(snippet.getFileName()));
    }

    @Test
    public void fileTypeShouldBeBlankIfNoFileName() {
        Snippet snippet = new Snippet("Strange file", "", "file content");
        assertEquals("", SnippetUtils.fileType(snippet.getFileName()));
    }
}
