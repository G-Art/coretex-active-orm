package com.coretex.validators;

import com.coretex.struct.bnf.StructParserDefinition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import org.intellij.grammar.LightPsi;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * **********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * **********************************************************************
 */
public class StructFileValidator {
    private StructParserDefinition definition;

    public StructFileValidator(StructParserDefinition definition) {
        this.definition = definition;
    }

    public void validateStructFile(File file, ErrorsAccumulator accumulator) {
        PsiFile res = null;
        try {
            res = LightPsi.parseFile(file, definition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collectErrors(res, accumulator);
    }

    public void validateStructFileText(String fileName, String text, ErrorsAccumulator accumulator) {
        PsiFile res = LightPsi.parseFile(fileName, text, definition);
        collectErrors(res, accumulator);
    }

    private static void collectErrors(
            PsiElement element,
            final ErrorsAccumulator errorProcessor) {
        if (errorProcessor != null) {
            element.accept(new PsiRecursiveElementWalkingVisitor() {

                @Override
                public void visitElement(@NotNull PsiElement element) {

                    super.visitElement(element);
                }

                @Override
                public void visitErrorElement(PsiErrorElement errorElement) {
                    errorProcessor.process(errorElement);
                    super.visitErrorElement(errorElement);
                }
            });
        }
    }


    public class ErrorsAccumulator {
        private final List<PsiErrorElement> errors = new ArrayList<>();

        void process(PsiErrorElement error) {
            errors.add(error);
        }

        public List<PsiErrorElement> errors() {
            return errors;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
