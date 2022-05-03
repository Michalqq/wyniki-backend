import React from "react";
import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";

export const MyButton = ({ variant, onClick, isLoading, msg, loadingMsg }) => {
  return (
    <Button
      className={"m-1"}
      variant={variant}
      onClick={onClick}
      disabled={isLoading}
    >
      {isLoading ? (
        <>
          <Spinner size="sm" animation="border" /> {" " + loadingMsg}
        </>
      ) : (
        <>{msg}</>
      )}
    </Button>
  );
};

export const RadioButton = ({
  onClick,
  label,
  isConfirmed = false,
  name,
  value,
}) => {
  return (
    <div className="form-check">
      <input
        onClick={onClick}
        defaultChecked={isConfirmed}
        className="form-check-input"
        type="radio"
        name={name}
        value={value}
      ></input>
      <label className="form-check-label">{label}</label>
    </div>
  );
};
