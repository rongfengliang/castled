import { StringAnyMap } from "./types";
import { FormFieldsDto } from "./../dtos/FormFieldsDto";
import _ from "lodash";
import * as yup from "yup";

export default {
  getValidation: (
    formFields: FormFieldsDto | undefined,
    namePrefix: string | undefined,
    moreValidations: StringAnyMap
  ) => {
    if (!formFields) return null;
    const configShape: StringAnyMap = {};
    let shape: StringAnyMap = {};
    _.map(formFields.fields, (field, key) => {
      let fieldValidator: yup.AnySchema<any> | null = null;
      const { validations, errorMessages } = field;
      const { title, type } = field.fieldProps;
      if (
        type === "TEXT_BOX" ||
        type === "RADIO_GROUP" ||
        type === "DROP_DOWN"
      ) {
        let validator: yup.StringSchema<any> = yup.string();
        if (validations.required) {
          validator = validator.required(
            errorMessages?.required || `${title} is required`
          );
        }
        if (validations.min) {
          validator = validator.min(
            validations.min,
            errorMessages?.min ||
              `${title} cannot be less than ${errorMessages?.min} characters in length`
          );
        }
        if (validations.max) {
          validator = validator.max(
            validations.max,
            errorMessages?.max ||
              `${title} cannot be less than ${errorMessages?.min} characters in length`
          );
        }
        if (validations.regex) {
          validator = validator.matches(
            new RegExp(validations.regex),
            errorMessages?.regex ||
              `${title} doesn't match the pattern ${errorMessages?.regex}`
          );
        }
        fieldValidator = validator;
      } else if (type === "CHECK_BOX") {
        let validator: yup.BooleanSchema<any> = yup.bool();
        if (validations.required) {
          validator = validator.required(
            errorMessages?.required || `${title} is required`
          );
        }
        fieldValidator = validator;
      }
      if (fieldValidator) {
        configShape[key] = fieldValidator;
      }
    });
    if (namePrefix) {
      shape[namePrefix] = yup.object().shape(configShape);
    } else {
      shape = configShape;
    }
    return yup.object().shape(shape);
  },
};
