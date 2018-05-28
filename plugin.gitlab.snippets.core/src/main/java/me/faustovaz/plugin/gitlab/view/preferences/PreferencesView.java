package me.faustovaz.plugin.gitlab.view.preferences;

import java.net.MalformedURLException;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import me.faustovaz.plugin.gitlab.snippets.GitlabPlugin;
import me.faustovaz.plugin.gitlab.view.snippet.SnippetsView;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

public class PreferencesView extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String ID = "me.faustovaz.eclipse.gitlab.plugin.view.PreferencesView";
    private StringFieldEditor url;
    private StringFieldEditor token;

    public PreferencesView() {
        super(GRID);
        setPreferenceStore(GitlabPlugin.getDefault().getPreferenceStore());
        setDescription("Gitlab URL and access token settings");
    }

    public void createFieldEditors() {
        url = new StringFieldEditor(PreferenceConstants.P_GITLAB_URL, "Gitlab &URL", getFieldEditorParent());
        token = new StringFieldEditor(PreferenceConstants.P_GITLAB_TOKEN, "Gitlab &API Token", getFieldEditorParent());
        addField(url);
        addField(token);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        try {
            refreshSnippetsView();
            return super.performOk();
        } catch (PartInitException e) {
            // TODO Log the errors
            return super.performOk();
        } catch (MalformedURLException badUrl) {
            setErrorMessage("The informed gitlab URL is invalid. " + "Please a valid url");
            return false;
        }

    }

    @Override
    protected void performApply() {
        try {
            refreshSnippetsView();
            super.performApply();
        } catch (PartInitException e) {
            // TODO Log the errors
            super.performApply();
        } catch (MalformedURLException badUrl) {
            setErrorMessage("The informed gitlab URL is invalid. " + "Inform a full url.");
        }
    }

    public void refreshSnippetsView() throws PartInitException, MalformedURLException {
        url.store();
        token.store();
        GitlabPlugin.newAPI();
        IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SnippetsView.ID);
        if (view != null) {
            SnippetsView snippetsView = (SnippetsView) view;
            snippetsView.refreshContent();
        }
    }

}