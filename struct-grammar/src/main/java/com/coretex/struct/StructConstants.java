package com.coretex.struct;

import com.coretex.struct.gen.parser.psi.StructItemAttrSetParamRule;

import java.util.Objects;

public final class StructConstants {

	public static final class Common {
		public static final String ELEMENTS_KEYWORD = "elements";
		public static final String ITEMS_KEYWORD = "items";
		public static final String RELATIONS_KEYWORD = "relations";
		public static final String ENUMS_KEYWORD = "enums";

		public static final String ATTRIBUTES_KEYWORD = "attributes";

		public static final String ASSOCIATED_PARAMETER = "associated";
		public static final String CONTAINER_TYPE_PARAMETER = "containerType";
		public static final String DESCRIPTION_PARAMETER = "description";
		public static final String OPTIONAL_PARAMETER = "optional";
		public static final String LOCALIZED_PARAMETER = "localized";
		public static final String UNIQUE_PARAMETER = "unique";
		public static final String INDEX_PARAMETER = "index";
		public static final String DEFAULT_VALUE_PARAMETER = "defaultValue";

		public static final String TABLE_ATTRIBUTE = "table";
		public static final String EXTEND_ATTRIBUTE = "extend";
		public static final String ENHANCE_ATTRIBUTE = "enhance";
		public static final String ABSTRACT_ATTRIBUTE = "abstract";

	}

	public static final class Relation {
		public static final String SOURCE = "source";
		public static final String TARGET = "target";

		public static final String SOURCE_PARAMETER = SOURCE;
		public static final String TARGET_PARAMETER = TARGET;

		public static final String SOURCE_ATTRIBUTE = SOURCE;
		public static final String TARGET_ATTRIBUTE = TARGET;
	}

	public static final class Item {
		public static final String CLASS_SUFFIX = "Item";
	}

	public static final class Enum {
		public static final String CLASS_SUFFIX = "Enum";
	}

}
