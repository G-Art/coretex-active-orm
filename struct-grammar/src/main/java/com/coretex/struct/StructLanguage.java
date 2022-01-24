package com.coretex.struct;

import com.intellij.lang.Language;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 26-07-2017
 */
public class StructLanguage extends Language {

    private static final StructLanguage INSTANCE = new StructLanguage();

    public static StructLanguage getInstance() {
        return INSTANCE;
    }

    private StructLanguage() {
        super("Struct");
    }

}
