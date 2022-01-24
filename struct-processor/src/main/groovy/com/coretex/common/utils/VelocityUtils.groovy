package com.coretex.common.utils

import org.apache.commons.lang3.StringUtils
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.apache.velocity.tools.ToolManager

import static java.io.File.separator

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
final class VelocityUtils {

    private static VelocityEngine getEngine() {
        VelocityEngine ve = new VelocityEngine()
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, 'classpath')
        ve.setProperty('classpath.resource.loader.class', ClasspathResourceLoader.name)

        ve.init()
        return ve
    }

    private static Template getTemplate(String templateName) {
        return engine.getTemplate("class${separator}templates${separator}${templateName}.vm")
    }

    private static Template getTemplate(String subPath, String templateName) {
        return engine.getTemplate("class${separator}templates${subPath}${separator}${templateName}.vm")
    }

    private static VelocityContext createContext(Map map) {
        ToolManager toolManager = new ToolManager()
        toolManager.autoConfigure(true)
        VelocityContext context = new VelocityContext(toolManager.createContext())
        context.put('vmUtils', new VmUtils())
        context.put('StringUtils', StringUtils)

        map.entrySet().each {
            context.put(it.key, it.value)
        }
        return context
    }

    static String executeMerge(String template, Map map) {
        StringWriter writer = new StringWriter()
        getTemplate(template).merge(createContext(map), writer)
        return writer.toString()
    }

    static String executeMerge(String subPath, String template, Map map) {
        StringWriter writer = new StringWriter()
        getTemplate(subPath, template).merge(createContext(map), writer)
        return writer.toString()
    }

    private VelocityUtils() {
    }
}
