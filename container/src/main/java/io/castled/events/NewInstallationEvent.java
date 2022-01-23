package io.castled.events;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NewInstallationEvent extends CastledEvent {

    private String installationId;

    public NewInstallationEvent(String installationId) {
        super(CastledEventType.NEW_INSTALLATION);
        this.installationId = installationId;
    }

    @Override
    public String entityId() {
        return installationId;
    }
}
