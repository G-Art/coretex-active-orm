package com.coretex.build.builder.impl

import com.coretex.Constants
import com.coretex.build.builder.ItemsBuilder
import com.coretex.build.builder.StructFileParser
import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.context.CoretexPluginContext.ModuleContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.traits.Adjustable
import com.coretex.build.resolvers.Resolver
import com.coretex.build.resolvers.impl.*
import com.coretex.struct.bnf.StructParserDefinition
import com.coretex.validators.StructFileValidator
import org.apache.commons.lang3.StringUtils
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.util.PatternSet

import static com.coretex.Constants.Config.Build.STRUCT_ANALISE
import static com.coretex.Constants.PROJECT
import static org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ItemsBuilderImpl implements ItemsBuilder {

    private static final Logger LOG = Logging.getLogger(ItemsBuilderImpl)

    private static final String PATH_SEPARATOR = '/'

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private List<Resolver> resolvers = [] as Queue

    private boolean structAnalise = true

    ItemsBuilderImpl() {
        if (buildContext.containBuildProperty(STRUCT_ANALISE)) {
            structAnalise = buildContext.getBuildProperty(STRUCT_ANALISE)
        }

        resolvers.add(new ModificationResolver())
        resolvers.add(new RelationResolver())
        resolvers.add(new ChildResolver())
        resolvers.add(new PackageResolver())
        resolvers.add(new ImportResolver())
//        resolvers.add(new BidirectionalResolver())
    }

    @Override
    void parse() {
        LOG.debug('Start method - [generate] - ItemsBuilderImpl class')
        Project project = buildContext.getBuildProperty(PROJECT)

        StructFileValidator validator = new StructFileValidator(new StructParserDefinition())

        Map<String, StructFileValidator.ErrorsAccumulator> errors = new HashMap<>()

        processProject(project.name, project, validator)

        project.childProjects.entrySet().each {
            String moduleName = moduleName(it.key)
            LOG.debug("Process ${moduleName} module ... ")

            Project module = it.value
            processProject(moduleName, module, validator)
        }

        if (!errors.isEmpty()) {
            errors.entrySet().each { entry ->
                LOG.lifecycle("Error in file [${entry.key}]")
                entry.value.errors().each { error ->
                    LOG.lifecycle(error.errorDescription)
                }
            }
            throw new StructFileValidationException("Invalid .struct files: ${errors.keySet()}")
        }

        generateProjectItems()
        LOG.debug('End method - [generate] - ItemsBuilderImpl class')
    }


    private processProject(String moduleName, Project project, StructFileValidator validator) {
        ModuleContext moduleCtx = buildContext.createModuleContext(moduleName.toLowerCase())
        def configuration = project.configurations.getByName(IMPLEMENTATION_CONFIGURATION_NAME)
        Map<String, Set<File>> resStructFiles = [:]
        configuration.files.each {
            def files = project.zipTree(it).matching(new PatternSet().include("/.*\\.struct\$/")).files
            if (!files.isEmpty()) {
                resStructFiles.put(it.name, files)
            }
        }
        project.projectDir.eachFileRecurse {
            if (!it.getAbsolutePath().startsWith(project.projectDir.absolutePath + "/${Constants.PATH_CLASSES}")) {
                StructFileValidator.ErrorsAccumulator accumulator = new StructFileValidator.ErrorsAccumulator()
                if (it.path =~ /.*\.struct$/) {
                    LOG.lifecycle("Indexing [ ${project.name}::${it.name} ]...")
                    if (structAnalise) {
                        validator.validateStructFile(it, accumulator)
                        if (accumulator.hasErrors()) {
                            project.put(it.name, accumulator)
                        }
                    }
                    moduleCtx.addItemsFile(it)
                }
            }
        }

        buildContext.addModuleContext(moduleCtx)
    }

    private String moduleName(String moduleName) {
        if (moduleName.contains(PATH_SEPARATOR)) {
            return moduleName[moduleName.lastIndexOf(PATH_SEPARATOR) + 1..-1]
        }
        moduleName
    }

    private void generateProjectItems() {
        LOG.debug('Start method - [generateItems] - ItemsBuilderImpl class')
        buildContext.moduleContexts.each { ModuleContext moduleContext ->
            LOG.debug "Parse items from [ ${moduleContext.moduleName} ] module"
            Closure<Void> generateItemsAction = prepareActionForItemsGeneration(moduleContext)
            moduleContext.itemsFiles
                    .each generateItemsAction
        }

        resolvers*.resolve(buildContext.items)

        LOG.debug('End method - [generateItems] - ItemsBuilderImpl class')
    }

    private Closure<Void> prepareActionForItemsGeneration(ModuleContext modCtx) {
        return { File structFile ->
            List<AbstractItem> elements = new StructFileParser(structFile, new StructParserDefinition()).run()
//            List<AbstractItem> elements = new StructFile(structFile).run()
            elements.each {
                it.each { item ->
                    item.ownerModuleName = modCtx.moduleName
                    if (item in Adjustable && StringUtils.isNotBlank(item.enhance)) {
                        buildContext.addItemModification(item)
                    } else {
                        buildContext.addItem(item)
                    }
                }
            }
        }
    }

}

