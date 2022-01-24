package com.coretex.struct.psi;

import com.coretex.struct.gen.parser.psi.StructAttributeIdValue;
import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class StructNamedElementImpl extends StructElementImpl implements StructNamedElement {

	public StructNamedElementImpl(@NotNull ASTNode node) {
		super(node);
	}


	@Override
	public String getName() {
		return this.getText();
	}

	@Override
	public PsiElement setName(@NotNull String s) throws IncorrectOperationException {
		return this;
	}
	@Override
	public boolean isAttributeDeclaration() {
		return this.findChildByClass(PsiNamedElement.class) instanceof StructAttributeIdValue;
	}

	@Override
	public PsiReference getReference() {
		return new StructNameReference(this, ElementManipulators.getValueTextRange(this));
	}
}
