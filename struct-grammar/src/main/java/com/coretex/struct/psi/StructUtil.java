package com.coretex.struct.psi;

import com.coretex.struct.StructFile;
import com.coretex.struct.StructFileType;
import com.coretex.struct.gen.parser.psi.StructElementsExpr;
import com.coretex.struct.gen.parser.psi.StructItemAttrSetParamRule;
import com.coretex.struct.gen.parser.psi.StructTypes;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructUtil {

	public static final String ENHANCE = "enhance";

	public static List<PsiNamedElement> findDeclaredElement(Project project, String key, Boolean includeEnhanced){
		List<PsiNamedElement> result = new ArrayList<>();
		Collection<VirtualFile> virtualFiles =
				FileTypeIndex.getFiles(StructFileType.getInstance(), GlobalSearchScope.allScope(project));
		for (VirtualFile virtualFile : virtualFiles) {
			StructFile structFile= (StructFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (structFile != null) {
				StructElementsExpr elements = PsiTreeUtil.getChildOfType(structFile, StructElementsExpr.class);
				if (elements != null) {
					List<StructNamedElementDeclaration> declarations = new ArrayList<>();

					if(Objects.nonNull(elements.getRelItemsRule())){
						declarations.addAll(elements.getRelItemsRule().getRelItemDeclarationList());
					}

					if(Objects.nonNull(elements.getItemItemsRule())){
						declarations.addAll(elements.getItemItemsRule().getItemItemDeclarationList());
					}

					if(Objects.nonNull(elements.getEnumItemsExp())){
						declarations.addAll(elements.getEnumItemsExp().getEnumItemDeclarationList());
					}
					Stream<StructNamedElementDeclaration> elementDeclarationStream = declarations.stream();
					if(!includeEnhanced) {
						elementDeclarationStream = elementDeclarationStream.filter(Predicate.not(StructNamedElementDeclaration::isEnhanced));
					}
					List<StructNamedElementDeclaration> elementDeclarations = elementDeclarationStream.collect(Collectors.toList());
					for (StructNamedElementDeclaration declaration : elementDeclarations) {
						if (key.equals(declaration.getName())) {
							result.add(declaration);
						}
					}
				}
			}
		}
		return result;
	}

	public static List<PsiNamedElement> findDeclaredElement(Project project, String key){
		return findDeclaredElement(project, key, false);
	}

	public static List<PsiNamedElement> findDeclaredElements(Project project) {
		List<PsiNamedElement> declarations = new ArrayList<>();
		Collection<VirtualFile> virtualFiles =
				FileTypeIndex.getFiles(StructFileType.getInstance(), GlobalSearchScope.allScope(project));
		for (VirtualFile virtualFile : virtualFiles) {
			StructFile structFile = (StructFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (structFile != null) {
				StructElementsExpr elements = PsiTreeUtil.getChildOfType(structFile, StructElementsExpr.class);
				if (elements != null) {
					if (Objects.nonNull(elements.getRelItemsRule())) {
						declarations.addAll(elements.getRelItemsRule().getRelItemDeclarationList());
					}

					if (Objects.nonNull(elements.getItemItemsRule())) {
						declarations.addAll(elements.getItemItemsRule().getItemItemDeclarationList());
					}

					if (Objects.nonNull(elements.getEnumItemsExp())) {
						declarations.addAll(elements.getEnumItemsExp().getEnumItemDeclarationList());
					}
				}
			}
		}
		return declarations;
	}

	@NotNull
	public static PsiElement findFurthestSiblingOfSameType(@NotNull PsiElement anchor, boolean after) {
		ASTNode node = anchor.getNode();
		// Compare by node type to distinguish between different types of comments
		final IElementType expectedType = node.getElementType();
		ASTNode lastSeen = node;
		while (node != null) {
			final IElementType elementType = node.getElementType();
			if (elementType == expectedType) {
				lastSeen = node;
			}
			else if (elementType == TokenType.WHITE_SPACE) {
				if (expectedType == StructTypes.COMMENT && node.getText().indexOf('\n', 1) != -1) {
					break;
				}
			}
			else if (!StructTypes.COMMENT.equals(elementType) || StructTypes.COMMENT.equals(expectedType)) {
				break;
			}
			node = after ? node.getTreeNext() : node.getTreePrev();
		}
		return lastSeen.getPsi();
	}


	public static boolean isEnhance(StructItemAttrSetParamRule structItemAttrSetParamRule){
		return structItemAttrSetParamRule.getAttributeIdValue()
				.getText()
				.equalsIgnoreCase(ENHANCE);
	}
}
