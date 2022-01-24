package com.coretex.struct;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PStructFileType extends LanguageFileType {

	private static final PStructFileType INSTANCE_PROXY = new PStructFileType();
	private static final StructFileType INSTANCE = StructFileType.getInstance();

	@Contract(pure = true)
	public static PStructFileType getInstance() {
		return INSTANCE_PROXY;
	}

	private PStructFileType() {
		super(StructLanguage.getInstance());
	}


	@Override
	public @NonNls @NotNull String getName() {
		return INSTANCE.getName();
	}

	@Override
	public @NlsContexts.Label @NotNull String getDescription() {
		return INSTANCE.getDescription();
	}

	@Override
	public @NlsSafe @NotNull String getDefaultExtension() {
		return INSTANCE.getDefaultExtension();
	}

	@Override
	public @Nullable Icon getIcon() {
		return StructIcons.FILE;
	}
}
