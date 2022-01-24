package com.coretex.build.services.impl

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.converters.impl.AttributeToTableFieldConverter
import com.coretex.build.converters.impl.ClassItemToTableConverter
import com.coretex.build.converters.impl.RelationItemToTableConverter
import com.coretex.build.data.db.Table
import com.coretex.build.data.db.builder.EssentialInsertDataBuilder
import com.coretex.build.data.db.inserts.Insert
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.services.FileGeneratorService
import com.coretex.common.annotation.EssentialDataItem.EssentialItem

import static com.coretex.Constants.*
import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.*
import static com.coretex.common.utils.VelocityUtils.executeMerge

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class SqlSchemaFileGeneratorService implements FileGeneratorService {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private ClassItemToTableConverter classItemToTableConverter
    private RelationItemToTableConverter relationItemToTableConverter

    SqlSchemaFileGeneratorService() {
        this.classItemToTableConverter = new ClassItemToTableConverter(buildContext.dbDialect)
        this.classItemToTableConverter.setAttributeConverter(new AttributeToTableFieldConverter(buildContext.dbDialect))

        this.relationItemToTableConverter = new RelationItemToTableConverter(buildContext.dbDialect)
        this.relationItemToTableConverter.setAttributeConverter(new AttributeToTableFieldConverter(buildContext.dbDialect))
    }

    @Override
    void generate() {
        String tmpDirPath = "${buildContext.getBuildProperty(PROJECT).projectDir.absolutePath}/tmp/sql"
        File tmpDir = createDir(tmpDirPath)

        createSqlSchema(new File(tmpDir.absolutePath, SQL_SCHEMA_FILE_NAME))
    }

    void createSqlSchema(File file) {
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()

        file.write(executeMerge("/sql/${buildContext.dbDialect}", SQL_SCHEMA,
                [queryFactory               : buildContext.dbDialect.queryFactory,
                 tables                     : prepareTables(),
                 metaTypeItems              : buildEssentialDataFor(META_TYPE),
                 metaAttributeTypeItems     : buildEssentialDataFor(META_ATTRIBUTE_TYPE),
                 regularTypeItems           : buildEssentialDataFor(REGULAR_TYPE),
                 enumTypeItems              : buildEssentialDataFor(META_ENUM_TYPE),
                 enumValueTypeItems         : buildEssentialDataFor(META_ENUM_VALUE_TYPE),
                 enumValueOwnerRelationItems: buildEssentialDataFor(ENUM_VALUE_OWNER_RELATION),
                 inheritanceRelationItems   : buildEssentialDataFor(INHERITANCE_RELATION),
                 attributeOwnerRelationItems: buildEssentialDataFor(ATTRIBUTE_OWNER_RELATION)]))
    }


    private List<Table> prepareTables() {
        List<Table> tables = []
        tables.addAll(buildContext.items
                .findAll { item -> item.class in ClassItem && ((ClassItem) item).table }
                .collect { item -> classItemToTableConverter.convert(item as ClassItem) })

        tables.addAll(buildContext.items
                .findAll { item -> item.class in RelationItem }
                .collect { item -> relationItemToTableConverter.convert(item as RelationItem) })

        return tables
    }

    private static List<Insert> buildEssentialDataFor(EssentialItem essentialItem) {
        return new EssentialInsertDataBuilder().itemType(essentialItem).build()
    }

    private static File createDir(String tmpDirPath) {
        File dir = new File(tmpDirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
}
