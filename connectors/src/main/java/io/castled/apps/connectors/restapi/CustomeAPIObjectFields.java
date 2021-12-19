package io.castled.apps.connectors.restapi;

import io.castled.schema.SchemaConstants;
import io.castled.schema.models.Schema;
import lombok.Getter;

public class CustomeAPIObjectFields {

    public enum GENERIC_OBJECT_FIELD {
        IDENITIFIER("identifier","Identifier",SchemaConstants.STRING_SCHEMA);

        GENERIC_OBJECT_FIELD(String fieldName, String fieldTitle , Schema schema) {
            this.fieldName = fieldName;
            this.fieldTitle = fieldTitle;
            this.schema = schema;
        }

        @Getter
        private final String fieldName;

        @Getter
        private final String fieldTitle;

        @Getter
        private final Schema schema;
    }
}
