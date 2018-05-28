package me.faustovaz.plugin.gitlab.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.gitlab4j.api.models.Snippet;
import org.gitlab4j.api.models.Visibility;

public class CreateSnippetWizardPage extends WizardPage {

    private Text title;
    private Text filename;
    private Text description;
    private Button publicView;
    private Button privateView;
    private Button internalView;
    private Text codeSnippet;
    private Composite container;
    private Snippet snippet;

    public CreateSnippetWizardPage() {
        super("Create Gitlab Snippet");
        setTitle("Gitlab Snippet");
        setDescription("Create a new gitlab snippet");
        setControl(title);
    }

    protected CreateSnippetWizardPage(String pageName) {
        super(pageName);
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        container.setLayout(layout);

        Label lblTitle = new Label(container, SWT.NONE);
        lblTitle.setText("Title*");
        title = new Text(container, SWT.SINGLE);
        title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        title.setText(snippet.getTitle());

        Label lblFilename = new Label(container, SWT.NONE);
        lblFilename.setText("Filename*");
        filename = new Text(container, SWT.SINGLE);
        filename.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        filename.setText(snippet.getFileName());

        Label lblDescription = new Label(container, SWT.NONE);
        lblDescription.setText("Description");
        description = new Text(container, SWT.SINGLE);
        description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        description.setText("");

        Label lblVisibility = new Label(container, SWT.NONE);
        lblVisibility.setText("Visibility");
        Composite radioGroup = new Composite(container, SWT.NONE);
        radioGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
        publicView = new Button(radioGroup, SWT.RADIO);
        publicView.setText("Public");
        publicView.setSelection(true);
        internalView = new Button(radioGroup, SWT.RADIO);
        internalView.setText("Internal");
        privateView = new Button(radioGroup, SWT.RADIO);
        privateView.setText("Private");

        Label lblSnippet = new Label(container, SWT.NONE);
        lblSnippet.setText("Snippet*");
        codeSnippet = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        codeSnippet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        codeSnippet.setText(snippet.getContent());

        container.getShell().setSize(600, 650);

        Rectangle parentBound = getShell().getParent().getShell().getBounds();
        Rectangle containerBound = getShell().getBounds();

        int containerLocationX = ((parentBound.width - containerBound.width) / 2) + parentBound.x;
        int containerLocationy = ((parentBound.height - containerBound.height) / 2) + parentBound.y;

        getShell().setLocation(containerLocationX, containerLocationy);
        getShell().layout(true);

        setControl(container);
        setPageComplete(true);
    }

    public Snippet getSnippet() {
        Snippet snippet = new Snippet();
        snippet.setTitle(title.getText());
        snippet.setFileName(filename.getText());
        snippet.setDescription(description.getText());

        if (publicView.getSelection())
            snippet.setVisibility(Visibility.PUBLIC);
        if (internalView.getSelection())
            snippet.setVisibility(Visibility.INTERNAL);
        if (privateView.getSelection())
            snippet.setVisibility(Visibility.PRIVATE);

        snippet.setContent(codeSnippet.getText());
        this.snippet = snippet;
        return this.snippet;
    }
}
