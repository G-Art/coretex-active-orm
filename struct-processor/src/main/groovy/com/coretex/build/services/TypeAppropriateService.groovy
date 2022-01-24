package com.coretex.build.services

import com.coretex.build.data.items.RegularClassItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.exceptions.TypeMismatchException
import com.google.common.collect.ImmutableMap

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 *
 * Supported java type classes:
 *
 * String, Boolean, Float, Double,
 * Character, Byte, Long, Short, Date, Number,
 * LocalDateTime, LocalDate, LocalTime, UUID, Class
 */
abstract class TypeAppropriateService {

    private static final Set<Class> regularClasses = [String,
                                                      Boolean,
                                                      Float,
                                                      Double,
                                                      Integer,
                                                      Character,
                                                      Long,
                                                      Date,
                                                      Byte,
                                                      Short,
                                                      LocalDateTime,
                                                      LocalDate,
                                                      LocalTime,
                                                      UUID,
                                                      Class,
                                                      BigDecimal]

    TypeAppropriateService() {
        init()
        Set<Class> connectedClasses = connectedTypes().keySet()
        if (!connectedClasses.containsAll(regularClasses)) {
            Class connectedClass = connectedClasses.find { !regularClasses.contains(it) }
            throw new NoClassDefFoundError("${connectedClass.name} in not connected")
        }
    }

    abstract ImmutableMap<Class, String> connectedTypes()

    abstract ImmutableMap<String, String> persistenceMapping()

    abstract String getNullValue()

    protected abstract void init()

    String getBasicDataType(Attribute<RegularClassItem> attribute) {
        try {
            return getBasicDataType(attribute.type)
        } catch (TypeMismatchException ex) {
            throw new TypeMismatchException('No one appropriate postgresql type available for ' +
                    "[ Attribute: ${attribute.name} Type: ${attribute.type.code} ]", ex)
        }
    }

    String getBasicDataType(RegularClassItem regularItemType) {
        if (connectedTypes().containsKey(regularItemType.regularClass)) {
            return connectedTypes().get(regularItemType.regularClass)
        }
        throw new TypeMismatchException("DataBase Provider service doesn't support $regularItemType.code")
    }

    String getAttributeBasicMappingType(Attribute<RegularClassItem> attribute) {
        try {
            return getBasicMappingType(attribute.type)
        } catch (TypeMismatchException ex) {
            throw new TypeMismatchException('No one appropriate persistence type available for ' +
                    "[ Attribute: ${attribute.name} Type: ${attribute.type.code} ]", ex)
        }

    }

    String getBasicMappingType(RegularClassItem regularItemType) {
        if (connectedTypes().containsKey(regularItemType.regularClass)) {
            return persistenceMapping().get(regularItemType.regularClass)
        }
        throw new TypeMismatchException("DataBase Provider service doesn't support $regularItemType.code")
    }

    String getRelationDataType() {
        return connectedTypes().get(UUID)
    }

    Set<Class> getRegularClasses() {
        return regularClasses
    }
}