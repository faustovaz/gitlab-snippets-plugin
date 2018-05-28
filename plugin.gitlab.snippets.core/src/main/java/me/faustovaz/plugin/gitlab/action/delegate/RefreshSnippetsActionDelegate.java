package me.faustovaz.plugin.gitlab.action.delegate;

import org.eclipse.swt.widgets.Shell;

import me.faustovaz.plugin.gitlab.view.IGitlabPluginView;

public class RefreshSnippetsActionDelegate implements IGitlabPluginActionDelegate {

    @Override
    public void run(IGitlabPluginView view, Shell shell) {
        view.refreshContent();
    }

}
