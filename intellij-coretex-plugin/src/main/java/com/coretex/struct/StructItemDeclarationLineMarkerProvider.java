package com.coretex.struct;

import com.coretex.struct.psi.StructUtil;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiNamedElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class StructItemDeclarationLineMarkerProvider extends RelatedItemLineMarkerProvider {

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element,
											@NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {

		if (element instanceof PsiClass) {
			Project project = element.getProject();
			PsiIdentifier el =  ((PsiClass) element).getNameIdentifier();
			if(Objects.nonNull(el)){
				String className = el.getText();
				String suffix = "";
				if (el.getText().endsWith(StructConstants.Enum.CLASS_SUFFIX)) {
					suffix = StructConstants.Enum.CLASS_SUFFIX;
				}
				if (el.getText().endsWith(StructConstants.Item.CLASS_SUFFIX)) {
					suffix = StructConstants.Item.CLASS_SUFFIX;
				}

				String finalClassName = StringUtils.removeEnd(className, suffix);
				List<PsiNamedElement> psiElements = StructUtil.findDeclaredElement(project, finalClassName, true);

				if (CollectionUtils.isNotEmpty(psiElements)) {
					NavigationGutterIconBuilder<PsiElement> builder =
							NavigationGutterIconBuilder.create(AllIcons.Gutter.WriteAccess)
									.setTargets(psiElements)
									.setTooltipText("Navigate to struct item declaration");
					result.add(builder.createLineMarkerInfo(el));
				}
			}
		}
	}

}
