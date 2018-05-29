package me.faustovaz.plugin.gitlab.action.snippet.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.gitlab4j.api.models.Snippet;

import me.faustovaz.plugin.gitlab.wizard.CustomWizardDialog;
import me.faustovaz.plugin.gitlab.wizard.snippet.CreateSnippetWizard;

public class CreateSnippetHandler implements IHandler {

    @Override
    public void addHandlerListener(IHandlerListener handlerListener) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWizardDescriptor wizard = workbench.getNewWizardRegistry()
                .findWizard(CreateSnippetWizard.ID);
        try {
            IWorkbenchWizard createdWizard = wizard.createWizard();
            CustomWizardDialog wizardDialog = new CustomWizardDialog(workbench.getDisplay().getActiveShell(),
                    createdWizard);
            wizardDialog.setSnippet(getSnippetFromSelection(event));
            wizardDialog.open();
        } catch (CoreException e) {
            MessageDialog.openError(HandlerUtil.getActiveShell(event), "Error",
                    "It was not possible to open the Gitlab Snippet wizard");
        }
        return null;
    }

    public Snippet getSnippetFromSelection(ExecutionEvent event) {
        Snippet snippet = new Snippet();
        if (hasTextSelection(event)) {
            String textSelection = getTextSelection(event).getText();
            if ((textSelection != null) && (!textSelection.isEmpty())) {
                String filename = getSelectedTextFileName(event);
                snippet = new Snippet(filename, filename, textSelection);
            }
        }
        return snippet;
    }

    @Override
    public boolean isEnabled() {
        ISelection selection = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getSelectionService().getSelection();
        if (selection instanceof ITextSelection) {
           String textSelection = ((ITextSelection) selection).getText();
           return (textSelection != null) && (!textSelection.isEmpty());
        }
        return false;
    }

    @Override
    public boolean isHandled() {
        return true;
    }

    @Override
    public void removeHandlerListener(IHandlerListener handlerListener) {

    }

    public Boolean hasTextSelection(ExecutionEvent event) {
        return getTextSelection(event) != null;
    }

    public ITextSelection getTextSelection(ExecutionEvent event) {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        ISelection selection = part.getSite().getSelectionProvider().getSelection();
        return selection instanceof ITextSelection ? (ITextSelection) selection : null;
    }

    public String getSelectedTextFileName(ExecutionEvent event) {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        String filename = part.getSite().getPage().getActiveEditor().getEditorInput().getName();
        if (filename != null && !filename.isEmpty())
            return filename;
        return "No-Named-Snippet";
    }

}
