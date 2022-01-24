package com.coretex.build.parsers

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.struct.StructConstants
import com.coretex.struct.gen.parser.psi.*
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.BooleanUtils

import static com.coretex.build.parsers.ParserUtils.unquoteString

class ParseItemStructVisitor extends StructVisitor implements AccumulatedItemStructVisitor {

    private Map<String, Closure> attributeExtractorMap = [
            (StructConstants.Common.TABLE_ATTRIBUTE)   : { StructItemAttrSetParamRule rule, ClassItem classItem ->
                if (Objects.nonNull(rule.boolValues) && Objects.nonNull(rule.boolValues.text)) {
                    classItem.setTable(BooleanUtils.toBooleanObject(rule.boolValues.text))
                }
            },
            (StructConstants.Common.EXTEND_ATTRIBUTE)  : { StructItemAttrSetParamRule rule, ClassItem classItem ->
                def extend = rule.idValue?.text
                if (Objects.isNull(extend)) {
                    extend = rule.stringLiteralValue?.text
                }
                classItem.setExtend(extend)
            },
            (StructConstants.Common.ENHANCE_ATTRIBUTE) : { StructItemAttrSetParamRule rule, ClassItem classItem ->
                classItem.setEnhance(unquoteString(rule.stringLiteralValue?.text))
            },
            (StructConstants.Common.ABSTRACT_ATTRIBUTE): { StructItemAttrSetParamRule rule, ClassItem classItem ->
                if (Objects.nonNull(rule.boolValues) && Objects.nonNull(rule.boolValues.text)) {
                    classItem.setAbstract(BooleanUtils.toBooleanObject(rule.boolValues.text))
                }
            }
    ]

    public static Map<String, Closure> paramExtractorMap = [
            (StructConstants.Common.ASSOCIATED_PARAMETER)    : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.associated = BooleanUtils.toBoolean(v.boolValues?.text)
            },
            (StructConstants.Common.CONTAINER_TYPE_PARAMETER): { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.containerType = v.idValue?.text == "Set" ? Set.class as Class<Object> : List.class as Class<Object>
            },
            (StructConstants.Common.DESCRIPTION_PARAMETER)   : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.description = unquoteString(v.stringLiteralValue?.text)
            },
            (StructConstants.Common.OPTIONAL_PARAMETER)      : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.optional = BooleanUtils.toBoolean(v.boolValues?.text)
            },
            (StructConstants.Common.LOCALIZED_PARAMETER)     : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.localized = BooleanUtils.toBoolean(v.boolValues?.text)
            },
            (StructConstants.Common.UNIQUE_PARAMETER)        : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.unique = BooleanUtils.toBoolean(v.boolValues?.text)
            },
            (StructConstants.Common.DEFAULT_VALUE_PARAMETER)        : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                if(Objects.nonNull(v.boolValues)){
                    a.defaultValue = v.boolValues.text
                }
                if(Objects.nonNull(v.stringLiteralValue)){
                    a.defaultValue = v.stringLiteralValue.text
                }
                if(Objects.nonNull(v.idValue)){
                    a.defaultValue = v.idValue.text
                }
            },
            (StructConstants.Common.INDEX_PARAMETER)         : { StructParamSetterAttrRule v, Attribute<ClassItem> a ->
                a.index = v.indexArray.stringLiteralValueList.collect { unquoteString(it.text) }
            }
    ]

    @Override
    void visitItemItemDeclaration(StructItemItemDeclaration itemItemRule) {
        String code = itemItemRule.idValue.text
        ClassItem item = new ClassItem(code)
        StructItemAttrSetParamsRule itemAttrSetParamsRule = itemItemRule.itemAttrtsRule?.itemAttrSetParamsRule

        if (Objects.nonNull(itemAttrSetParamsRule)) {
            extractParamAttributes(itemAttrSetParamsRule, item)
        }

        def itemItemParamRule = itemItemRule.itemItemParamRule
        if (Objects.nonNull(itemItemParamRule)) {
            def descriptionSetterAttrRule = itemItemParamRule.paramSetterAttrRule
            if (Objects.nonNull(descriptionSetterAttrRule)) {
                item.setDescription(unquoteString(descriptionSetterAttrRule.stringLiteralValue?.text))
            }
            extractAttributes(itemItemParamRule.itemAttrsRule?.itemAttrDclrRuleList, item)
        }

        accumulator.addItem(item)
    }


    void extractParamAttributes(StructItemAttrSetParamsRule structItemAttrSetParamsRule, ClassItem classItem) {
        if (Objects.nonNull(structItemAttrSetParamsRule)) {

            StructItemAttrSetParamRule rule = structItemAttrSetParamsRule.itemAttrSetParamRule

            if (attributeExtractorMap.containsKey(rule.attributeIdValue.text)) {
                attributeExtractorMap.get(rule.attributeIdValue.text).call(rule, classItem)
            }else {
                throw new IllegalArgumentException( "Attribute ["+rule.attributeIdValue.text+"] is not allowed ")
            }
            structItemAttrSetParamsRule.itemAttrSetParamsRuleList.forEach {
                extractParamAttributes(it, classItem)
            }
        }
    }


    void extractAttributes(List<StructItemAttrDclrRule> structItemAttrDclrRules, ClassItem classItem) {
        if (CollectionUtils.isNotEmpty(structItemAttrDclrRules)) {
            structItemAttrDclrRules.each {
                extractAttribute(it, classItem)
            }
        }
    }

    void extractAttribute(StructItemAttrDclrRule structItemAttrDclrRule, ClassItem classItem) {
        def attrName = structItemAttrDclrRule.attributeIdValue?.text
        def attrTypeCode = structItemAttrDclrRule.idValue?.text

        Attribute attribute = new Attribute(attrName, attrTypeCode)
        List<StructParamSetterAttrRule> paramSetterAttrRules = structItemAttrDclrRule.itemParamAttributes?.paramSetterAttrRuleList
        if (CollectionUtils.isNotEmpty(paramSetterAttrRules)) {
            paramSetterAttrRules.each { StructParamSetterAttrRule rule ->

                def attr = rule.attributeIdValue.text
                if (paramExtractorMap.containsKey(attr)) {
                    paramExtractorMap.get(attr).call(rule, attribute)
                }else {
                    throw new IllegalArgumentException("Attribute parammeter [${classItem.getTypeName()}::${attribute.name}::${attr}] is not allowed ")
                }
            }
        }

        attribute.owner = classItem
        classItem.addAttribute(attribute)
    }

}
