package me.faustovaz.plugin.gitlab.snippets;

import java.net.MalformedURLException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gitlab4j.api.GitLabApi;
import org.osgi.framework.BundleContext;

import me.faustovaz.plugin.gitlab.view.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class GitlabPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "me.faustovaz.plugin.gitlab.snippets"; //$NON-NLS-1$

    // The shared instance
    private static GitlabPlugin plugin;

    private static GitLabApi api;

    /**
     * The constructor
     */
    public GitlabPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
     * BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static GitlabPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative
     * path
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static GitLabApi gitlabAPI() throws MalformedURLException {
        if (api == null)
            api = new GitLabApi(getStoredValue(PreferenceConstants.P_GITLAB_URL),
                    getStoredValue(PreferenceConstants.P_GITLAB_TOKEN));
        return api;
    }

    public static GitLabApi newAPI() throws MalformedURLException {
        api = new GitLabApi(getStoredValue(PreferenceConstants.P_GITLAB_URL),
                getStoredValue(PreferenceConstants.P_GITLAB_TOKEN));
        return gitlabAPI();
    }

    public static String getStoredValue(String key) {
        return getDefault().getPreferenceStore().getString(key);
    }

    public static boolean isGitlabPluginPreferencesSaved() {
        String url = getStoredValue(PreferenceConstants.P_GITLAB_URL);
        String token = getStoredValue(PreferenceConstants.P_GITLAB_TOKEN);
        return !(url.trim().isEmpty() || token.trim().isEmpty());
    }
}
