package com.coretex.struct.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

public interface StructNamedElementDeclaration extends PsiNamedElement, StructElement {


	boolean isEnhanced();


	PsiNamedElement getNameIdentifier();

	StructNamedElementDeclarationImpl.ElementType getElementType();
}
