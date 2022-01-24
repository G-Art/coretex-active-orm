package com.coretex.struct.bnf;

import com.coretex.struct.StructFile;
import com.coretex.struct.StructLanguage;
import com.coretex.struct.gen.parser.StructParser;
import com.coretex.struct.gen.parser._StructLexer;
import com.coretex.struct.gen.parser.psi.StructTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.ParserDefinition.SpaceRequirements.MAY;

/**
 * **********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com
 * <p>
 * !!!!!Do not convert this java file to groovy, due to compilation error!!!!!
 * **********************************************************************
 */
public class StructParserDefinition implements ParserDefinition {

    private static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);

    private static final TokenSet COMMENTS = TokenSet.create(StructTypes.COMMENT);


    private static final IFileElementType FILE = new IFileElementType(StructLanguage.getInstance());

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new _StructLexer());
    }

    @Override
    public PsiParser createParser(Project project) {
        return new StructParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(StructTypes.STRING_LITERAL);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode astNode) {
        return StructTypes.Factory.createElement(astNode);
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new StructFile(fileViewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
        return MAY;
    }
}
