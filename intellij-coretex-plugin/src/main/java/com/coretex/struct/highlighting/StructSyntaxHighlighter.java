package com.coretex.struct.highlighting;

import com.coretex.struct.gen.parser._StructLexer;
import com.coretex.struct.gen.parser.psi.StructAttributeIdValue;
import com.coretex.struct.gen.parser.psi.StructTypes;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;


public class StructSyntaxHighlighter extends SyntaxHighlighterBase {

	public static final TextAttributesKey STRUCT_KEY_WORD =
			createTextAttributesKey("STRUCT.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

	public static final TextAttributesKey STRUCT_FIELD =
			createTextAttributesKey("STRUCT.FIELD", DefaultLanguageHighlighterColors.STATIC_FIELD);

	public static final TextAttributesKey STRUCT_BOOLEAN =
			createTextAttributesKey("STRUCT.BOOLEAN", DefaultLanguageHighlighterColors.NUMBER);

	public static final TextAttributesKey STRUCT_STRING =
			createTextAttributesKey("STRUCT.STRING", DefaultLanguageHighlighterColors.STRING);

	public static final TextAttributesKey STRUCT_OPERATIONS =
			createTextAttributesKey("STRUCT.OPERATIONS", DefaultLanguageHighlighterColors.OPERATION_SIGN);

	public static final TextAttributesKey STRUCT_COMMA =
			createTextAttributesKey("STRUCT.COMMA", DefaultLanguageHighlighterColors.COMMA);

	public static final TextAttributesKey STRUCT_COMMENT =
			createTextAttributesKey("STRUCT.COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

	public static final TextAttributesKey STRUCT_ID_VALUE =
			createTextAttributesKey("STRUCT.ID_VALUE", DefaultLanguageHighlighterColors.IDENTIFIER);

	public static final TextAttributesKey STRUCT_ATTRIBUTE_ID =
			createTextAttributesKey("STRUCT.ATTRIBUTE_ID", DefaultLanguageHighlighterColors.INSTANCE_METHOD);

	public static final TextAttributesKey STRUCT_LABEL =
			createTextAttributesKey("STRUCT.LABEL", DefaultLanguageHighlighterColors.LABEL);

	public static final TextAttributesKey STRUCT_BRACKETS =
			createTextAttributesKey("STRUCT.BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);

	public static final TextAttributesKey STRUCT_PARENTHESES =
			createTextAttributesKey("STRUCT.PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);


	private static final Map<IElementType, TextAttributesKey> ourMap;

	static {
		ourMap = new HashMap<>();

		fillMap(ourMap, STRUCT_KEY_WORD,
				StructTypes.ELEMENTS,
				StructTypes.ITEMS,
				StructTypes.RELATIONS,
				StructTypes.ENUMS,
				StructTypes.ATTRIBUTES);

		fillMap(ourMap, STRUCT_ID_VALUE,
				StructTypes.ID);

		fillMap(ourMap, STRUCT_STRING,
				StructTypes.STRING_LITERAL);

		fillMap(ourMap, STRUCT_COMMA,
				StructTypes.COMMA);

		fillMap(ourMap, STRUCT_BOOLEAN,
				StructTypes.TRUE, StructTypes.FALSE);

		fillMap(ourMap, STRUCT_OPERATIONS,
				StructTypes.SET, StructTypes.EQUALS);

		fillMap(ourMap, STRUCT_ATTRIBUTE_ID,
				StructTypes.ATTRIBUTE_ID);

		fillMap(ourMap, STRUCT_BRACKETS,
				StructTypes.LEFT_BRACE, StructTypes.RIGHT_BRACE,
				StructTypes.LEFT_SQR_BRACE, StructTypes.RIGHT_SQR_BRACE);

		fillMap(ourMap, STRUCT_PARENTHESES,
				StructTypes.LEFT_PAREN, StructTypes.RIGHT_PAREN);

		fillMap(ourMap, STRUCT_COMMENT,
				StructTypes.COMMENT);

	}

	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new FlexAdapter(new _StructLexer());
	}

	@NotNull
	@Override
	public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
		if (ourMap.containsKey(tokenType)) {
			return pack(ourMap.get(tokenType));
		}
		return EMPTY_KEYS;
	}

}