package me.faustovaz.plugin.gitlab.action.snippet;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import me.faustovaz.plugin.gitlab.action.delegate.IGitlabPluginActionDelegate;
import me.faustovaz.plugin.gitlab.view.IGitlabPluginView;

public abstract class GitlabViewAction extends Action {

    private IGitlabPluginView view;
    private Shell shell;
    private IGitlabPluginActionDelegate delegate;

    public GitlabViewAction(IGitlabPluginView view, Shell shell, IGitlabPluginActionDelegate delegate) {
        super();
        this.view = view;
        this.shell = shell;
        this.delegate = delegate;
        init();
    }

    @Override
    public void run() {
        this.delegate.run(this.view, this.shell);
    }

    public abstract void init();
}
