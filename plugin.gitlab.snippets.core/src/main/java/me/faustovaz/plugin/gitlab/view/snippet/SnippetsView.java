package me.faustovaz.plugin.gitlab.view.snippet;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import api.gitlab.wrapper.exception.GitlabApiException;
import api.gitlab.wrapper.model.GitlabSnippet;
import me.faustovaz.eclipse.gitlab.plugin.GitlabPlugin;
import me.faustovaz.eclipse.gitlab.plugin.action.delegate.RefreshSnippetsActionDelegate;
import me.faustovaz.eclipse.gitlab.plugin.action.snippet.RefreshSnippetsAction;
import me.faustovaz.eclipse.gitlab.plugin.view.IGitlabPluginView;
import me.faustovaz.eclipse.gitlab.plugin.view.preferences.PreferenceConstants;
import me.faustovaz.eclipse.gitlab.plugin.view.preferences.filter.FileFilterPreferencesUtils;
import me.faustovaz.eclipse.gitlab.plugin.view.snippet.editor.StringEditorInput;

public class SnippetsView extends ViewPart implements IGitlabPluginView {

    public static final String ID = "me.faustovaz.eclipse.gitlab.plugin.view.SnippetsView";

    @Inject
    IWorkbench workbench;

    private TableViewer viewer;

    private RefreshSnippetsAction refreshSnippetsAction;

    @Override
    public void createPartControl(Composite parent) {
        this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.buildTable(this.viewer);
        workbench.getHelpSystem().setHelp(viewer.getControl(), "gitlab-plugin.viewer");
        getSite().setSelectionProvider(viewer);

        refreshSnippetsAction = new RefreshSnippetsAction(this, parent.getShell(), new RefreshSnippetsActionDelegate());
        refreshSnippetsAction.setEnabled(true);

        createMenu();
        createContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }

    public void buildTable(TableViewer viewer) {
        try {
            if (!GitlabPlugin.isGitlabPluginPreferencesSaved()) {
                createSingleLineTableWith(viewer, "Please inform Gitlab Url and Access Token in the Gitlab Preferences",
                        false);
            } else {
                List<GitlabSnippet> snippets = GitlabPlugin.gitlabAPI().snippets().allSnippets();
                snippets = applyFileFilterPreferences(snippets);
                if (snippets.isEmpty())
                    createSingleLineTableWith(viewer, "No snippets to show", false);
                else {
                    createTableColumns(viewer.getTable());
                    viewer.setContentProvider(new SnippetContentProvider());
                    viewer.setLabelProvider(new SnippetLabelProvider());
                    viewer.setInput(snippets);
                    viewer.getTable().setHeaderVisible(true);
                    viewer.getTable().setLinesVisible(true);
                }
            }
        } catch (GitlabApiException e) {
            createSingleLineTableWith(viewer, e.getMessage(), true);
        } catch (MalformedURLException badUrl) {
            createSingleLineTableWith(viewer, badUrl.getMessage(), true);
        }
    }

    public void createSingleLineTableWith(TableViewer viewer, String message, boolean error) {
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setLabelProvider(new SnippetErrorLabelProvider());
        if (error)
            viewer.setInput(new String[] { "Error: " + message });
        else
            viewer.setInput(new String[] { message });
    }

    public void createTableColumns(Table table) {

        TableColumn icon = new TableColumn(table, SWT.LEFT, 0);
        icon.setResizable(Boolean.TRUE);
        icon.setWidth(32);
        icon.setText("");

        TableColumn fileName = new TableColumn(table, SWT.LEFT, 1);
        fileName.setResizable(Boolean.TRUE);
        fileName.setWidth(300);
        fileName.setText("File");

        TableColumn title = new TableColumn(table, SWT.LEFT, 2);
        title.setResizable(Boolean.TRUE);
        title.setWidth(300);
        title.setText("Title");

        TableColumn description = new TableColumn(table, SWT.LEFT, 3);
        description.setResizable(Boolean.TRUE);
        description.setWidth(500);
        description.setText("Description");
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    private void createMenu() {
        MenuManager manager = new MenuManager();
        manager.setRemoveAllWhenShown(Boolean.TRUE);
        manager.addMenuListener(new IMenuListener() {

            @Override
            public void menuAboutToShow(IMenuManager manager) {
                manager.add(refreshSnippetsAction);
            }
        });

        Menu menu = manager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(manager, null);
    }

    private void createContextMenu() {
        Menu contextMenu = new Menu(this.viewer.getTable());
        this.viewer.getTable().setMenu(contextMenu);
        addDeleteMenuItem(contextMenu);
        new MenuItem(contextMenu, SWT.SEPARATOR);
        addViewMenuItem(contextMenu);
    }

    private void addDeleteMenuItem(Menu menu) {
        MenuItem itemRemove = new MenuItem(menu, SWT.NONE);
        itemRemove.setText("Delete Snippet");
        itemRemove.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    boolean deleteIt = MessageDialog.openConfirm(viewer.getControl().getShell(), "Gitlab Snippet",
                            "Delete snippet?");
                    if (deleteIt) {
                        int selectedIndex = viewer.getTable().getSelectionIndex();
                        TableItem item = viewer.getTable().getItem(selectedIndex);
                        GitlabSnippet snippet = (GitlabSnippet) item.getData();
                        GitlabPlugin.gitlabAPI().snippets().remove(snippet);
                        refreshContent();
                    }
                } catch (GitlabApiException error) {
                    MessageDialog.openError(viewer.getControl().getShell(), "Snippets Error",
                            "Error: " + error.getMessage());
                } catch (MalformedURLException badUrl) {
                    MessageDialog.openError(viewer.getControl().getShell(), "Gitlab Plugin Error",
                            "Error: " + badUrl.getMessage());
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void addViewMenuItem(Menu menu) {
        MenuItem itemView = new MenuItem(menu, SWT.NONE);
        itemView.setText("View Snippet");
        itemView.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                int selected = viewer.getTable().getSelectionIndex();
                TableItem item = viewer.getTable().getItem(selected);
                GitlabSnippet snippet = (GitlabSnippet) item.getData();
                openEditorForContentOf(snippet);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private void openEditorForContentOf(GitlabSnippet snippet) {
        try {
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
            IEditorDescriptor editor = workbench.getEditorRegistry().getDefaultEditor(snippet.file_name);
            if (editor == null)
                editor = workbench.getEditorRegistry().findEditor("org.eclipse.ui.DefaultTextEditor");
            page.openEditor(new StringEditorInput(snippet.file_name, snippet.content), editor.getId());
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                int selected = viewer.getTable().getSelectionIndex();
                TableItem item = viewer.getTable().getItem(selected);
                GitlabSnippet snippet = (GitlabSnippet) item.getData();
                openEditorForContentOf(snippet);
            }
        });
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(refreshSnippetsAction);
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(refreshSnippetsAction);
    }

    public void refreshContent() {
        viewer.getControl().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if ((!viewer.getControl().isDisposed()) && (!viewer.getTable().isDisposed())) {
                    TableColumn[] columns = viewer.getTable().getColumns();
                    for (TableColumn column : columns)
                        column.dispose();
                    viewer.setInput(null);
                    viewer.getTable().setHeaderVisible(false);
                    viewer.getTable().setLinesVisible(false);
                    buildTable(viewer);
                }
            }
        });
    }

    protected List<GitlabSnippet> applyFileFilterPreferences(List<GitlabSnippet> snippets) {
        String preference = GitlabPlugin.getStoredValue(PreferenceConstants.P_GITLAB_SNIPPET_FILTER);
        if (!FileFilterPreferencesUtils.isToIncludeAllFiles(preference)) {
            List<String> filetypes = FileFilterPreferencesUtils.parse(preference);
            List<GitlabSnippet> filteredSnippets = new ArrayList<>();
            for (String filetype : filetypes) {
                filteredSnippets.addAll(
                        snippets.stream().filter(snippet -> !filetype.isEmpty() && snippet.file_name.endsWith(filetype))
                                .collect(Collectors.toList()));
            }
            return filteredSnippets;
        }
        return snippets;
    }
}
