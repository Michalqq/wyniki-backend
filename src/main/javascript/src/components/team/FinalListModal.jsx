import React, { useState, useEffect } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { backendUrl, fetchCreateFinalList } from "../utils/fetchUtils";
import { Selector } from "../common/Selector";
import { MyButton } from "../common/Button";
import authHeader from "../../service/auth-header";
import { InputLabeled } from "../common/InputLabeled";
import { TimePicker } from "../common/DateInput";
import { CalendarContainer } from "react-datepicker";
import { OkModal } from "../common/Modal";

import moment from "moment";
import { closeOnBack } from "../utils/utils";

export const FinalListModal = ({ show, handleClose, eventId }) => {
  const [creating, setCreating] = useState(false);
  const [okModal, setOkModal] = useState();
  const [stage, setStage] = useState();
  const [frequency, setFrequency] = useState(1);
  const [psOptions, setPsOptions] = useState([]);
  const [startTime, setStartTime] = useState();

  useEffect(() => {
    if (!show) return;

    closeOnBack(handleClose);
    setCreating(false);
    setFrequency(1);
    axios
      .get(`${backendUrl()}/event/getPsOptions?eventId=${eventId}`, {
        headers: authHeader(),
      })
      .then((res) => {
        setPsOptions(res.data);
        setStage(res.data[0]?.value);
      });
  }, [show]);

  const DatePickerContainer = ({ className, children }) => {
    return (
      <>
        <CalendarContainer className={className}>
          <div style={{ display: "flex" }}>{children}</div>
        </CalendarContainer>
      </>
    );
  };

  const getFormattedTime = () => {
    return moment(startTime).format("YYYY-MM-DDTHH:mm:ss.SSS[Z]");
  };

  return (
    <>
      <Modal
        style={{
          zIndex: okModal ? 1000 : 1055,
        }}
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header closeButton className="bg-dark-green text-white">
          <Modal.Title className="text-white">{"Lista startowa"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="row text-center">
            <div className="col-lg-12 pb-3">
              <Card className="">
                <Card.Body>
                  <p
                    style={{
                      whiteSpace: "break-spaces",
                    }}
                  >
                    {`Na liście startowej znajdą się wszyscy zawodnicy którzy NIE otrzymali negatywnego BK i/lub OA oraz \nNIE dostali kary wykluczającej z zawodów (dyskwalifikacja lub wycofanie)
                    \nNegatywny BK i/lub OA możesz dodać w panelu danych zawodnika.
                    \nPlik z listą startową zostanie dodany w komunikatach wydarzenia.
                    `}
                  </p>
                  <Selector
                    label={"Odcinek na który dodawana jest lista startowa"}
                    options={psOptions}
                    value={stage}
                    handleChange={(value) => setStage(value)}
                    isValid={true}
                  />
                  <TimePicker
                    label={"Czas startu odcinka"}
                    onChange={(value) => setStartTime(value)}
                    calendarContainer={DatePickerContainer}
                    selected={startTime}
                  />
                  <InputLabeled
                    label="Co ile minut start zawodnika"
                    value={frequency}
                    handleChange={(e) => setFrequency(e.target.value)}
                    onlyNumber={true}
                    type={"number"}
                    big={true}
                  />
                </Card.Body>
              </Card>
            </div>
          </div>
          <div className="text-center">
            <MyButton
              variant="success"
              isLoading={creating}
              onClick={() => {
                setCreating(true);
                fetchCreateFinalList(
                  eventId,
                  stage,
                  getFormattedTime(startTime),
                  frequency,
                  (data) => {
                    setCreating(false);
                    setOkModal(data);
                  }
                );
              }}
              disabled={!stage || !frequency || !startTime}
              msg="Dodaj listę startową"
              loadingMsg="Dodawanie listy startowej"
            />
            <Button
              className={"m-1"}
              variant="secondary"
              onClick={() => handleClose()}
            >
              {"Wróć"}
            </Button>
          </div>
        </Modal.Body>
      </Modal>
      <OkModal
        show={okModal !== undefined}
        title={""}
        text={
          okModal
            ? "Lista startowa została dodana do komunikatów wydarzenia"
            : "Akcja nieudana"
        }
        handleAccept={() => setOkModal()}
      />
    </>
  );
};
