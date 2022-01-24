package com.coretex.build.services.impl

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.services.FileGeneratorService
import com.google.common.collect.ImmutableMap
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static com.coretex.Constants.PATH_SEPARATOR
import static com.coretex.Constants.PROPERTY_GEN_JAVA_SRC
import static com.coretex.Constants.PROPERTY_MODULE_FOR_GEN_MODEL
import static com.coretex.Constants.TYPE_ENUM
import static com.coretex.Constants.TYPE_ITEM
import static com.coretex.Constants.TYPE_RELATION
import static com.coretex.common.utils.VelocityUtils.executeMerge

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class JavaSourceFileGeneratorService implements FileGeneratorService {

    private Logger LOG = Logging.getLogger(JavaSourceFileGeneratorService)

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private Map<Class, String> classTemplate

    JavaSourceFileGeneratorService() {
        classTemplate = ImmutableMap.builder()
                .put(EnumItem, TYPE_ENUM)
                .put(ClassItem, TYPE_ITEM)
                .put(RelationItem, TYPE_RELATION).build()
    }

    @Override
    void generate() {
        buildContext.items.each this.&generateFile
    }

    private void generateFile(AbstractItem item) {

        String module = buildContext.getBuildProperty(PROPERTY_MODULE_FOR_GEN_MODEL)
        String genSrcDir = genDirectory(item)

        LOG.debug "Module for generating code: $module; Source generating dir: $genSrcDir"
        createSourceFileForItem(genSrcDir, item)
    }

    private String genDirectory(AbstractItem item) {
        return "${buildContext.getBuildProperty(PROPERTY_GEN_JAVA_SRC)}" +
                "${PATH_SEPARATOR}${item.itemTypeFolder}" +
                "${PATH_SEPARATOR}${item.ownerModuleName.toLowerCase()}"
    }

    void createSourceFileForItem(String dir, AbstractItem item) {
        createDirectory(dir)
        def srcFile = new File(dir, "${item.typeName}.java")
        if (!srcFile.exists()) {

            LOG.debug "File ${srcFile.canonicalPath} is not exist ... generate ..."
            srcFile.createNewFile()

            LOG.debug 'File has been generated ... defining souces ...'
            populateSourceFile(srcFile, item)

            LOG.debug 'File has been filled'
        }
    }

    void populateSourceFile(File srcFile, AbstractItem item) {
        if (classTemplate.containsKey(item.class)) {
            srcFile.write(executeMerge(classTemplate[item.class], ['item': item]))
        }
    }

    private void createDirectory(String dir) {
        LOG.debug "Check directory existing... [ $dir ]"

        File itemFile = new File(dir)
        if (itemFile.exists()) {
            LOG.debug 'Directory exist'
        } else {
            LOG.debug 'Directory is not exist ... creating'
            itemFile.mkdirs()
            LOG.debug 'Directory has been created'
        }
    }

}
