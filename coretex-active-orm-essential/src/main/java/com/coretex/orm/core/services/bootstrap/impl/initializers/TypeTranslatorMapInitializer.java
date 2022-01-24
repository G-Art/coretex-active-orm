package com.coretex.orm.core.services.bootstrap.impl.initializers;

import com.coretex.orm.core.activeorm.translator.TypeTranslator;
import com.coretex.orm.core.activeorm.translator.standard.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TypeTranslatorMapInitializer implements CoretexContextInitializer<Map<String, TypeTranslator<?>>>{

	private final Logger LOG = LoggerFactory.getLogger(TypeTranslatorMapInitializer.class);

	private Map<String, TypeTranslator<?>> typeTranslatorMap;

	@Override
	public Map<String, TypeTranslator<?>> initialize() {
		typeTranslatorMap = new HashMap<>(50);

		// standard primitives (used by RowTranslator#initColumnTranslators)
		putTypeTranslator("boolean", new BooleanTranslator());
		putTypeTranslator("byte", new ByteTranslator());
		putTypeTranslator("double", new DoubleTranslator());
		putTypeTranslator("float", new FloatTranslator());
		putTypeTranslator("int", new IntegerTranslator());
		putTypeTranslator("long", new LongTranslator());
		putTypeTranslator("short", new ShortTranslator());
		putTypeTranslator("char", new CharacterTranslator());

		// standard types
		putTypeTranslator(BigDecimal.class, new BigDecimalTranslator());
		putTypeTranslator(Class.class, new ClassTranslator());
		putTypeTranslator(Character.class, new CharacterTranslator());
		putTypeTranslator(Boolean.class, new BooleanTranslator());
		putTypeTranslator(Byte.class, new ByteTranslator());
		putTypeTranslator(Double.class, new DoubleTranslator());
		putTypeTranslator(Float.class, new FloatTranslator());
		putTypeTranslator(Integer.class, new IntegerTranslator());
		putTypeTranslator(Long.class, new LongTranslator());
		putTypeTranslator(Short.class, new ShortTranslator());
		putTypeTranslator(Object.class, new ObjectTranslator());
		putTypeTranslator(String.class, new StringTranslator());
		putTypeTranslator(java.util.Date.class, new DateTranslator());
		putTypeTranslator(java.sql.Date.class, new SqlDateTranslator());
		putTypeTranslator(java.sql.Time.class, new SqlTimeTranslator());
		putTypeTranslator(java.sql.Timestamp.class, new SqlTimestampTranslator());
		putTypeTranslator(GregorianCalendar.class, new GregorianCalendarTranslator());
		putTypeTranslator(LocalDate.class, new LocalDateTranslator());
		putTypeTranslator(LocalTime.class, new LocalTimeTranslator());
		putTypeTranslator(LocalDateTime.class, new LocalDateTimeTranslator());
		putTypeTranslator(Instant.class, new InstantTranslator());
		putTypeTranslator(UUID.class, new UUIDTranslator());
		return typeTranslatorMap;
	}

	private void putTypeTranslator(Class<?> typeClass, TypeTranslator<?> typeTranslator) {
		putTypeTranslator(typeClass.getCanonicalName(), typeTranslator);
	}

	private void putTypeTranslator(String typeClassName, TypeTranslator<?> typeTranslator) {
		if (LOG.isDebugEnabled()) LOG.debug("adding " + typeTranslator + " for " + typeClassName);
		typeTranslatorMap.put(typeClassName, typeTranslator);
	}
}
