package com.coretex.struct.highlighting;

import com.coretex.struct.gen.parser.psi.StructAttributeIdValue;
import com.coretex.struct.gen.parser.psi.StructItemAttrSetParamRule;
import com.coretex.struct.gen.parser.psi.StructParamSetterAttrRule;
import com.coretex.struct.psi.StructElement;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StructAnnotator implements Annotator {

	public StructAnnotator() {
	}

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		if (!(element instanceof StructElement)) {
			return;
		}

		StructAttributeIdValue attributeIdValue =null;
		if(element instanceof StructParamSetterAttrRule){
			attributeIdValue = ((StructParamSetterAttrRule) element).getAttributeIdValue();

		}
		if(element instanceof StructItemAttrSetParamRule){
			attributeIdValue = ((StructItemAttrSetParamRule) element).getAttributeIdValue();
		}

		if(Objects.nonNull(attributeIdValue)){
			holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
					.range(attributeIdValue.getTextRange()).textAttributes(DefaultLanguageHighlighterColors.STATIC_FIELD)
					.create();
		}
	}
}
