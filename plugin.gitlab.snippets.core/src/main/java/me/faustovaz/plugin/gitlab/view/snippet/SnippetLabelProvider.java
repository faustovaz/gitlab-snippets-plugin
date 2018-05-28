package me.faustovaz.plugin.gitlab.view.snippet;

import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.gitlab4j.api.models.Snippet;

import me.faustovaz.plugin.gitlab.snippets.ImageUtils;
import me.faustovaz.plugin.gitlab.snippets.SnippetUtils;


public class SnippetLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Snippet snippet = (Snippet) element;
		if (columnIndex == 0) {
			Optional<ImageDescriptor> optionalImage = ImageUtils
	                                       .getFileIcon(SnippetUtils.fileType(snippet.getFileName()));
			if (optionalImage.isPresent())
				return optionalImage.get().createImage();
				
			Optional<ImageDescriptor> defaultImage = ImageUtils.getDefaultFileIcon();
			if (defaultImage.isPresent())
				return defaultImage.get().createImage();
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
	    Snippet snippet = (Snippet) element;
		if(columnIndex == 1) return snippet.getFileName();
		if(columnIndex == 2) return snippet.getTitle();
		if(columnIndex == 3) return snippet.getDescription();
		return "";
	}

}
