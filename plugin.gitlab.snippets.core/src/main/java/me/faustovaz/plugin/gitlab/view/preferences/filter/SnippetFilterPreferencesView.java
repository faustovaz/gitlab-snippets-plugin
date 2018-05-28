package me.faustovaz.plugin.gitlab.view.preferences.filter;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import me.faustovaz.plugin.gitlab.snippets.GitlabPlugin;
import me.faustovaz.plugin.gitlab.view.preferences.PreferenceConstants;
import me.faustovaz.plugin.gitlab.view.snippet.SnippetsView;

public class SnippetFilterPreferencesView extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String ID = "me.faustovaz.plugin.gitlab.view.preferences.SnippetFilterPreferencesView";

    public SnippetFilterPreferencesView() {
        super(GRID);
        setPreferenceStore(GitlabPlugin.getDefault().getPreferenceStore());
        setDescription("Comma separated file extensions for filtering (e.g: java, jsp, xml, sql, txt)");
    }

    @Override
    public void init(IWorkbench workbench) {

    }

    @Override
    protected void createFieldEditors() {
        StringFieldEditor fileExtensions = new StringFieldEditor(PreferenceConstants.P_GITLAB_SNIPPET_FILTER,
                "File types", getFieldEditorParent());
        fileExtensions.setErrorMessage("");
        addField(fileExtensions);
    }

    @Override
    public boolean performOk() {
        try {
            IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(SnippetsView.ID);
            if (view != null) {
                SnippetsView snippetsView = (SnippetsView) view;
                snippetsView.refreshContent();
            }
            return super.performOk();
        } catch (PartInitException e) {
            // TODO Log the errors
            return super.performOk();
        }
    }

}
