package com.coretex.struct;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CreateStructFileAction extends CreateFileFromTemplateAction {

	@NonNls
	private static final String DEFAULT_STRUCT_TEMPLATE_PROPERTY = "DefaultStructFileTemplate";

	public CreateStructFileAction() {
		super("New Struct File", "Create new struct file",
				StructFileType.getInstance().getIcon());
	}

	@Override
	protected String getDefaultTemplateProperty() {
		return DEFAULT_STRUCT_TEMPLATE_PROPERTY;
	}

	@Override
	protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
		builder
				.setTitle("New Struct File")
				.addKind("Struct file", StructFileType.getInstance().getIcon(), "Struct file");
	}

	@Override
	protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
		return "Struct File";
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof CreateStructFileAction;
	}
}
