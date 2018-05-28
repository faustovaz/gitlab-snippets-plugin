package me.faustovaz.plugin.gitlab.view.preferences.filter;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;

public class FileFilterPreferencesUtilsTests {
    @Test
    public void givenAStarItShouldIncludedAllFiles() {
        assertTrue(FileFilterPreferencesUtils.isToIncludeAllFiles("*"));
    }

    @Test
    public void givenABlankSpaceItShouldIncludeAllFiles() {
        assertTrue(FileFilterPreferencesUtils.isToIncludeAllFiles(" "));
    }
    
    @Test
    public void givenAnEmptyStringItShouldIncludeAllFiles() {
        assertTrue(FileFilterPreferencesUtils.isToIncludeAllFiles(""));
    }
    
    @Test
    public void givenACommaSeparatedFileTypesItShouldReturnAListWithAllTypes() {
        List<String> filetypes = FileFilterPreferencesUtils.parse("java, sql, xml, txt");
        assertEquals(4, filetypes.size());
    }
    
    @Test
    public void givenACommaSeparatedFileTpesItShouldReturnAWellFormatedList() {
        List<String> filetypes = FileFilterPreferencesUtils
                .parse("java,                sql,         xml,    txt");
        assertEquals(4, filetypes.size());
        assertEquals("java", filetypes.get(0));
        assertEquals("sql", filetypes.get(1));
        assertEquals("xml", filetypes.get(2));
        assertEquals("txt", filetypes.get(3));      
    }
}
