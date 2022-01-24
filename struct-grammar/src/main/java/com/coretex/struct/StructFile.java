package com.coretex.struct;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 30-07-2017
 */
public class StructFile extends PsiFileBase {

    public StructFile(@NotNull final FileViewProvider viewProvider) {
        super(viewProvider, StructLanguage.getInstance());
    }

    @Override
    public @NotNull FileType getFileType() {
        return StructFileType.getInstance();
    }

    @Override
    public @NotNull String toString() {
        return "Struct file";
    }
}
