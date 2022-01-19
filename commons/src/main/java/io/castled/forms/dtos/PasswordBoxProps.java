package io.castled.forms.dtos;

import io.castled.forms.FormFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordBoxProps extends DisplayFieldProps {

    public PasswordBoxProps(String placeholder, String title, String description, String optionsRef) {
        super(FormFieldType.PASSWORD_BOX, title, description);
        this.placeholder = placeholder;
        this.optionsRef = optionsRef;
    }

    private String placeholder;
    private String optionsRef;
}
