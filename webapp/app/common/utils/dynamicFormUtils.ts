import { StringAnyMap } from "./types";
import { FormFieldsDto } from "./../dtos/FormFieldsDto";
import _ from "lodash";
import * as yup from "yup";

export default {
  getValidation: (formFields?: FormFieldsDto) => {
    if (!formFields) return null;
    const shape: StringAnyMap = {};
    _.map(formFields.fields, (field, key) => {
      let validator: yup.AnySchema<any> | null = null;
      const { validations, errorMessages } = field;
      const { title, type } = field.fieldProps;
      if (
        type === "TEXT_BOX" ||
        type === "RADIO_GROUP" ||
        type === "DROP_DOWN"
      ) {
        validator = (
          validations.required
            ? yup
                .string()
                .required(errorMessages?.required || `${title} is required`)
            : yup.string()
        )
          .min(
            validations.min,
            errorMessages?.min ||
              `${title} cannot be less than ${errorMessages?.min} characters in length`
          )
          .max(
            validations.max,
            errorMessages?.max ||
              `${title} cannot exceed ${errorMessages?.max} characters in length`
          )
          .matches(
            new RegExp(validations.regex),
            errorMessages?.regex ||
              `${title} doesn't match the pattern ${errorMessages?.regex}`
          );
      } else if (type === "CHECK_BOX") {
        validator = validations.required
          ? yup
              .bool()
              .required(errorMessages?.required || `${title} is required`)
          : yup.bool();
      }
      shape[key] = validator;
    });
    shape["name"] = yup.string().required("Name is required");
    return yup.object().shape(shape);
  },
};
