package io.castled.apps.connectors.googlesheets;

import io.castled.apps.models.SyncObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleSheetsSyncObject extends SyncObject {

    private Integer sheetsId;

    public GoogleSheetsSyncObject(Integer sheetsId, String sheetsTitle) {
        super(sheetsTitle);
        this.sheetsId = sheetsId;
    }
}
