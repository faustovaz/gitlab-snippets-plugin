package me.faustovaz.plugin.gitlab.snippets;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;

public class ImageUtils {

    public static Optional<ImageDescriptor> getIcon(String iconName) {
        try {
            URL url = new URL(GitlabPlugin.getDefault().getBundle().getEntry("/"), String.format("icons/%s", iconName));
            return Optional.of(ImageDescriptor.createFromURL(url));
        } catch (MalformedURLException urlException) {
            return Optional.empty();
        }
    }

    public static Optional<ImageDescriptor> getFileIcon(String iconName) {
        try {
            if (fileIconExists(iconName)) {
                URL url = new URL(GitlabPlugin.getDefault().getBundle().getEntry("/"),
                        String.format("icons/file-icons/%s.png", iconName));
                return Optional.of(ImageDescriptor.createFromURL(url));
            }
            return Optional.empty();
        } catch (MalformedURLException urlException) {
            return Optional.empty();
        }
    }

    public static Optional<ImageDescriptor> getDefaultFileIcon() {
        try {
            URL url = new URL(GitlabPlugin.getDefault().getBundle().getEntry("/"), "icons/file-icons/_blank.png");
            return Optional.of(ImageDescriptor.createFromURL(url));
        } catch (MalformedURLException urlException) {
            return Optional.empty();
        }
    }

    public static Boolean fileIconExists(String iconName) {
        String path[] = GitlabPlugin.getDefault().getBundle().getLocation().split(":");
        String iconPath = String.format("/icons/file-icons/%s.png", iconName);
        File file = new File(path[path.length - 1] + iconPath);
        return file.exists();
    }

}
