package io.castled.forms.dtos;

import io.castled.forms.FormFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HiddenProps extends FormFieldProps {

    private final String optionsRef;
    private final String loadingText;

    public HiddenProps(String optionsRef, String loadingText) {
        super(FormFieldType.HIDDEN);
        this.optionsRef = optionsRef;
        this.loadingText = loadingText;
    }

}
