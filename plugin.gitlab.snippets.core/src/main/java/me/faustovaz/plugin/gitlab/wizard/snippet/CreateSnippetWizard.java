package me.faustovaz.plugin.gitlab.wizard.snippet;

import java.net.MalformedURLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Snippet;

import me.faustovaz.plugin.gitlab.snippets.GitlabPlugin;
import me.faustovaz.plugin.gitlab.view.snippet.SnippetsView;
import me.faustovaz.plugin.gitlab.wizard.page.CreateSnippetWizardPage;

public class CreateSnippetWizard extends Wizard implements INewWizard {

    public static final String ID = "me.faustovaz.plugin.gitlab.wizard.snippet.create";

    private CreateSnippetWizardPage page;
    private Snippet snippet;

    public CreateSnippetWizard() {
        super();
    }

    @Override
    public boolean performFinish() {
        Snippet pageSnippet = page.getSnippet();
        if (pageSnippet.getTitle().isEmpty() || pageSnippet.getFileName().isEmpty()
                || pageSnippet.getContent().isEmpty())
            return false;
        try {
            GitLabApi gitlabAPI = GitlabPlugin.gitlabAPI();

            gitlabAPI.getSnippetApi().createSnippet(pageSnippet.getTitle(), pageSnippet.getFileName(),
                    pageSnippet.getContent(), pageSnippet.getVisibility(), pageSnippet.getDescription());

            IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(SnippetsView.ID);
            SnippetsView snippetsView = (SnippetsView) view;
            snippetsView.refreshContent();
            return true;
        } catch (GitLabApiException e) {
            MessageDialog.openError(getShell(), "Error", e.getMessage());
            return false;
        } catch (PartInitException partInitException) {
            // It doesn't matter if the wizard can't refresh the view to get the added
            // snippet
            return true;
        } catch (MalformedURLException badUrl) {
            MessageDialog.openError(getShell(), "Error", "The informed gitlab URL is invalid. Please a valid url");
            return false;
        }
    }

    @Override
    public void addPages() {
        addPage(getCreateSnippetWizardPage());
        super.addPages();
    }

    public CreateSnippetWizardPage getCreateSnippetWizardPage() {
        page = new CreateSnippetWizardPage();
        page.setSnippet(getSnippet());
        return page;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public Snippet getSnippet() {
        if (snippet == null)
            snippet = new Snippet("", "", "");
        return snippet;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

}
