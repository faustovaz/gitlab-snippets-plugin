package me.faustovaz.plugin.gitlab.view.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import me.faustovaz.plugin.gitlab.snippets.GitlabPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = GitlabPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_GITLAB_URL, "");
        store.setDefault(PreferenceConstants.P_GITLAB_TOKEN, "");
        store.setDefault(PreferenceConstants.P_GITLAB_SNIPPET_FILTER, "*");
    }

}
