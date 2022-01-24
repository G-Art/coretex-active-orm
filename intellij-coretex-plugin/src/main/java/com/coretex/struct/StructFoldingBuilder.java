package com.coretex.struct;

import com.coretex.struct.gen.parser.psi.StructTypes;
import com.coretex.struct.psi.StructElementType;
import com.coretex.struct.psi.StructNamedElementDeclaration;
import com.coretex.struct.psi.StructUtil;
import com.intellij.json.psi.JsonPsiUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructFoldingBuilder implements FoldingBuilder, DumbAware {
	@Override
	public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
		final List<FoldingDescriptor> descriptors = new ArrayList<>();
		collectDescriptorsRecursively(node, document, descriptors);
		return descriptors.toArray(FoldingDescriptor.EMPTY);
	}

	private static void collectDescriptorsRecursively(@NotNull ASTNode node,
													  @NotNull Document document,
													  @NotNull List<FoldingDescriptor> descriptors) {
		final IElementType type = node.getElementType();
		if ((type == StructTypes.ELEMENTS_EXPR ||
				type == StructTypes.ITEM_ITEMS_RULE ||
				type == StructTypes.REL_ITEMS_RULE ||
				type == StructTypes.ENUM_ITEMS_EXP ||

				type == StructTypes.ENUM_ITEM_DECLARATION||
				type == StructTypes.ITEM_ITEM_DECLARATION||
				type == StructTypes.REL_ITEM_DECLARATION ||

				type == StructTypes.ITEM_ATTRS_RULE ||
				type == StructTypes.REL_PARAM_SOURCE_RULE||
				type == StructTypes.REL_PARAM_TARGET_RULE ||
				type == StructTypes.ITEM_ATTR_DCLR_RULE)

				&& spanMultipleLines(node, document)) {
			descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
		}
		else if (type == StructTypes.COMMENT) {
			final Couple<PsiElement> commentRange = expandLineCommentsRange(node.getPsi());
			final int startOffset = commentRange.getFirst().getTextRange().getStartOffset();
			final int endOffset = commentRange.getSecond().getTextRange().getEndOffset();
			if (document.getLineNumber(startOffset) != document.getLineNumber(endOffset)) {
				descriptors.add(new FoldingDescriptor(node, new TextRange(startOffset, endOffset)));
			}
		}

		for (ASTNode child : node.getChildren(null)) {
			collectDescriptorsRecursively(child, document, descriptors);
		}
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode node) {
		final IElementType type = node.getElementType();
		if (type == StructTypes.ENUM_ITEM_DECLARATION||
				type == StructTypes.ITEM_ITEM_DECLARATION||
				type == StructTypes.REL_ITEM_DECLARATION ) {
			final PsiNamedElement object = node.getFirstChildNode().getPsi(PsiNamedElement.class);
			final String name = object.getText();

			if (name != null) {
				return "{" + name + "...}";
			}
			return "{...}";
		}
		else if (type == StructTypes.INDEX_ARRAY) {
			return "[...]";
		}
		else if (type == StructTypes.COMMENT) {
			return "//...";
		}
		String text = node.getFirstChildNode().getText();
		if(StringUtils.isNotBlank(text)){
			return "{" + text + "...}";
		}
		return "...";
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node) {
		return false;
	}

	@NotNull
	public static Couple<PsiElement> expandLineCommentsRange(@NotNull PsiElement anchor) {
		return Couple.of(StructUtil.findFurthestSiblingOfSameType(anchor, false), StructUtil.findFurthestSiblingOfSameType(anchor, true));
	}

	private static boolean spanMultipleLines(@NotNull ASTNode node, @NotNull Document document) {
		final TextRange range = node.getTextRange();
		int endOffset = range.getEndOffset();
		return document.getLineNumber(range.getStartOffset())
				< (endOffset < document.getTextLength() ? document.getLineNumber(endOffset) : document.getLineCount() - 1);
	}
}
