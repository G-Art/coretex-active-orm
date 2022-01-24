package com.coretex.build.parsers

import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.EnumValue
import com.coretex.struct.gen.parser.psi.StructEnumItemDeclaration
import com.coretex.struct.gen.parser.psi.StructItemAttrSetParamRule
import com.coretex.struct.gen.parser.psi.StructVisitor
import org.jetbrains.annotations.NotNull

import static com.coretex.build.parsers.ParserUtils.unquoteString

class ParseEnumItemStructVisitor extends StructVisitor implements AccumulatedItemStructVisitor {

    @Override
    void visitEnumItemDeclaration(@NotNull StructEnumItemDeclaration enumItemExp) {
        String code = enumItemExp.idValue.text
        EnumItem item = new EnumItem(code)

        extractEnhanceAttr(enumItemExp.enumAttrtsRule?.itemAttrSetParamRule, item)

        enumItemExp.attributeIdValueList?.each {
            EnumValue ev= new EnumValue(it.text)
            ev.owner = item
            item.addValue(ev)
        }

        accumulator.addItem(item)
    }

    void extractEnhanceAttr(StructItemAttrSetParamRule attrSetParamRule, EnumItem enumItem) {
        if (Objects.nonNull(attrSetParamRule)) {
            enumItem.enhance = unquoteString(attrSetParamRule.stringLiteralValue?.text)
        }
    }
}
