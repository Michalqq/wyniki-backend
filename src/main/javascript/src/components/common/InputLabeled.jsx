import React, { useState, useEffect } from "react";

export const InputLabeled = ({
  name = "",
  value = "",
  label = "",
  inputPlaceholder = "",
  onlyNumber = false,
  handleChange,
  disabled = false,
  max,
  big = false,
}) => {
  const [error, setError] = useState(false);

  useEffect(() => {
    if (error) setTimeout(() => setError(false), 4000);
  }, [error]);

  const numericValidation = (newValue) => {
    if (/\D/.test(newValue)) {
      return "";
    }
    return newValue;
  };

  const maxValidation = (newValue) => {
    if (max >= newValue) return newValue;

    setError(true);
    return "";
  };

  const onChange = (val) => {
    value = onlyNumber ? numericValidation(val) : val;
    value = max ? maxValidation(val) : val;
    if (handleChange !== undefined) handleChange(value);
  };

  const errorClass = error ? " border border-danger border-3 rounded" : "";

  return (
    <div className="centered-grid form-group p-2">
      <span className={"input-group-text " + (big ? "" : "my-input")} id="">
        {label}
      </span>
      <input
        name={name}
        className={"form-control " + (big ? "" : "my-input") + errorClass}
        value={value}
        placeholder={inputPlaceholder}
        onChange={(e) => onChange(e.target.value)}
        disabled={disabled}
        max={max}
      ></input>
    </div>
  );
};
