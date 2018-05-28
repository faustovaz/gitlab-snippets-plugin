package me.faustovaz.plugin.gitlab.view.snippet;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

public class SnippetContentProvider implements IStructuredContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        return ((List<?>) inputElement).toArray();
    }

}
