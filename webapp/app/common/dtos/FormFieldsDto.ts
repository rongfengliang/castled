import { FormFieldType } from "../enums/FormFieldType";
import { CodeBlock } from "./CodeBlock";
import { HelpText } from "./HelpText";

export interface FormFieldsDto {
  fields: {
    [key: string]: FormFieldMeta;
  };
  helpText?: HelpText;
  codeBlock?: CodeBlock;
  groupActivators: {
    [key: string]: {
      dependencies: string[];
      condition?: string;
    };
  };
}

export interface FormFieldMeta {
  group: string;
  fieldProps: {
    type: FormFieldType;
    title: string;
    description: string;
    placeholder?: string;
    optionsRef?: string;
  };
  validations: {
    required: boolean;
    min: number;
    max: number;
    regex: string;
  };
  errorMessages?: {
    required: string;
    min: string;
    max: string;
    regex: string;
  };
}
