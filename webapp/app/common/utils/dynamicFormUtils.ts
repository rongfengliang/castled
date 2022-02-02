import { StringAnyMap } from "./types";
import { FormFieldsDto, GroupActivator } from "./../dtos/FormFieldsDto";
import _ from "lodash";
import * as yup from "yup";
import jexl from "jexl";
import jsonUtils from "./jsonUtils";

const isFieldHidden = (
  groupActivator: GroupActivator,
  namePrefix: string | undefined,
  values: any,
  depValues: any[]
): boolean => {
  let skip = false;
  if (!groupActivator) return skip;
  for (const dependency of groupActivator.dependencies) {
    const dependencyName = namePrefix
      ? `${namePrefix}.${dependency}`
      : dependency;
    const depValue: any = _.get(values, dependencyName);
    if (!depValue) {
      skip = true;
      break;
    }
    depValues.push(JSON.stringify(depValue));
  }
  if (groupActivator?.condition) {
    skip = !jexl.evalSync(groupActivator.condition!, values);
  }
  return skip;
};

const getValidationSchema = (
  formFields: FormFieldsDto | undefined,
  namePrefix: string | undefined,
  values: any,
  moreValidations: StringAnyMap
) => {
  if (!formFields) return null;
  const configShape: StringAnyMap = {};
  let shape: StringAnyMap = {};
  _.map(formFields.fields, (field, key) => {
    let fieldValidator: yup.AnySchema<any> | null = null;
    const { validations, errorMessages } = field;
    const { title, type } = field.fieldProps;
    const groupActivator = formFields.groupActivators[field.group];
    const depValues: any[] = [];
    let skip = isFieldHidden(groupActivator, namePrefix, values, depValues);
    if (skip) return true;
    if (type === "TEXT_BOX" || type === "RADIO_GROUP" || type === "DROP_DOWN") {
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
  return yup.object().shape({ ...shape, ...moreValidations });
};

const getValidationErrors = (
  formFields: FormFieldsDto | undefined,
  namePrefix: string | undefined,
  values: any,
  moreValidations: StringAnyMap
): StringAnyMap => {
  const schema = getValidationSchema(
    formFields,
    namePrefix,
    values,
    moreValidations
  );
  if (!schema) return {};
  let errors: StringAnyMap = {};
  try {
    schema.validateSync(values, { abortEarly: false });
  } catch (e) {
    const errorMeta: any = jsonUtils.safeJSON(e);
    for (var meta of errorMeta.inner) {
      _.set(errors, meta.path, meta.message);
    }
  }
  return errors;
};

export default {
  getValidationSchema,
  getValidationErrors,
  isFieldHidden,
};
