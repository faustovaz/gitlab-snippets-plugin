package me.faustovaz.plugin.gitlab.view.snippet.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class StringEditorInput implements IStorageEditorInput {

    private final String _inputString;
    private String _title;
    private String _tooltip;
    private ImageDescriptor _imageDescriptor;

    public StringEditorInput(String title, String inputString) {
        _title = title;
        _inputString = inputString;
    }

    public String getTooltip() {
        return _tooltip;
    }

    public void setTooltip(String tooltip) {
        _tooltip = tooltip;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return _imageDescriptor;
    }

    public void setImageDescriptor(ImageDescriptor imageDescriptor) {
        _imageDescriptor = imageDescriptor;
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {

        return null;

    }

    @Override
    public String getName() {
        return _title;
    }

    @Override
    public String getToolTipText() {
        return _tooltip;

    }

    @Override
    public IStorage getStorage() throws CoreException {
        return new IStorage() {

            @Override
            public InputStream getContents() throws CoreException {
                return new ByteArrayInputStream(_inputString.getBytes());
            }

            @Override
            public IPath getFullPath() {
                return null;
            }

            @Override
            public String getName() {
                return StringEditorInput.this.getName();
            }

            @Override
            public boolean isReadOnly() {
                return true;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
                return null;

            }
        };
    }

}
