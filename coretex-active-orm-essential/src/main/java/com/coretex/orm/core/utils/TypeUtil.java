package com.coretex.orm.core.utils;

import com.coretex.items.core.RegularTypeItem;
import com.coretex.orm.core.CoretexConfigurationProvider;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.findField;

public final class TypeUtil {

	private static Logger LOG = LoggerFactory.getLogger(TypeUtil.class);

	private TypeUtil() {
	}

	private static SimpleTypeConverter typeConverter = new SimpleTypeConverter();

	static {
		typeConverter.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalTime.parse(text));
			}
		});

		typeConverter.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Parse LocalDateTime [{}]", text);
				}
				setValue(Timestamp.valueOf(text).toLocalDateTime());
			}
		});

		typeConverter.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Parse LocalDate [{}]", text);
				}
				setValue(LocalDate.parse(text));
			}
		});

		typeConverter.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				DbDialectService dialectService =
						CoretexConfigurationProvider.getDbDialectService();

				if (LOG.isDebugEnabled()) {
					LOG.debug("Parse Date [{}]", text);
				}
				setValue(dialectService.stringToDate(text));
			}
		});
	}

	public static <T> T toType(Object obj, Class<T> targetType) {
		return typeConverter.convertIfNecessary(obj, targetType);
	}

	@SuppressWarnings("unchecked")
	public static <T> T toType(Object obj, RegularTypeItem regularTypeItem) {
		return (T) toType(obj, regularTypeItem.getRegularClass());
	}

	public static String getMetaTypeCode(Class<? extends AbstractGenericItem> itemClass) {
		checkArgument(nonNull(itemClass), "Item class is required for building item context");
		Field field = findField(itemClass, "ITEM_TYPE");
		return (String) ReflectionUtils.getField(field, null);
	}
}
