package com.coretex.struct.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class StructElementImpl extends ASTWrapperPsiElement implements StructElement{

	public StructElementImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public String toString() {
		final String className = getClass().getSimpleName();
		return StringUtil.trimEnd(className, "Impl");
	}
}
