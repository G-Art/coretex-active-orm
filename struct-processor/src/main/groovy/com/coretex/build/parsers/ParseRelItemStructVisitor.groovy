package com.coretex.build.parsers

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.struct.StructConstants
import com.coretex.struct.gen.parser.psi.StructParamSetterAttrRule
import com.coretex.struct.gen.parser.psi.StructRelItemDeclaration
import com.coretex.struct.gen.parser.psi.StructRelParamAttributes
import com.coretex.struct.gen.parser.psi.StructVisitor
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.BooleanUtils

import static com.coretex.build.parsers.ParserUtils.unquoteString
import static com.coretex.struct.StructConstants.Relation.SOURCE_ATTRIBUTE
import static com.coretex.struct.StructConstants.Relation.TARGET_ATTRIBUTE

class ParseRelItemStructVisitor extends StructVisitor implements AccumulatedItemStructVisitor {

    @Override
    void visitRelItemDeclaration(StructRelItemDeclaration relItemRule) {
        String code = relItemRule.idValue.text
        RelationItem item = new RelationItem(code)

        def relAttrsRule = relItemRule.relAttrsRule

        relAttrsRule.itemAttrSetParamRuleList.forEach {
            String attributeId = it.attributeIdValue.text
            if(attributeId.equalsIgnoreCase(SOURCE_ATTRIBUTE)){
                def sourceId = it.idValue.text
                Objects.requireNonNull(sourceId, "Source Id for [${code}] must not be null")
                item.source = sourceId
            }
            if(attributeId.equalsIgnoreCase(TARGET_ATTRIBUTE)){
                def targetId = it.idValue.text
                Objects.requireNonNull(targetId, "Target Id for [${code}] must not be null")
                item.target = targetId
            }
        }

        def relItemParamRule = relItemRule.relItemParamRule

        def sourceAttrName = relItemParamRule.relParamSourceRule?.attributeIdValueList[1]?.text
        Attribute<ClassItem> sourceAttribute = new Attribute<>(sourceAttrName, item.target)
        sourceAttribute.owner = item
        def relSourceParamAttributes = relItemParamRule.relParamSourceRule?.relParamAttributes
        extractAttributes(sourceAttribute, relSourceParamAttributes)
        item.sourceAttribute = sourceAttribute

        def targetAttrName = relItemParamRule.relParamTargetRule?.attributeIdValueList[1]?.text
        Attribute<ClassItem> targetAttribute = new Attribute<>(targetAttrName, item.source)
        targetAttribute.owner = item
        def relTargetParamAttributes = relItemParamRule.relParamTargetRule?.relParamAttributes
        extractAttributes(targetAttribute, relTargetParamAttributes)
        item.targetAttribute = targetAttribute

        accumulator.addItem(item)
    }

    void extractAttributes(Attribute<ClassItem> attribute, StructRelParamAttributes structRelParamAttributes) {

        List<StructParamSetterAttrRule> relParamAttributes = structRelParamAttributes.paramSetterAttrRuleList
        if (CollectionUtils.isNotEmpty(relParamAttributes)) {
            relParamAttributes.each { StructParamSetterAttrRule rule ->
                def attr = rule.attributeIdValue.text
                if(ParseItemStructVisitor.paramExtractorMap.containsKey(attr)){
                    ParseItemStructVisitor.paramExtractorMap.get(attr).call(rule, attribute)
                }else {
                    throw new IllegalArgumentException("Attribute parammeter [${attribute.getOwner().getTypeName()}::${attribute.name}::${attr}] is not allowed ")

                }
            }
        }
    }

}
