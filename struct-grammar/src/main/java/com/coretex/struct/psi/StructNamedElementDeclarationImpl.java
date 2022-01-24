package com.coretex.struct.psi;

import com.coretex.struct.gen.parser.psi.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StructNamedElementDeclarationImpl extends StructElementImpl implements StructNamedElementDeclaration {

	public StructNamedElementDeclarationImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public PsiElement setName(@NotNull String s) throws IncorrectOperationException {
		return this;
	}

	@Override
	public String getName() {
		return Objects.requireNonNull(this.findChildByClass(PsiNamedElement.class)).getText();
	}


	@Override
	public boolean isEnhanced() {
		if (this instanceof StructItemItemDeclaration) {
			StructItemAttrtsRule itemAttrtsRule = ((StructItemItemDeclaration) this).getItemAttrtsRule();
			return Objects.nonNull(itemAttrtsRule) &&
					Objects.nonNull(itemAttrtsRule.getItemAttrSetParamsRule()) &&
					hasEnhance(itemAttrtsRule.getItemAttrSetParamsRule());

		}
		if (this instanceof StructEnumItemDeclaration) {
			StructEnumAttrtsRule enumAttrtsRule = ((StructEnumItemDeclaration) this).getEnumAttrtsRule();
			return Objects.nonNull(enumAttrtsRule) && Objects.nonNull(enumAttrtsRule.getItemAttrSetParamRule());
		}
		return false;
	}

	private boolean hasEnhance(StructItemAttrSetParamsRule itemAttrSetParamsRule) {
		boolean enhance = StructUtil.isEnhance(itemAttrSetParamsRule.getItemAttrSetParamRule());
		if (enhance) {
			return enhance;
		} else {
			if (!itemAttrSetParamsRule.getItemAttrSetParamsRuleList().isEmpty()) {
				for (StructItemAttrSetParamsRule structItemAttrSetParamsRule : itemAttrSetParamsRule.getItemAttrSetParamsRuleList()) {
					return hasEnhance(structItemAttrSetParamsRule);
				}
			}
		}
		return false;
	}

	@Override
	public PsiNamedElement getNameIdentifier() {
		return this.findChildByClass(PsiNamedElement.class);
	}

	@Override
	public ElementType getElementType(){
		return ElementType.typeOfElement(this);
	}

	@Override
	public PsiReference getReference() {
		return new StructNameReference(this, ElementManipulators.getValueTextRange(getNameIdentifier()));
	}

	public enum ElementType {
		RELATION, ITEM, ENUM;

		public static ElementType typeOfElement(StructNamedElementDeclaration elementDeclaration) {
			if (elementDeclaration instanceof StructRelItemDeclaration) {
				return RELATION;
			}
			if (elementDeclaration instanceof StructItemItemDeclaration) {
				return ITEM;
			}
			if (elementDeclaration instanceof StructEnumItemDeclaration) {
				return ENUM;
			}
			throw new IllegalArgumentException("Unknown item type declaration [ " + elementDeclaration.getClass().getName() + " ]");
		}
	}
}
