package io.castled.apps.connectors.googlesheets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetRow {
    private long rowNo;
    private LinkedHashMap<String, Object> values;
}
