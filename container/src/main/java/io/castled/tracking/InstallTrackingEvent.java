package io.castled.tracking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstallTrackingEvent {

    private TrackingEventType eventType;
    private String installId;
    private Map<String, String> tags;
}
