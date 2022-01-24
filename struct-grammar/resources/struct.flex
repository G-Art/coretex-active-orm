package com.coretex.struct.gen.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.coretex.struct.gen.parser.psi.StructTypes.*;

%%

%{
  public _StructLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _StructLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
crlf                = (([\n])|([\r])|(\r\n))

WHITE_SPACE         = ([ \t\f]|{crlf})
STRING_LITERAL      ='([^'\\]|\\.)*'|\"([^\"\\]|\\.)*\"
ID                  = [A-Z][:jletterdigit:]+
ATTRIBUTE_ID        = [a-z][:jletterdigit:]+
COMMENT="//".*

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return WHITE_SPACE; }

  ":"                   { return SET; }
  "="                   { return EQUALS; }
  "("                   { return LEFT_PAREN; }
  ")"                   { return RIGHT_PAREN; }
  "{"                   { return LEFT_BRACE; }
  "}"                   { return RIGHT_BRACE; }
  "["                   { return LEFT_SQR_BRACE; }
  "]"                   { return RIGHT_SQR_BRACE; }
  ","                   { return COMMA; }
  "elements"            { return ELEMENTS; }
  "relations"           { return RELATIONS; }
  "items"               { return ITEMS; }
  "enums"               { return ENUMS; }
  "attributes"          { return ATTRIBUTES; }
  "true"                { return TRUE; }
  "false"               { return FALSE; }

  {COMMENT}             { return COMMENT; }
  {WHITE_SPACE}         { return WHITE_SPACE; }
  {STRING_LITERAL}      { return STRING_LITERAL; }
  {ID}                  { return ID; }
  {ATTRIBUTE_ID}        { return ATTRIBUTE_ID; }

}

[^] { return BAD_CHARACTER; }
