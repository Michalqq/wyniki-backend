import React from "react";

import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

export const OkCancelModal = ({
  show,
  title,
  text,
  handleAccept,
  handleClose,
}) => {
  return (
    <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header closeButton className="bg-dark-green text-white">
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <p
          style={{
            whiteSpace: "break-spaces",
          }}
        >
          {text}
        </p>
      </Modal.Body>

      <Modal.Footer className={"justify-content-center"}>
        <Button className={"mx-3"} variant="secondary" onClick={handleClose}>
          Anuluj
        </Button>
        <Button
          className={"px-4 mx-3"}
          variant="success"
          onClick={handleAccept}
        >
          OK
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export const OkModal = ({ show, title, text, handleAccept }) => {
  return (
    <Modal show={show} onHide={handleAccept} backdrop="static" keyboard={false}>
      <Modal.Header closeButton className="bg-dark-green text-white">
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <p
          style={{
            whiteSpace: "break-spaces",
          }}
        >
          {text}
        </p>
      </Modal.Body>

      <Modal.Footer className={"justify-content-center"}>
        <Button
          className={"px-4 mx-3"}
          variant="success"
          onClick={handleAccept}
        >
          OK
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
