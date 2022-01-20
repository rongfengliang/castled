package io.castled.warehouses.connectors.redshift;

import io.castled.schema.models.TimestampSchema;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRedshiftCsvSchemaMapper {

    @Test
    public void transformValue() throws Exception{

        RedshiftCsvSchemaMapper redshiftCsvSchemaMapper = new RedshiftCsvSchemaMapper();
        redshiftCsvSchemaMapper.transformValue("2021-05-22 03:16:40.105", TimestampSchema.builder().build());
        redshiftCsvSchemaMapper.transformValue("2021-05-22 03:16:40", TimestampSchema.builder().build());
    }
}