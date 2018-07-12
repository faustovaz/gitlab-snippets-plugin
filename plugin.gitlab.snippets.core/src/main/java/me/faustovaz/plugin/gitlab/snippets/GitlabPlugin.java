package me.faustovaz.plugin.gitlab.snippets;

import java.net.MalformedURLException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gitlab4j.api.GitLabApi;
import org.osgi.framework.BundleContext;

import me.faustovaz.plugin.gitlab.view.preferences.PreferenceConstants;

public class GitlabPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "me.faustovaz.plugin.gitlab.snippets"; //$NON-NLS-1$

    private static GitlabPlugin plugin;

    private static GitLabApi api;

    public GitlabPlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static GitlabPlugin getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static GitLabApi gitlabAPI() throws MalformedURLException {
        if (api == null) {
            api = new GitLabApi(getStoredValue(PreferenceConstants.P_GITLAB_URL),
                    getStoredValue(PreferenceConstants.P_GITLAB_TOKEN));
            api.setIgnoreCertificateErrors(true);
        }
        return api;
    }

    public static GitLabApi newAPI() throws MalformedURLException {
        api = null;
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
