package com.coretex.struct.highlighting;

import com.coretex.struct.StructBundle;
import com.coretex.struct.StructIcons;
import com.coretex.struct.StructLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.RainbowColorSettingsPage;
import com.intellij.psi.codeStyle.DisplayPriority;
import com.intellij.psi.codeStyle.DisplayPrioritySortable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

import static com.coretex.struct.highlighting.StructSyntaxHighlighter.*;

public class StructColorsPage implements RainbowColorSettingsPage, DisplayPrioritySortable {

	private static final AttributesDescriptor[] ourAttributeDescriptors = new AttributesDescriptor[]{
			new AttributesDescriptor(StructBundle.message("color.page.attribute.brackets"), STRUCT_BRACKETS),
			new AttributesDescriptor(StructBundle.message("color.page.attribute.comma"), STRUCT_COMMA),
			new AttributesDescriptor(StructBundle.message("color.page.attribute.string"), STRUCT_STRING),
			new AttributesDescriptor(StructBundle.message("color.page.attribute.keyword"), STRUCT_KEY_WORD),
			new AttributesDescriptor(StructBundle.message("color.page.attribute.line.comment"), STRUCT_COMMENT)
	};

	@Override
	public boolean isRainbowType(TextAttributesKey type) {
		return STRUCT_BRACKETS.equals(type)
				|| STRUCT_STRING.equals(type)
				|| STRUCT_KEY_WORD.equals(type);
	}

	@Override
	public @Nullable Language getLanguage() {
		return StructLanguage.getInstance();
	}

	@Override
	public @Nullable Icon getIcon() {
		return StructIcons.FILE;
	}

	@Override
	public @NotNull SyntaxHighlighter getHighlighter() {
		return SyntaxHighlighterFactory.getSyntaxHighlighter(StructLanguage.getInstance(), null, null);
	}

	@Override
	public @NonNls @NotNull String getDemoText() {
		return "elements {\n" +
				"    relations {\n" +
				"        GenericMetaTypeRelation(source: Generic, target: MetaType) {\n" +
				"            source(metaType) {\n" +
				"                description = \"Relation to meta type description\"\n" +
				"                optional = false\n" +
				"            }\n" +
				"            target(instances) {\n" +
				"                description = \"List of all of instances of this type\"\n" +
				"                containerType = List\n" +
				"            }\n" +
				"        }\n" +
				"        MetaAttributeOwnerRelation(source: MetaAttributeType, target: MetaType) {\n" +
				"            source(owner) {\n" +
				"                description = \"Owner of this attribute\"\n" +
				"                optional = false\n" +
				"            }\n" +
				"            target(itemAttributes) {\n" +
				"                description = \"Super Type uuid\"\n" +
				"                containerType = Set\n" +
				"            }\n" +
				"        }\n" +
				"    }\n" +
				"\n" +
				"    items {\n" +
				"        Generic(extend: \"\", abstract: true) {\n" +
				"            description = \"Generic item\"\n" +
				"            attributes {\n" +
				"                createDate(LocalDateTime) {\n" +
				"                    description = \"Creation date\"\n" +
				"                }\n" +
				"\n" +
				"                updateDate(LocalDateTime) {\n" +
				"                    description = \"Updating date\"\n" +
				"                }\n" +
				"            }\n" +
				"        }\n" +
				"\n" +
				"        RegularType {\n" +
				"            description = \"Regular item\"\n" +
				"            attributes {\n" +
				"                dbType(String) {\n" +
				"                    description = \"Sql type representation\"\n" +
				"                    optional = false\n" +
				"                }\n" +
				"\n" +
				"                regularItemCode(String) {\n" +
				"                    description = \"Java type representation\"\n" +
				"                    unique = true\n" +
				"                    optional = false\n" +
				"                }\n" +
				"                regularClass(Class) {\n" +
				"                    description = \"Java class type representation\"\n" +
				"                    optional = false\n" +
				"                }\n" +
				"\n" +
				"                persistenceType(String) {\n" +
				"                    description = \"Persistence value qualifier\"\n" +
				"                }\n" +
				"\n" +
				"                columnSize(Integer) {\n" +
				"                    description = \"Persistence value qualifier\"\n" +
				"                }\n" +
				"            }\n" +
				"        }\n" +
				"\n" +
				"        MetaRelationType(extend: MetaType, table: false) {\n" +
				"            description = \"Relation type holds information of relations between items\"\n" +
				"\n" +
				"            attributes {\n" +
				"                sourceType(MetaType) {\n" +
				"                    description = \"Specify type of source insistence\"\n" +
				"                }\n" +
				"\n" +
				"                sourceAttribute(MetaAttributeType) {\n" +
				"                    description = \"Specify attribute of source relation owner\"\n" +
				"                }\n" +
				"\n" +
				"                targetType(MetaType) {\n" +
				"                    description = \"Specify type of target insistence\"\n" +
				"                }\n" +
				"\n" +
				"                targetAttribute(MetaAttributeType) {\n" +
				"                    description = \"Specify attribute of target relation owner\"\n" +
				"                }\n" +
				"\n" +
				"                source(Generic) {\n" +
				"\n" +
				"                }\n" +
				"                target(Generic) {\n" +
				"\n" +
				"                }\n" +
				"            }\n" +
				"        }\n" +
				"    }\n" +
				"\n" +
				"    enums {\n" +
				"        Example {\n" +
				"            test1\n" +
				"            test2\n" +
				"            test3\n" +
				"        }\n" +
				"    }\n" +
				"}";
	}

	@Override
	public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return Map.of();
	}

	@Override
	public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
		return ourAttributeDescriptors;
	}

	@Override
	public ColorDescriptor @NotNull [] getColorDescriptors() {
		return  ColorDescriptor.EMPTY_ARRAY;
	}

	@Override
	public @NotNull String getDisplayName() {
		return StructBundle.message("settings.display.name.struct");
	}

	@Override
	public DisplayPriority getPriority() {
		return DisplayPriority.LANGUAGE_SETTINGS;
	}
}
