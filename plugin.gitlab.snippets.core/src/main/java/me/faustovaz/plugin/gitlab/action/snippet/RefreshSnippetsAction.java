package me.faustovaz.plugin.gitlab.action.snippet;

import org.eclipse.swt.widgets.Shell;

import me.faustovaz.plugin.gitlab.action.delegate.IGitlabPluginActionDelegate;
import me.faustovaz.plugin.gitlab.snippets.ImageUtils;
import me.faustovaz.plugin.gitlab.view.IGitlabPluginView;

public class RefreshSnippetsAction extends GitlabViewAction {
    public RefreshSnippetsAction(IGitlabPluginView view, Shell shell, IGitlabPluginActionDelegate delegate) {
        super(view, shell, delegate);
    }

    @Override
    public void init() {
        this.setText("Refresh");
        this.setToolTipText("Refresh current snippet view");
        this.setImageDescriptor(ImageUtils.getIcon("refresh.gif").get());
    }
}
