package me.faustovaz.plugin.gitlab.view.preferences.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileFilterPreferencesUtils {

    public static Boolean isToIncludeAllFiles(String preference) {
        preference = preference.trim();
        return preference.equals("*") || preference.equals(" ") || preference.isEmpty();
    }

    public static List<String> parse(String preference) {
        if (!isToIncludeAllFiles(preference)) {
            List<String> fileTypes = Arrays.asList(preference.split(","));
            return fileTypes.stream().map(fileType -> fileType.trim()).collect(Collectors.toList());
        }
        return new ArrayList<String>();
    }

}
