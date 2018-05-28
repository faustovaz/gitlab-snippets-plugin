package me.faustovaz.plugin.gitlab.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.gitlab4j.api.models.Snippet;

import me.faustovaz.plugin.gitlab.wizard.snippet.CreateSnippetWizard;

public class CustomWizardDialog extends WizardDialog {

    private Snippet snippet;

    public CustomWizardDialog(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
        this.setTitle("Create a new Gitlab Snippet");
        setSnippet(null);
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    @Override
    public int open() {
        CreateSnippetWizard wizard = (CreateSnippetWizard) getWizard();
        wizard.setSnippet(snippet);
        return super.open();
    }

}
