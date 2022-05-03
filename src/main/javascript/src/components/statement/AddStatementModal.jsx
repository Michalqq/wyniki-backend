import React, { useState, useEffect } from "react";
import axios from "axios";
import Button from "react-bootstrap/Button";
import { InputLabeled } from "../common/InputLabeled";
import Modal from "react-bootstrap/Modal";
import Spinner from "react-bootstrap/Spinner";
import { faBook, faCarAlt } from "@fortawesome/free-solid-svg-icons";
import { CustomDatePicker } from "../common/DateInput";
import { CalendarContainer } from "react-datepicker";
import { backendUrl } from "../utils/fetchUtils";
import authHeader from "../../service/auth-header";

export const AddStatementModal = ({ show, handleClose, eventId }) => {
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState("");
  const [statement, setStatement] = useState({});
  const [postFile, setPostFile] = useState();

  useEffect(() => {
    setMsg("");
    setLoading(false);
    setPostFile();
    setStatement({
      eventId: eventId,
      date: new Date(),
      name: "",
      description: "",
      file: null,
    });
  }, [show]);

  const handleSubmit = (event) => {
    setLoading(true);
    event.preventDefault();
    axios
      .post(`${backendUrl()}/statement/addStatement`, statement, {
        headers: authHeader(),
      })
      .then((res) => {
        if (postFile) fetchPostFile(res.data);
        else {
          setMsg("Dodano komunikat");
          setLoading(false);
        }
      });
  };

  const fetchPostFile = (statementId) => {
    axios
      .post(
        `${backendUrl()}/statement/addFileToStatement?statementId=${statementId}`,
        postFile,
        {
          headers: authHeader(),
        }
      )
      .then((res) => {
        setMsg(res.data);
        setLoading(false);
      });
  };

  const handleChange = (event) => {
    setStatement({ ...statement, [event.target.name]: event.target.value });
  };

  const DatePickerContainer = ({ className, children }) => {
    return (
      <>
        <CalendarContainer className={className}>
          <div style={{ display: "flex" }}>{children}</div>
        </CalendarContainer>
      </>
    );
  };
  const addFile = (e) => {
    const currentFile = e.target.files[0];
    const formData = new FormData();
    formData.append("file", currentFile);

    setStatement({
      ...statement,
      fileName: currentFile.name,
    });
    setPostFile(formData);
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      keyboard={false}
      size="lg"
    >
      <Modal.Header className="bg-dark text-white" closeButton>
        <Modal.Title>Dodawanie komunikatu</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="d-flex justify-content-center text-center">
          <form onSubmit={handleSubmit}>
            <div className="row d-flex">
              <CustomDatePicker
                label={"Data komunikatu"}
                onChange={(value) =>
                  setStatement({ ...statement, date: value })
                }
                selected={statement.date}
                calendarContainer={DatePickerContainer}
                minDate={null}
                maxDate={null}
              />
              <InputLabeled
                label="Nazwa komunikatu"
                name="name"
                handleChange={handleChange}
                value={statement.name}
                big={true}
                required={true}
                icon={faBook}
              />
            </div>
            <div className="row d-flex">
              <div className="col-lg-12">
                <InputLabeled
                  label="Treść komunikatu"
                  inputPlaceholder="Dodaj treść komunikatu tekstowego lub załącz plik który będzie udostępniony do pobrania"
                  name="description"
                  handleChange={handleChange}
                  value={statement.description}
                  big={true}
                  icon={faCarAlt}
                  multiline={5}
                />
              </div>
            </div>
            <div className="row d-flex">
              <div className="col-lg-12">
                <span className={"font14 input-group-text py-0 "} id="">
                  Dodaj plik
                </span>
                <div class="input-group mb-0">
                  <input
                    type="file"
                    name="file"
                    accept="application/pdf,application/vnd.ms-excel"
                    onChange={(e) => addFile(e)}
                  />
                </div>
              </div>
            </div>
            <div className="text-center" style={{ height: "50px" }}>
              <h4 className="fw-bold">{msg}</h4>
              {loading && (
                <Spinner animation="border" variant="secondary" size="lg" />
              )}
            </div>
            <Button
              className={"m-1"}
              variant="success"
              type="submit"
              //   disabled={msg}
            >
              Dodaj
            </Button>
            <Button
              className={"m-1"}
              variant="secondary"
              onClick={() => handleClose()}
            >
              {"Wyjdź"}
            </Button>
          </form>
        </div>
      </Modal.Body>
    </Modal>
  );
};
