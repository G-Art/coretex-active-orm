package com.coretex.struct;

import com.coretex.struct.gen.parser.psi.StructElementsExpr;
import com.coretex.struct.gen.parser.psi.StructItemItemParamRule;
import com.coretex.struct.gen.parser.psi.StructParamSetterAttrRule;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.PlatformPatterns.psiFile;

public class StructCompletionContributor extends CompletionContributor {
	public StructCompletionContributor() {
		Map<ElementPattern<? extends PsiElement>, String> keywordContribution = new HashMap<>();
		keywordContribution.put(psiElement().withSuperParent(2, psiFile()).andNot(psiElement().withParent(StructElementsExpr.class)), StructConstants.Common.ELEMENTS_KEYWORD);
		keywordContribution.put(psiElement().withSuperParent(2, StructElementsExpr.class), StructConstants.Common.ITEMS_KEYWORD);
		keywordContribution.put(psiElement().withSuperParent(2, StructElementsExpr.class), StructConstants.Common.RELATIONS_KEYWORD);
		keywordContribution.put(psiElement().withSuperParent(2, StructElementsExpr.class), StructConstants.Common.ENUMS_KEYWORD);
		keywordContribution.put(
				psiElement()
						.withSuperParent(2, StructItemItemParamRule.class),
				StructConstants.Common.ATTRIBUTES_KEYWORD);

		keywordContribution.forEach((k, v) -> extend(CompletionType.BASIC,
				k,
				new KeywordsCompletionProvider(v)));
	}

	@Override
	public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
		super.fillCompletionVariants(parameters, result);
	}



	private static class KeywordsCompletionProvider extends CompletionProvider<CompletionParameters> {
		private final String keyword;

		public KeywordsCompletionProvider(String keyword) {
			this.keyword = keyword;
		}

		@Override
		protected void addCompletions(@NotNull CompletionParameters parameters,
									  @NotNull ProcessingContext context,
									  @NotNull CompletionResultSet result) {
			result.withPrefixMatcher("")
			.addElement(LookupElementBuilder.create(keyword));
		}
	}
}
