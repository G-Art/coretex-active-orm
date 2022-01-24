package com.coretex.struct.psi;

import com.coretex.struct.StructIcons;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructNameReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

	StructElement structElement;

	public StructNameReference(StructElement structElement, TextRange textRange) {
		super(structElement, textRange);
		this.structElement = structElement;
	}

	@NotNull
	@Override
	public TextRange getRangeInElement() {
		return ElementManipulators.getValueTextRange(structElement);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		final ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}

	@Override
	public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
		final Project project = myElement.getProject();
		final String key = myElement.getText();
		List<PsiNamedElement> result = StructUtil.findDeclaredElement(project, key);
		return PsiElementResolveResult.createResults(result);

	}

	@Override
	public Object @NotNull [] getVariants() {
		Project project = myElement.getProject();
		List<PsiNamedElement> properties = StructUtil.findDeclaredElements(project);
		List<LookupElement> variants = new ArrayList<>();
		for (final PsiNamedElement property : properties) {
			if (property.getName() != null && property.getName().length() > 0) {
				variants.add(LookupElementBuilder
						.create(property).withIcon(StructIcons.FILE)
						.withTypeText(property.getContainingFile().getName())
				);
			}
		}
		return variants.toArray();
	}
}
