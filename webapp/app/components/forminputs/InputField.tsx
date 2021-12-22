import React, { useEffect, useState } from "react";
import { FieldInputProps, FieldMetaProps, useField } from "formik";
import cn from "classnames";
import { InputBaseProps } from "@/app/common/dtos/InputBaseProps";
import { AxiosResponse } from "axios";
import { DataFetcherResponseDto } from "@/app/common/dtos/DataFetcherResponseDto";
import TextareaAutosize from "react-textarea-autosize";

export interface InputFieldProps extends InputBaseProps {
  type: string;
  minRows?: number;
  optionsRef?: string;
  dataFetcher?: (
    optionsRef: string
  ) => Promise<AxiosResponse<DataFetcherResponseDto>>;
}

const InputField = ({
  title,
  required,
  description,
  className,
  onChange,
  setFieldValue,
  optionsRef,
  dataFetcher,
  ...props
}: InputFieldProps) => {
  const [field, meta] = useField({
    ...props,
    validate: (v) => {
      if (!v?.length && props?.validations?.required) return title + ' Field is Required';
    }
  });
  const isHidden = props.type === "hidden";
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    if (optionsRef) {
      setLoading(true);
      dataFetcher?.(optionsRef).then(({ data }) => {
        setFieldValue(field.name, data.options[0].value);
        setLoading(false);
      });
    }
  }, [optionsRef]);
  return (
    <div className={className ? className : cn({ "mb-3": !isHidden })}>
      {title && !isHidden && (
        <label htmlFor={props.id || props.name} className="form-label">
          {required && <span className="required-icon">*</span>}
          {title}
        </label>
      )}
      {getInput(field, onChange, props, optionsRef, meta)}
      <div className={cn({'spinner-border spinner-border-sm': loading && !isHidden})}></div>
      {meta.touched && meta.error ? (
        <div className="error" style={{ color: 'red' }}>
          {meta.error}
        </div>
      ) : null}
    </div>
  );
};

function getInput(
  field: FieldInputProps<any>,
  onChange: ((value: string) => void) | undefined,
  props: any,
  optionsRef?: string,
  meta?: any
) {
  if (props.type === "textarea") {
    return (
      <TextareaAutosize
        {...field}
        {...props}
        onChange={(e) => {
          field.onChange(e);
          onChange?.(e.currentTarget.value);
        }}
        className={cn(props.className, "form-control", {
          "required-field": meta.touched && meta.error,
        })}
        defaultValue={field.value}
      />
    );
  } else {
    return (
      <input
        onChange={(e) => {
          field.onChange(e);
          onChange?.(e.currentTarget.value);
        }}
        onBlur={field.onBlur}
        {...props}
        className={cn(meta.touched && meta.error && 'is-invalid-form', "form-control")}
        value={field.value}
        defaultValue={field.value}
        disabled={optionsRef}
      />
    );
  }
}

export default InputField;