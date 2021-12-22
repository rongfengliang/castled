import { StringAnyMap } from "./types";
import { FormFieldMeta, FormFieldsDto } from "./../dtos/FormFieldsDto";
import _ from "lodash";
import * as yup from "yup";
import jexl from "jexl";

export default {
  getValidation: (
    formFields: any | undefined,
    namePrefix: string | undefined,
    moreValidations: StringAnyMap,
    tunnel: boolean | undefined
  ) => {
    if (!formFields) return null;
    console.log(tunnel);
    const configShape: StringAnyMap = {};
    let shape: StringAnyMap = {};
    const orderedFieldsInfo: any[] = [];
    const names = Object.keys(formFields.fields);

    names.forEach((key, i) => {
      const group = formFields.fields[key].group;
      orderedFieldsInfo.push({ order: i, key, group, field: formFields.fields[key] });
    });
    orderedFieldsInfo.sort(function (a: any, b: any) {
      if (a.group < b.group) return -1;
      if (a.group > b.group) return 1;
      return a.order - b.order;
    });
    _.map(orderedFieldsInfo, (fieldObj) => {
      let fieldValidator: yup.AnySchema<any> | null = null;
      const { validations, errorMessages } = fieldObj.field;
      const { title, type } = fieldObj.field.fieldProps;
      if (fieldObj.field.group in formFields.groupActivators && !tunnel) {

      } else if (
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
        if (validations.required && !tunnel) {
          validator = validator.required(
            errorMessages?.required || `${title} is required`
          );
        }
        fieldValidator = validator;
      }
      if (fieldValidator) {
        configShape[fieldObj.key] = fieldValidator;
      }
    });
    if (namePrefix) {
      shape[namePrefix] = yup.object().shape(configShape);
    } else {
      shape = configShape;
    }
    return yup.object().shape({ ...shape, ...moreValidations });
  },
};