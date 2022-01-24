package com.coretex.struct;

import com.coretex.struct.gen.parser.psi.StructStringLiteralValue;
import com.coretex.struct.gen.parser.psi.StructTypes;
import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler;
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructQuoteHandler extends SimpleTokenSetQuoteHandler implements MultiCharQuoteHandler {
	public StructQuoteHandler() {
		super(StructTypes.STRING_LITERAL);
	}

	@Nullable
	@Override
	public CharSequence getClosingQuote(@NotNull HighlighterIterator iterator, int offset) {
		final IElementType tokenType = iterator.getTokenType();
		if (tokenType == TokenType.WHITE_SPACE) {
			final int index = iterator.getStart() - 1;
			if (index >= 0) {
				return String.valueOf(iterator.getDocument().getCharsSequence().charAt(index));
			}
		}
		return String.valueOf(iterator.getDocument().getCharsSequence().charAt(iterator.getStart()));
	}

	@Override
	public void insertClosingQuote(@NotNull Editor editor, int offset, @NotNull PsiFile file, @NotNull CharSequence closingQuote) {
		PsiElement element = file.findElementAt(offset - 1);
		PsiElement parent = element == null ? null : element.getParent();
		if (parent instanceof StructStringLiteralValue) {
			PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
			TextRange range = parent.getTextRange();
			if (offset - 1 != range.getStartOffset() || !"\"".contentEquals(closingQuote)) {
				int endOffset = range.getEndOffset();
				if (offset < endOffset) return;
				if (offset == endOffset && !StringUtil.isEmpty(parent.getText())) return;
			}
		}
		editor.getDocument().insertString(offset, closingQuote);
	}
}