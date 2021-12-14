import React, { useEffect, useState } from "react";
import { FieldInputProps, useField } from "formik";
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
  isValid,
  optionsRef,
  dataFetcher,
  ...props
}: InputFieldProps) => {
  const [field, meta] = useField(props);
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
          {title}
          {required && <span className="required-icon">*</span>}
        </label>
      )}
      {getInput(
        field,
        onChange,
        props,
        optionsRef,
        (meta.touched || !isValid) && !!meta.error
      )}
      {loading && !isHidden && (
        <div className="spinner-border spinner-border-sm"></div>
      )}
      {(meta.touched || !isValid) && !!meta.error ? (
        <div className="error" style={{ color: "#f74c3c", fontSize: "12px" }}>
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
  valid?: boolean
) {
  if (props.type === "textarea") {
    return (
      <TextareaAutosize
        // onChange={(e) => {
        //   field.onChange(e);
        //   onChange?.(e.currentTarget.value);
        // }}
        {...field}
        {...props}
        className={cn(
          props.className,
          "form-control",
          valid ? "required-field" : ""
        )}
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
        className={cn(
          props.className,
          "form-control",
          valid ? "required-field" : ""
        )}
        value={field.value}
        defaultValue={field.value}
        disabled={optionsRef}
      />
    );
  }
}

export default InputField;
