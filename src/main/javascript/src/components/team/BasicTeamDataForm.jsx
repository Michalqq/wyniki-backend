import React, { useState, useEffect } from "react";
import axios from "axios";
import { backendUrl } from "../utils/fetchUtils";
import { InputLabeled } from "../common/InputLabeled";
import Form from "react-bootstrap/Form";
import Spinner from "react-bootstrap/Spinner";
import { Selector } from "../common/Selector";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {
  faBuilding,
  faCar,
  faCarAlt,
  faUserAstronaut,
  faUserClock,
  faUserFriends,
} from "@fortawesome/free-solid-svg-icons";

export const BasicTeamDataForm = ({
  show,
  myTeam,
  eventId,
  onSave,
  okBtnLabel,
  handleClose,
  title,
}) => {
  const [loading, setLoading] = useState(false);
  const [options, setOptions] = useState();
  const [team, setTeam] = useState(myTeam);

  useEffect(() => {
    if (show) {
      axios.get(`${backendUrl()}/team/getTeamOptionList`).then((res) => {
        setOptions(res.data);
      });
      setLoading(false);
    }
  }, [show]);

  const handleSubmit = (event) => {
    setLoading(true);
    event.preventDefault();
    onSave(team);
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      keyboard={false}
      size="l"
    >
      <Modal.Header closeButton className="bg-dark text-white">
        <Modal.Title className="text-white">{title}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="d-flex justify-content-center text-center">
          <form onSubmit={handleSubmit}>
            <div className="row d-flex">
              <div className="col-lg-6">
                <InputLabeled
                  label="Imie i nazwisko kierowcy"
                  name="driver"
                  handleChange={(e) =>
                    setTeam({ ...team, driver: e.target.value })
                  }
                  value={team.driver}
                  big={true}
                  required={true}
                  icon={faUserAstronaut}
                />
              </div>
              <div className="col-lg-6">
                <InputLabeled
                  label="Imie i nazwisko pilota"
                  name="coDriver"
                  handleChange={(e) =>
                    setTeam({ ...team, coDriver: e.target.value })
                  }
                  value={team.coDriver}
                  big={true}
                  icon={faUserClock}
                />
              </div>
            </div>
            <div className="row d-flex">
              <div className="col-lg-6">
                <InputLabeled
                  label="Automobilklub kierowcy"
                  name="club"
                  handleChange={(e) =>
                    setTeam({ ...team, club: e.target.value })
                  }
                  value={team.club}
                  big={true}
                  icon={faBuilding}
                />
              </div>
              <div className="col-lg-6">
                <InputLabeled
                  label="Automobilklub pilota"
                  name="coClub"
                  handleChange={(e) =>
                    setTeam({ ...team, coClub: e.target.value })
                  }
                  value={team.coClub}
                  big={true}
                  icon={faBuilding}
                />
              </div>
            </div>
            <div className="row d-flex">
              <div className="col-lg-12">
                <InputLabeled
                  label="Nazwa Teamu"
                  name="teamName"
                  handleChange={(e) =>
                    setTeam({ ...team, teamName: e.target.value })
                  }
                  value={team.teamName}
                  big={true}
                  icon={faUserFriends}
                />
              </div>
            </div>

            <div className="row d-flex">
              <div className="col-lg-6">
                <InputLabeled
                  label="Marka samochodu"
                  name="carBrand"
                  handleChange={(e) =>
                    setTeam({
                      ...team,
                      currentCar: { ...team.currentCar, brand: e.target.value },
                    })
                  }
                  value={team.currentCar?.brand}
                  big={true}
                  required={true}
                  icon={faCar}
                />
              </div>
              <div className="col-lg-6">
                <InputLabeled
                  label="Model samochodu"
                  name="carModel"
                  handleChange={(e) =>
                    setTeam({
                      ...team,
                      currentCar: { ...team.currentCar, model: e.target.value },
                    })
                  }
                  value={team.currentCar?.model}
                  big={true}
                  required={true}
                  icon={faCarAlt}
                />
              </div>
            </div>
            <div className="row d-flex">
              <div className="col-lg-6">
                <InputLabeled
                  label="Pojemność silnika [cm3]"
                  name="engineCapacity"
                  handleChange={(e) =>
                    setTeam({
                      ...team,
                      currentCar: {
                        ...team.currentCar,
                        engineCapacity: e.target.value,
                      },
                    })
                  }
                  value={team.currentCar?.engineCapacity}
                  big={true}
                  required={true}
                  onlyNumber={true}
                />
              </div>
              <div className="col-lg-6">
                <Selector
                  label={"Rodzaj napędu"}
                  options={options?.driveTypeOption}
                  handleChange={(value) =>
                    setTeam({
                      ...team,
                      currentCar: {
                        ...team.currentCar,
                        driveType: value,
                      },
                    })
                  }
                  value={team.currentCar?.driveType}
                  isValid={true}
                />
              </div>
            </div>
            <div className="d-flex justify-content-center">
              <Form>
                <span className={"py-0 mb-2 mt-1 mx-1 input-group-text"}>
                  Turbo
                </span>
                <Form.Check
                  inline
                  label="TAK"
                  name="turbo"
                  type={"radio"}
                  id={`inline-1`}
                  checked={team.currentCar?.turbo}
                  onClick={() =>
                    setTeam({
                      ...team,
                      currentCar: { ...team.currentCar, turbo: true },
                    })
                  }
                />
                <Form.Check
                  inline
                  label="NIE"
                  name="turbo"
                  type={"radio"}
                  id={`inline-2`}
                  checked={!team.currentCar?.turbo}
                  onClick={() =>
                    setTeam({
                      ...team,
                      currentCar: { ...team.currentCar, turbo: false },
                    })
                  }
                />
              </Form>
            </div>
            <div className="text-center" style={{ height: "50px" }}>
              {loading && (
                <Spinner animation="border" variant="secondary" size="lg" />
              )}
            </div>
            <Button className={"m-1"} variant="success" type="submit">
              {okBtnLabel}
            </Button>
            <Button
              className={"m-1"}
              variant="secondary"
              onClick={() => handleClose()}
            >
              Anuluj
            </Button>
          </form>
        </div>
      </Modal.Body>
    </Modal>
  );
};
