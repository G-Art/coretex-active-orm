package com.coretex.struct;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 30-07-2017
 */
public class StructFileType extends LanguageFileType {

    private static final StructFileType INSTANCE = new StructFileType();

    @Contract(pure = true)
    public static StructFileType getInstance() {
        return INSTANCE;
    }

    private StructFileType() {
        super(StructLanguage.getInstance());
    }

    @Override
    public @NotNull String getName() {
        return "Struct file";
    }

    @Override
    public @NotNull String getDescription() {
        return "Struct language file";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "struct";
    }

    @Override
    public @Nullable Icon getIcon() {
        return StructIcons.FILE;
    }
}
