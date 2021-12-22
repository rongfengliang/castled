package io.castled.apps.optionfetchers;

import io.castled.apps.AppConfig;
import io.castled.forms.dtos.FormFieldOption;

import java.util.List;

public interface AppOptionsFetcher {

    List<FormFieldOption> getFieldOptions(AppConfig appConfig);
}
