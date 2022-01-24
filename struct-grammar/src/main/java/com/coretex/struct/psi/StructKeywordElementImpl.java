package com.coretex.struct.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class StructKeywordElementImpl extends StructElementImpl implements StructKeywordElement{

	public StructKeywordElementImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public String toString() {
		final String className = getClass().getSimpleName();
		return StringUtil.trimEnd(className, "Impl");
	}
}
