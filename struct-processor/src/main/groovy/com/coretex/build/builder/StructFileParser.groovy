package com.coretex.build.builder


import com.coretex.build.parsers.ItemAccumulator
import com.coretex.build.parsers.ParseItemsStructVisitor
import com.coretex.struct.bnf.StructParserDefinition
import com.intellij.psi.PsiFile
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.intellij.grammar.LightPsi
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class StructFileParser  {
    private static final Logger LOG = Logging.getLogger(StructFileParser)

    private StructParserDefinition definition

    private ItemAccumulator accumulator

    private File structFile

    StructFileParser(File structFile, StructParserDefinition definition) {
        this.structFile = structFile
        this.definition = definition
        this.accumulator = new ItemAccumulator()
    }

    List run() {

        PsiFile res = null;
        try {
            LOG.debug("Parsing file :: [${structFile.canonicalPath}]")
            res = LightPsi.parseFile(structFile, definition);
        } catch (IOException e) {
            LOG.error("File [${structFile.canonicalPath}] parsing error", e)
        }
        res.accept(new ParseItemsStructVisitor(accumulator: accumulator))

        return accumulator.items
    }

}
