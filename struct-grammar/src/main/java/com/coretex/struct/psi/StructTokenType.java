package com.coretex.struct.psi;

import com.coretex.struct.StructLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 05-10-2017
 */
public class StructTokenType extends IElementType {

    public StructTokenType(@NotNull @NonNls final String debugName) {
        super(debugName, StructLanguage.getInstance());
    }

    @Override
    public String toString() {
        final String name = super.toString();

        if (name == null) {
            return "_empty_";
        }

        final String fixedName = name.toLowerCase().replaceAll(" ", "");

        return "<" + fixedName + '>';
    }
}
