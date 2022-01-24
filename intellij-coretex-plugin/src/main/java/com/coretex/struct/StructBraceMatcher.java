package com.coretex.struct;

import com.coretex.struct.gen.parser.psi.StructTypes;
import com.coretex.struct.psi.StructElementType;
import com.coretex.struct.psi.StructTokenType;
import com.intellij.json.JsonElementTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructBraceMatcher  implements PairedBraceMatcher {
	private static final BracePair[] PAIRS = {
			new BracePair(StructTypes.LEFT_BRACE, StructTypes.RIGHT_BRACE, true),
			new BracePair(StructTypes.LEFT_SQR_BRACE, StructTypes.RIGHT_SQR_BRACE, true),
			new BracePair(StructTypes.LEFT_PAREN, StructTypes.RIGHT_PAREN, true)
	};

	@Override
	public BracePair @NotNull [] getPairs() {
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		return openingBraceOffset;
	}
}