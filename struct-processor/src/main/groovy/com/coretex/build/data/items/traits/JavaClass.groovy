package com.coretex.build.data.items.traits

import com.coretex.build.data.items.ClassItem
import org.apache.commons.lang.StringUtils

import static com.coretex.build.parsers.ParserUtils.unquoteString

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
trait JavaClass {

    private Set<String> imports = [] as Set

    private String extend = 'Generic'
    private ClassItem parentItem

    private boolean isAbstract = false

    String getExtend() {
        return extend
    }


    void setAbstract(boolean isAbstract){
        this.isAbstract = isAbstract
    }

    boolean getAbstract(){
        return this.isAbstract
    }

    void setExtend(String extend) {
        if (extend != null && extend == StringUtils.EMPTY) {
            this.extend = null
        }
        if (StringUtils.isNotBlank(extend)) {
            this.extend = unquoteString(extend)
        }
    }

    Set<String> getImports() {
        return imports
    }

    void addImport(String importDefinition) {
        imports.add(importDefinition)
    }

    ClassItem getParentItem() {
        return parentItem
    }

    void setParentItem(ClassItem extendFromItem) {
        this.parentItem = extendFromItem
    }

}