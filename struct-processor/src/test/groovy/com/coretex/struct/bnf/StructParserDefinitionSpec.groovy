package com.coretex.struct.bnf

import com.coretex.validators.StructFileValidator
import org.apache.velocity.texen.util.FileUtil
import spock.lang.*

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 26-07-2017
 */
class StructParserDefinitionSpec extends Specification {

    public static final String PATH = "src/test/resources/struct/"
    private StructParserDefinition structParserDefinition

    private StructFileValidator.ErrorsAccumulator errorsAccumulator

    void setup() {
        structParserDefinition = new StructParserDefinition()
        errorsAccumulator = new StructFileValidator.ErrorsAccumulator()
    }

    def 'shouldn\'t be errors in struct file'() {
        when:
        new StructFileValidator(structParserDefinition)
                .validateStructFile(FileUtil.file("${PATH}validFull.struct"), errorsAccumulator)
        then:
        !errorsAccumulator.hasErrors()
    }

    def 'shouldn\'t be errors in struct file if only relations present'() {
        when:
        new StructFileValidator(structParserDefinition)
                .validateStructFile(FileUtil.file("${PATH}validRelations.struct"), errorsAccumulator)

        then:
        !errorsAccumulator.hasErrors()
    }

    def 'shouldn\'t be errors in struct file if only items present'() {
        when:
        new StructFileValidator(structParserDefinition)
                .validateStructFile(FileUtil.file("${PATH}validItems.struct"), errorsAccumulator)

        then:
        !errorsAccumulator.hasErrors()
    }

    def 'shouldn\'t be errors in struct file if only enums present'() {
        when:
        new StructFileValidator(structParserDefinition)
                .validateStructFile(FileUtil.file("${PATH}validEnums.struct"), errorsAccumulator)

        then:
        !errorsAccumulator.hasErrors()
    }

    def 'should have valid errors in struct file if only enums present'() {
        when:
        new StructFileValidator(structParserDefinition)
                .validateStructFile(FileUtil.file("${PATH}enumsWithErrors.struct"), errorsAccumulator)

        then:
        errorsAccumulator.hasErrors()
        and:
        errorsAccumulator.errors().size() == 1
        and:
        errorsAccumulator.errors().get(0).getErrorDescription() == "<(> or <{> expected, got 'test1'"
    }

    def 'should have valid errors in struct file if only relations present'() {
        when:
        new StructFileValidator(structParserDefinition)
                .validateStructFile(FileUtil.file("${PATH}relationsWithErrors.struct"), errorsAccumulator)

        then:
        errorsAccumulator.hasErrors()
        and:
        errorsAccumulator.errors().size() == 2
        and:
        errorsAccumulator.errors().get(0).getErrorDescription() == "<,> expected, got 'target'"

    }

}
