package com.coretex.build.parsers

import com.coretex.struct.gen.parser.psi.*
import com.intellij.psi.PsiFile
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.jetbrains.annotations.NotNull

class ParseItemsStructVisitor extends StructVisitor implements AccumulatedItemStructVisitor {

    private static final Logger LOG = Logging.getLogger(ParseItemsStructVisitor)

    @Override
    void visitFile(PsiFile file) {
        file.getFirstChild().accept(this)
    }

    @Override
    void visitElementsExpr(@NotNull StructElementsExpr structElementsExpr) {

        def enumItemsExp = structElementsExpr.getEnumItemsExp()
        if (Objects.nonNull(enumItemsExp)) {
            def visitor = new ParseEnumItemStructVisitor(accumulator: accumulator)
            LOG.debug("Parsing enum struct elements")
            enumItemsExp.getEnumItemDeclarationList()
                    .each { StructEnumItemDeclaration enumItem ->
                        enumItem.accept(visitor)
                    }
        }

        def itemItemsRule = structElementsExpr.getItemItemsRule()
        if (Objects.nonNull(itemItemsRule)) {
            def visitor = new ParseItemStructVisitor(accumulator: accumulator)
            LOG.debug("Parsing item struct elements")
            itemItemsRule.getItemItemDeclarationList()
                    .each { StructItemItemDeclaration enumItem ->
                        enumItem.accept(visitor)
                    }
        }

        def relItemsRule = structElementsExpr.getRelItemsRule()
        if (Objects.nonNull(relItemsRule)) {
            def visitor = new ParseRelItemStructVisitor(accumulator: accumulator)
            LOG.debug("Parsing relation struct elements")
            relItemsRule.getRelItemDeclarationList()
                    .each { StructRelItemDeclaration enumItem ->
                        enumItem.accept(visitor)
                    }
        }
    }
}
