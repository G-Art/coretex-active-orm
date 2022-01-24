package com.coretex.struct;

import com.coretex.struct.psi.StructNamedElementDeclaration;
import com.coretex.struct.psi.StructNamedElementDeclarationImpl;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StructItemLineMarkerProvider extends RelatedItemLineMarkerProvider {

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element,
											@NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {


		// This must be an element with a literal expression as a parent
		if (element instanceof StructNamedElementDeclaration) {
			Project project = element.getProject();
			PsiNamedElement el = ((StructNamedElementDeclaration) element).getNameIdentifier();
			StructNamedElementDeclarationImpl.ElementType elementType = ((StructNamedElementDeclaration) element).getElementType();

			String suffix;
			switch (elementType) {
				case ITEM:
					suffix = StructConstants.Item.CLASS_SUFFIX;
					break;
				case ENUM:
					suffix = StructConstants.Enum.CLASS_SUFFIX;
					break;
				default:
					suffix = StringUtils.EMPTY;
			}

			@NotNull PsiClass[] classesByName = PsiShortNamesCache
					.getInstance(project)
					.getClassesByName(el.getName() + suffix, GlobalSearchScope.allScope(project));

			List<PsiElement> psiElements = Arrays.stream(classesByName)
					.map(PsiClass::getNameIdentifier)
					.filter(id -> id.getText().equals(el.getName() + suffix))
					.collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(psiElements)) {
				NavigationGutterIconBuilder<PsiElement> builder =
						NavigationGutterIconBuilder.create(AllIcons.Gutter.ReadAccess)
								.setTargets(psiElements)
								.setTooltipText("Navigate to generated java class");
				if (Objects.nonNull(el.getFirstChild())) {
					result.add(builder.createLineMarkerInfo(el.getFirstChild()));
				} else {
					result.add(builder.createLineMarkerInfo(el));
				}
			}
		}
	}

}
