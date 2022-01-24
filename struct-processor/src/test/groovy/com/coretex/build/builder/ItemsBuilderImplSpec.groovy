package com.coretex.build.builder


import com.coretex.build.builder.impl.ItemsBuilderImpl
import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.EnumItem
import com.coretex.build.data.items.attributes.Attribute
import org.apache.velocity.texen.util.FileUtil
import org.gradle.api.Project
import spock.lang.*

import static com.coretex.Constants.Config.Build.STRUCT_ANALISE
import static com.coretex.Constants.PROJECT
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ItemsBuilderImplSpec extends Specification {

    private ItemsBuilderImpl itemBuilder
    public static final String PATH = "src/test/resources/struct/"
    private static final String MODULE_NAME = 'test_module'
    private static final String TEST_PROJECT_NAME = "test/${MODULE_NAME}"

    private Project project = Mock()
    private Project childProject = Mock()
    private File dir_file = Mock()

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    private String TEST_ITEM = 'Test'
    private String TEST_ITEM_DESCRIPTION = 'Test item'
    private String TEST_ITEM_ATTRIBUTE_NAME = 'testAttribute'
    private String TEST_ITEM_ATTRIBUTE_DESCRIPTION = 'Attribute description'

    private String TEST_ITEM_2 = 'Test2'
    private String TEST_ITEM_DESCRIPTION_2 = 'Test item2'
    private String TEST_ITEM_ATTRIBUTE_NAME_2 = 'testAttribute2'
    private String TEST_ITEM_ATTRIBUTE_DESCRIPTION_2 = 'Attribute description 2'

    private String STRUCTURE_WITH_SINGLE_ITEM = """${PATH}singleItem-items.struct"""

    private String STRUCTURE_WITH_TWO_ITEMS = """${PATH}twoItems-items.struct"""

    private String STRUCTURE_WITH_INHERITED_ITEMS = """${PATH}inheritedItems-items.struct"""

    private String STRUCTURE_WITH_ENHANCE_ITEMS = """${PATH}enhanceItems-items.struct"""

    private String STRUCTURE_WITH_ENHANCE_ENUM = """${PATH}enhanceEnum-items.struct"""

    private String STRUCTURE_WITH_RELATED_ITEMS = """${PATH}relatedItems-items.struct"""

    void setup() {
        buildContext.cleanUpContext()

        project = Mock()
        childProject = Mock()
        dir_file = Mock()

        buildContext.addProperty(PROJECT, project)
        buildContext.addProperty(STRUCT_ANALISE, false)

        itemBuilder = new ItemsBuilderImpl()

        dir_file.exists() >> true
        dir_file.isDirectory() >> true
        dir_file.getAbsolutePath() >> {"test/path"}

        project.childProjects >> [(TEST_PROJECT_NAME.toString()): childProject]
        childProject.projectDir >> dir_file
    }

    def 'create item from struct file for module'() {
        given:
        File spec_file  = FileUtil.file(STRUCTURE_WITH_SINGLE_ITEM)
        dir_file.listFiles() >> [spec_file]

        when:
        itemBuilder.parse()

        and:
        List<CoretexPluginContext.ModuleContext> moduleContexts = buildContext.moduleContexts
        CoretexPluginContext.ModuleContext context = moduleContexts.get(0)

        and:
        List<AbstractItem> abstractItemList = buildContext.items
        ClassItem abstractItem = abstractItemList.get(0) as ClassItem

        and:
        Set<Attribute> attributes = abstractItem.attributes
        Attribute attribute = ++attributes.iterator()

        then:
        moduleContexts != null
        moduleContexts.size() == 1
        context.moduleName == MODULE_NAME

        then:
        abstractItemList != null
        abstractItemList.size() == 1

        then:
        abstractItem.code == TEST_ITEM
        abstractItem.description == TEST_ITEM_DESCRIPTION
        abstractItem.ownerModuleName == MODULE_NAME

        then:
        attributes != null
        attributes.size() == 1

        then:
        attribute.name == TEST_ITEM_ATTRIBUTE_NAME
        attribute.description == TEST_ITEM_ATTRIBUTE_DESCRIPTION
        attribute.owner == abstractItem

    }

    def 'create independent items'() {
        given:
        File spec_file  = FileUtil.file(STRUCTURE_WITH_TWO_ITEMS)
        dir_file.listFiles() >> [spec_file]

        when:
        itemBuilder.parse()

        and:
        List<AbstractItem> abstractItemList = buildContext.items

        then:
        abstractItemList != null
        abstractItemList.size() == 2

        then:
        abstractItemList[0].code == TEST_ITEM
        abstractItemList[0].description == TEST_ITEM_DESCRIPTION
        abstractItemList[0].ownerModuleName == MODULE_NAME

        then:
        abstractItemList[1].code == TEST_ITEM_2
        abstractItemList[1].description == TEST_ITEM_DESCRIPTION_2
        abstractItemList[1].ownerModuleName == MODULE_NAME

        then:
        abstractItemList[0].attributes != null
        abstractItemList[0].attributes.size() == 1

        then:
        abstractItemList[1].attributes != null
        abstractItemList[1].attributes.size() == 1

        then:
        abstractItemList[0].attributes[0].name == TEST_ITEM_ATTRIBUTE_NAME
        abstractItemList[0].attributes[0].description == TEST_ITEM_ATTRIBUTE_DESCRIPTION
        abstractItemList[0].attributes[0].owner == abstractItemList[0]

        then:
        abstractItemList[1].attributes[0].name == TEST_ITEM_ATTRIBUTE_NAME_2
        abstractItemList[1].attributes[0].description == TEST_ITEM_ATTRIBUTE_DESCRIPTION_2
        abstractItemList[1].attributes[0].owner == abstractItemList[1]

    }

    def 'create inherited items'() {
        given:
        File spec_file  = FileUtil.file(STRUCTURE_WITH_INHERITED_ITEMS)
        dir_file.listFiles() >> [spec_file]

        when:
        itemBuilder.parse()

        and:
        List<AbstractItem> abstractItemList = buildContext.items

        then:
        abstractItemList[1].parentItem == abstractItemList[0]

    }

    def 'create enhanced items'() {
        given:
        File spec_file  = FileUtil.file(STRUCTURE_WITH_ENHANCE_ITEMS)
        dir_file.listFiles() >> [spec_file]

        when:
        itemBuilder.parse()

        and:
        List<AbstractItem> abstractItemList = buildContext.items

        then:
        abstractItemList.size() == 1
        ((ClassItem)abstractItemList[0]).attributes.size() == 2

    }

    def 'create enhanced enums'() {
        given:
        File spec_file  = FileUtil.file(STRUCTURE_WITH_ENHANCE_ENUM)
        dir_file.listFiles() >> [spec_file]

        when:
        itemBuilder.parse()

        and:
        List<AbstractItem> abstractItemList = buildContext.items

        then:
        abstractItemList.size() == 1
        ((EnumItem)abstractItemList[0]).values.size() == 2

    }

    def 'create related items'() {
        given:
        File spec_file  = FileUtil.file(STRUCTURE_WITH_RELATED_ITEMS)
        dir_file.listFiles() >> [spec_file]

        when:
        itemBuilder.parse()

        and:
        List<AbstractItem> abstractItemList = buildContext.items

        then:
        abstractItemList[1].attributes[0].type == abstractItemList[0]

    }
}
