package me.faustovaz.plugin.gitlab.action.delegate;

import org.eclipse.swt.widgets.Shell;

import me.faustovaz.plugin.gitlab.view.IGitlabPluginView;

public interface IGitlabPluginActionDelegate {

    public void run(IGitlabPluginView view, Shell shell);

}
