import React, { useState, useEffect } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import { InputLabeled } from "../common/InputLabeled";
import { backendUrl } from "../utils/fetchUtils";
import { MyDatePicker } from "../common/DateInput";
import { Selector } from "../common/Selector";
import {
  faCar,
  faDigitalTachograph,
  faIdBadge,
  faNewspaper,
} from "@fortawesome/free-solid-svg-icons";
import { closeOnBack } from "../utils/utils";

export const CarPanelModal = ({
  show,
  handleClose,
  teamId,
  carToEdit,
  mode,
}) => {
  const [disabled, setDisabled] = useState(false);
  const [options, setOptions] = useState();
  const [car, setCar] = useState({
    carId: null,
    brand: "",
    model: "",
    year: "",
    licensePlate: "",
    vin: "",
    engineCapacity: "",
    turbo: false,
    driveType: "1",
    petrol: "BENZYNA",
    insurance: "",
    insuranceExpiryDate: new Date(),
    carInspectionExpiryDate: new Date(),
  });

  const setEmptyCar = () => {
    setCar({
      carId: null,
      brand: "",
      model: "",
      year: "",
      licensePlate: "",
      vin: "",
      engineCapacity: "",
      turbo: false,
      driveType: "1",
      petrol: "BENZYNA",
      insurance: "",
      insuranceExpiryDate: new Date(),
      carInspectionExpiryDate: new Date(),
    });
  };

  useEffect(() => {
    if (show) {
      closeOnBack(handleClose);
      axios.get(`${backendUrl()}/team/getTeamOptionList`).then((res) => {
        setOptions(res.data);
      });
      if (carToEdit !== true) setCar(carToEdit);
    }
    if (mode === "preview") setDisabled(true);
  }, [show]);

  const handleChange = (event) => {
    setCar({ ...car, [event.target.name]: event.target.value });
  };

  const fetchAddCar = () => {
    axios
      .post(`${backendUrl()}/team/addCar?teamId=${teamId}`, {
        ...car,
        teamId: teamId,
      })
      .then(() => {
        setEmptyCar();
        handleClose();
      });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    fetchAddCar();
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      keyboard={false}
      size="lg"
    >
      <Modal.Header closeButton className="bg-dark-green text-white">
        <Modal.Title className="text-white">
          {disabled ? "Dane samochodu" : "Dodawanie samochodu"}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form onSubmit={handleSubmit}>
          <div className="row text-center">
            <div className="col-lg-12 pb-3">
              <Card className="">
                <Card.Body>
                  <div className="row">
                    <div className="col-lg-4 px-1">
                      <InputLabeled
                        label="Marka"
                        name="brand"
                        handleChange={handleChange}
                        big={true}
                        value={car.brand}
                        icon={faCar}
                        required={true}
                        disabled={disabled}
                      />
                    </div>
                    <div className="col-lg-4 px-1">
                      <InputLabeled
                        label="Model"
                        name="model"
                        handleChange={handleChange}
                        big={true}
                        value={car.model}
                        icon={faCar}
                        required={true}
                        disabled={disabled}
                      />
                    </div>
                    <div className="col-lg-4 px-1">
                      <InputLabeled
                        label="Rok produkcji"
                        name="year"
                        handleChange={handleChange}
                        big={true}
                        value={car.year}
                        icon={faIdBadge}
                        required={true}
                        onlyNumber={true}
                        disabled={disabled}
                      />
                    </div>
                  </div>
                  <div className="row py-1">
                    <div className="col-lg-4 px-1">
                      <InputLabeled
                        label="Nr. rejestracyjny"
                        name="licensePlate"
                        handleChange={handleChange}
                        big={true}
                        value={car.licensePlate}
                        icon={faNewspaper}
                        required={true}
                        disabled={disabled}
                      />
                    </div>
                    <div className="col-lg-8 px-1">
                      <InputLabeled
                        label="VIN"
                        name="vin"
                        handleChange={handleChange}
                        big={true}
                        value={car.vin}
                        icon={faDigitalTachograph}
                        required={true}
                        disabled={disabled}
                      />
                    </div>
                  </div>
                  <div className="row py-1">
                    <div className="col-lg-3 px-1">
                      <InputLabeled
                        label="Silnik [cm3]"
                        name="engineCapacity"
                        handleChange={handleChange}
                        big={true}
                        value={car.engineCapacity}
                        required={true}
                        onlyNumber={true}
                        disabled={disabled}
                      />
                    </div>
                    <div className="col-lg-3 px-1">
                      <Form>
                        <span
                          className={"py-0 mb-2 mt-1 mx-1 input-group-text"}
                        >
                          Turbo
                        </span>
                        <Form.Check
                          inline
                          label="TAK"
                          name="turbo"
                          type={"radio"}
                          id={`inline-1`}
                          checked={car.turbo}
                          onClick={() => setCar({ ...car, turbo: true })}
                          disabled={disabled}
                        />
                        <Form.Check
                          inline
                          label="NIE"
                          name="turbo"
                          type={"radio"}
                          id={`inline-2`}
                          checked={!car.turbo}
                          onClick={() => setCar({ ...car, turbo: false })}
                          disabled={disabled}
                        />
                      </Form>
                    </div>
                    <div className="col-lg-3 px-1">
                      <Selector
                        label={"Rodzaj napędu"}
                        options={options?.driveTypeOption}
                        handleChange={(value) =>
                          setCar({ ...car, driveType: value })
                        }
                        value={car.driveType}
                        isValid={true}
                        disabled={disabled}
                      />
                    </div>
                    <div className="col-lg-3 px-1">
                      <Selector
                        label={"Paliwo"}
                        options={options?.petrolOption}
                        handleChange={(value) =>
                          setCar({ ...car, petrol: value })
                        }
                        value={car.petrol}
                        isValid={true}
                        disabled={disabled}
                      />
                    </div>
                  </div>
                  <div className="row py-1">
                    <div className="col-lg-5 px-0">
                      <MyDatePicker
                        label={"Data ważności polisy"}
                        onChange={(val) =>
                          setCar({ ...car, insuranceExpiryDate: val })
                        }
                        selected={car?.insuranceExpiryDate}
                        disabled={disabled}
                      />
                    </div>
                    <div className="col-lg-7 px-1">
                      <InputLabeled
                        label="Nr. polisy ubezpieczeniowej + firma"
                        name="insurance"
                        handleChange={handleChange}
                        big={true}
                        value={car.insurance}
                        required={true}
                        disabled={disabled}
                      />
                    </div>
                  </div>
                  <div className="row py-1">
                    <div className="col-lg-5 px-0">
                      <MyDatePicker
                        label={"Data ważn. przeglądu samochodu"}
                        onChange={(val) =>
                          setCar({ ...car, carInspectionExpiryDate: val })
                        }
                        selected={car?.carInspectionExpiryDate}
                        disabled={disabled}
                      />
                    </div>
                  </div>
                </Card.Body>
              </Card>
            </div>
          </div>
          <div className="text-center">
            <p className="m-0">
              Wprowadzone dane zostaną przekazane organizatorowi wydarzenia.
            </p>
            {!disabled && (
              <Button className={"m-1"} variant="success" type="submit">
                {carToEdit !== true ? "Zapisz zmiany" : "Dodaj samochód"}
              </Button>
            )}
            <Button
              className={"m-1"}
              variant="secondary"
              onClick={() => {
                setEmptyCar();
                handleClose();
              }}
            >
              {disabled ? "Powrót" : "Anuluj"}
            </Button>
          </div>
        </form>
      </Modal.Body>
    </Modal>
  );
};
