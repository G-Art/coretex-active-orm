package com.coretex.build.context


import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.common.DbDialect
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@Singleton(strict = false)
class CoretexPluginContext implements ItemsContext {
    private Logger LOG = Logging.getLogger(CoretexPluginContext)

    private Map<Object, Object> parameters

    private Map<String, File> profileConfig

    private List<ModuleContext> moduleContexts

    private Attribute<RegularClassItem> uuidAttribute

    private DbDialect dbDialect

    private Project coreModule

    private CoretexPluginContext() {
        cleanUpContext()
    }

    void addProfile(Map<String, File> properties) {
        properties.entrySet().each {
            addProfile(it.key, it.value)
        }
    }

    void addProfile(String key, File value) {
        profileConfig.put(key, value)
    }

    Map<String, File> getProfileConfig() {
        return profileConfig
    }

    DbDialect getDbDialect() {
        return dbDialect
    }

    void setDbDialect(DbDialect dbDialect) {
        this.dbDialect = dbDialect
    }

    Project getCoreModule() {
        return coreModule
    }

    void setCoreModule(Project coreModule) {
        this.coreModule = coreModule
    }

    void addProperty(Map properties) {
        properties.entrySet().each {
            addProperty(it.key, it.value)
        }
    }

    void addProperty(Object key, Object value) {
        LOG.debug("Add new properties: Key - [ ${key} ]; Value - [ ${value} ];")
        parameters.put(key, value)
    }

    Object getBuildProperty(Object key) {
        parameters.get(key)
    }

    Map<Object, Object> getAllBuildProperties() {
        return Map.copyOf(parameters)
    }

    Boolean containBuildProperty(Object key) {
        parameters.containsKey(key)
    }

    List<ModuleContext> getModuleContexts() {
        moduleContexts
    }

    Attribute<RegularClassItem> getUuidAttribute() {
        return uuidAttribute
    }

    void addModuleContext(ModuleContext moduleItemsXMLs) {
        moduleContexts.add(moduleItemsXMLs)
    }

    ModuleContext createModuleContext(String moduleName) {
        new ModuleContext(moduleName)
    }

    private void initDialect() {
        dbDialect.typeAppropriateService.regularClasses.each {
            addRegularItem(it)
        }
    }

    void cleanUpContext() {
        parameters = [:]
        profileConfig = [:]
        moduleContexts = [] as Queue
        cleanUpItemContext()
        dbDialect = DbDialect.POSTGRESQL
        initDialect()
        String uuidTypeCode = dbDialect.typeAppropriateService.connectedTypes().get(UUID)
        uuidAttribute = new Attribute<>('uuid', uuidTypeCode, regularItems.get(uuidTypeCode))
        coreModule = null
    }

    class ModuleContext {

        private String moduleName

        private Set<File> itemsFiles = [] as Set

        private ModuleContext(String moduleName) {
            this.moduleName = moduleName
        }

        String getModuleName() {
            moduleName
        }

        Set<File> getItemsFiles() {
            itemsFiles
        }

        void addItemsFile(File file) {
            if (file?.exists()) {
                itemsFiles.add(file)
            } else {
                if (file != null) {
                    LOG.debug("File or path [ ${file.absolutePath} ] is not exist")
                }
            }
        }
    }

}