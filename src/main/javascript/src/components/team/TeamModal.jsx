import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { InputLabeled } from "../common/InputLabeled";
import {
  backendUrl,
  fetchBkChecked,
  fetchSaveTeam,
  fetchTeamChecked,
} from "../utils/fetchUtils";
import { MyDatePicker } from "../common/DateInput";
import { CarPanelModal } from "./CarPanelModal";
import Spinner from "react-bootstrap/Spinner";
import { Selector } from "../common/Selector";
import authHeader from "../../service/auth-header";
import {
  faUserAstronaut,
  faUserClock,
  faUserFriends,
  faBuilding,
  faAt,
  faPhoneAlt,
  faCarCrash,
} from "@fortawesome/free-solid-svg-icons";
import { getCarLogo } from "../utils/car";
import { OkModal } from "../common/Modal";
import { closeOnBack } from "../utils/utils";
import { Form } from "react-bootstrap";

export const TeamModal = ({ show, handleClose, handleOk, myEvent, mode }) => {
  const [disable, setDisable] = useState(false);
  const [addingToEvent, setAddingToEvent] = useState(false);
  const [team, setTeam] = useState(undefined);
  const [carsOption, setCarsOption] = useState([]);
  const [addCar, setAddCar] = useState();
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);
  const [okModal, setOkModal] = useState();
  const [manualCarClass, setManualCarClass] = useState();
  const [classesOptions, setClassesOptions] = useState([]);

  useEffect(() => {
    if (!show) return;

    closeOnBack(handleClose);
    setMsg("");
    if (mode === undefined || mode === "teamPanel") fetchGetTeam();
    if (mode === "preview") {
      fetchTeam(myEvent?.team?.teamId);
      setDisable(true);
    }
    fetchPsOptions();
    setCarsOption([]);
    setAddingToEvent(false);
    setLoading(false);
  }, [show]);

  const fetchGetTeam = () => {
    axios
      .get(`${backendUrl()}/team/getTeam`, {
        headers: authHeader(),
      })
      .then((res) => {
        if (res.data !== "") setTeam(res.data);
      });
  };

  const fetchTeam = (teamId) => {
    axios
      .get(`${backendUrl()}/team/getTeamByTeamId?teamId=${teamId}`, {
        headers: authHeader(),
      })
      .then((res) => {
        if (res.data !== "")
          if (mode === "preview") {
            const et = myEvent?.team;
            setTeam({
              ...res.data,
              driver: et.driver,
              coDriver: et.coDriver,
              club: et.club,
              coClub: et.coClub,
              teamName: et.teamName,
            });
          } else setTeam(res.data);
      });
  };

  const fetchPsOptions = () => {
    axios
      .get(
        `${backendUrl()}/event/getAllEventClassesOptions?eventId=${
          myEvent?.eventId
        }`
      )
      .then((res) => setClassesOptions(res.data || []));
  };

  const fetchManualCarClass = (teamId) => {
    axios
      .get(
        `${backendUrl()}/event/getManualCarClass?eventId=${
          myEvent?.eventId
        }&teamId=${teamId}`
      )
      .then((res) => setManualCarClass(res.data));
  };

  const fetchSaveManualCarClass = (teamId) => {
    axios.post(
      `${backendUrl()}/event/saveManualCarClass?eventId=${
        myEvent?.eventId
      }&teamId=${teamId}&carClassId=${manualCarClass}`
    );
  };

  useEffect(() => {
    let tempOptions = [];
    if (team !== undefined && team.cars) {
      team.cars.map((x) => {
        const option = {
          label: x.brand + " " + x.model + " " + x.licensePlate,
          value: x.carId,
          defValue: false,
        };
        tempOptions.push(option);
      });
      setCarsOption(tempOptions);
    }
    if (team?.teamId) fetchManualCarClass(team.teamId);
  }, [team]);

  const fetchAddTeam = () => {
    setAddingToEvent(true);
    setLoading(true);
    axios
      .post(`${backendUrl()}/team/addTeam?eventId=${myEvent.eventId}`, team, {
        headers: authHeader(),
      })
      .then((res) => {
        if (myEvent?.carClassManual) fetchSaveManualCarClass(res.data);
        handleOk();
        handleClose();
      });
  };

  const handleChange = (event) => {
    setTeam({ ...team, [event.target.name]: event.target.value });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (myEvent?.carClassManual && !manualCarClass) {
      setMsg("Proszę wybrać klasę samochodu");
      return;
    }
    if (mode === "teamPanel") return saveTeamData();

    if (!validation()) return;

    fetchAddTeam();
  };
  const saveTeamData = () => {
    fetchSaveTeam(team, () => {
      setMsg("Twoje dane zostały zapisane");
    });
  };

  const validation = () => {
    let message = "";

    if (!team.currentCar) {
      message += "\nProszę wybrać lub dodać auto";
      setMsg(message);
      return false;
    }

    if (
      new Date(team.currentCar.insuranceExpiryDate).getTime() <
      new Date(myEvent.date).getTime()
    )
      message += "\nPolisa auta będzie nieaktualna w dniu wydarzenia";

    if (
      new Date(team.currentCar.carInspectionExpiryDate).getTime() <
      new Date(myEvent.date).getTime()
    )
      message += "\nPrzegląd auta będzie nieaktualny w dniu wydarzenia";

    if (message !== "") message += "\nNie można zapisać!";

    setMsg(message.startsWith("\n") ? message.substring(1) : message);
    return message === "";
  };

  useEffect(() => {
    setTimeout(() => setMsg(""), 8000);
  }, [msg]);

  return (
    <>
      <Modal
        style={{ zIndex: addCar || okModal ? 1000 : 1055 }}
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="xl"
      >
        <Modal.Header className="bg-dark-green text-white" closeButton>
          <Modal.Title className="w-100 row">
            <div className="col-lg-8">
              {disable ? "Dane zawodnika" : `Panel zawodnika`}
            </div>
          </Modal.Title>
        </Modal.Header>

        <Modal.Body className={"px-3 py-0"}>
          <form id="teamForm" onSubmit={handleSubmit}>
            {team === undefined && (
              <div className="text-center">
                <Spinner animation="border" variant="secondary" size="lg" />
              </div>
            )}
            {team !== undefined && (
              <div className="row">
                <div className="col-lg-6 pb-3 px-1">
                  <Card className="text-center">
                    <Card.Header className="bg-dark-green text-white pt-1 pb-0">
                      <h5>Kierowca</h5>
                    </Card.Header>
                    <Card.Body>
                      <div className="row d-flex">
                        <div className="col-lg-12 px-1">
                          <InputLabeled
                            label="Imie i nazwisko"
                            name="driver"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.driver}
                            icon={faUserAstronaut}
                            required={true}
                          />
                        </div>
                      </div>
                      <div className="row d-flex">
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Nazwa Teamu"
                            name="teamName"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.teamName}
                            icon={faUserFriends}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Automobilklub"
                            name="club"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.club}
                            icon={faBuilding}
                          />
                        </div>
                      </div>
                      <div className="row">
                        <Form className="px-1">
                          <span
                            className={"py-0 mb-2 mt-1 mx-0 input-group-text"}
                          >
                            Licencja sportowa
                          </span>
                          <Form.Check
                            inline
                            label="TAK"
                            name="sportLicense"
                            type={"radio"}
                            id={`inline-1`}
                            checked={team.sportLicense}
                            readOnly={disable}
                            onClick={() =>
                              setTeam({ ...team, sportLicense: true })
                            }
                          />
                          <Form.Check
                            inline
                            label="NIE"
                            name="sportLicense"
                            type={"radio"}
                            id={`inline-2`}
                            checked={!team.sportLicense}
                            readOnly={disable}
                            onClick={() =>
                              setTeam({ ...team, sportLicense: false })
                            }
                          />
                        </Form>
                      </div>
                      <div className="row mt-2">
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Email"
                            name="email"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.email}
                            icon={faAt}
                            required={true}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Nr. telefonu"
                            name="phone"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.phone}
                            icon={faPhoneAlt}
                            required={true}
                            onlyNumber={true}
                          />
                        </div>
                      </div>
                      <div className="row mt-2">
                        <div className="col-lg-6 px-0">
                          <MyDatePicker
                            label={"Data urodzenia"}
                            onChange={(val) =>
                              setTeam({ ...team, birthDate: val })
                            }
                            selected={team.birthDate}
                            disabled={disable}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Nr. prawa jazdy"
                            name="drivingLicense"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.drivingLicense}
                            required={true}
                          />
                        </div>
                      </div>
                      <div className="row mt-2">
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Imię i nazwisko (wypadek)"
                            name="emergencyPerson"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.emergencyPerson}
                            icon={faCarCrash}
                            required={true}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Nr. telefonu (wypadek)"
                            name="emergencyPhone"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.emergencyPhone}
                            icon={faCarCrash}
                            required={true}
                            onlyNumber={true}
                          />
                        </div>
                      </div>
                    </Card.Body>
                  </Card>
                  {myEvent?.carClassManual && (
                    <Card className="text-center">
                      <Card.Header className="bg-dark-green text-white pt-1 pb-0">
                        <h5>Klasa samochodu</h5>
                      </Card.Header>
                      <Card.Body>
                        <div className="row d-flex">
                          <div className="col-lg-12 px-1">
                            {manualCarClass !== undefined && (
                              <h5>{`Wybrana klasa: ${manualCarClass}`}</h5>
                            )}
                            <Selector
                              label={"Klasy"}
                              options={classesOptions}
                              handleChange={(value) => setManualCarClass(value)}
                              isValid={true}
                              skipDefault={true}
                            />
                          </div>
                        </div>
                      </Card.Body>
                    </Card>
                  )}
                </div>
                <div className="col-lg-6 pb-3 px-1">
                  <Card className="text-center">
                    <Card.Header className="bg-dark-green text-white pt-1 pb-0">
                      <h5>Pilot</h5>
                    </Card.Header>
                    <Card.Body>
                      <div className="row mt-2 ">
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Imie i nazwisko"
                            name="coDriver"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.coDriver}
                            icon={faUserClock}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Automobilklub"
                            name="coClub"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.coClub}
                            icon={faBuilding}
                          />
                        </div>
                      </div>
                      <div className="row">
                        <Form className="px-1">
                          <span
                            className={"py-0 mb-2 mt-1 mx-0 input-group-text"}
                          >
                            Licencja sportowa
                          </span>
                          <Form.Check
                            inline
                            label="TAK"
                            name="coSportLicense"
                            type={"radio"}
                            id={`inline-1`}
                            checked={team.coSportLicense}
                            readOnly={disable}
                            onClick={() =>
                              setTeam({ ...team, coSportLicense: true })
                            }
                          />
                          <Form.Check
                            inline
                            label="NIE"
                            name="coSportLicense"
                            type={"radio"}
                            id={`inline-2`}
                            checked={!team.coSportLicense}
                            readOnly={disable}
                            onClick={() =>
                              setTeam({ ...team, coSportLicense: false })
                            }
                          />
                        </Form>
                      </div>
                      <div className="row mt-2 ">
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Email"
                            name="coEmail"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.coEmail}
                            icon={faAt}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Nr. telefonu"
                            name="coPhone"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.coPhone}
                            icon={faPhoneAlt}
                            onlyNumber={true}
                          />
                        </div>
                      </div>
                      <div className="row mt-2 px-0">
                        <div className="col-lg-6 px-0">
                          <MyDatePicker
                            label={"Data urodzenia"}
                            onChange={(val) =>
                              setTeam({ ...team, coBirthDate: val })
                            }
                            selected={team.coBirthDate}
                            disabled={disable}
                          />
                        </div>
                        <div className="col-lg-6 px-1">
                          <InputLabeled
                            label="Nr. prawa jazdy"
                            name="coDrivingLicense"
                            handleChange={handleChange}
                            disabled={disable}
                            big={true}
                            value={team.coDrivingLicense}
                          />
                        </div>
                      </div>
                    </Card.Body>
                  </Card>
                  <Card className="text-center">
                    <Card.Header className="bg-dark-green text-white pt-1 pb-0">
                      {team.currentCar !== undefined &&
                      team.currentCar !== null ? (
                        <h5>{`Samochód:`}</h5>
                      ) : (
                        <h5>{`Dodaj samochód:`}</h5>
                      )}
                    </Card.Header>
                    <Card.Body>
                      <div className="d-flex justify-content-center pb-2">
                        {getCarLogo(team.currentCar?.brand, 35)}
                        {team.currentCar && (
                          <h5 className="px-2">{`${team.currentCar?.brand} ${team.currentCar?.model} ${team.currentCar?.licensePlate}`}</h5>
                        )}
                      </div>
                      {!disable && (
                        <>
                          <Selector
                            label={"Twoje auta"}
                            options={carsOption}
                            handleChange={(value) => {
                              setTeam({
                                ...team,
                                currentCar: team.cars.find(
                                  (x) => x.carId === value
                                ),
                              });
                            }}
                            isValid={true}
                            skipDefault={true}
                          />
                          <Button
                            className="m-1"
                            variant="primary"
                            onClick={() => {
                              fetchSaveTeam(team);
                              setAddCar(true);
                            }}
                          >
                            Dodaj samochód
                          </Button>
                          <Button
                            className="m-1"
                            variant="secondary"
                            onClick={() => {
                              fetchSaveTeam(team);
                              setAddCar(team.currentCar);
                            }}
                          >
                            Edytuj
                          </Button>
                        </>
                      )}
                      {disable && (
                        <Button
                          className="m-1"
                          variant="secondary"
                          onClick={() => setAddCar(team.currentCar)}
                        >
                          Dane samochodu
                        </Button>
                      )}
                    </Card.Body>
                  </Card>
                </div>
              </div>
            )}
            <div className="text-center my-0">
              <h6 style={{ whiteSpace: "pre-line" }}>{msg}</h6>
              {mode !== "teamPanel" && !disable && (
                <h6 className="font13" style={{ whiteSpace: "pre-line" }}>
                  Uzupełnij profil danymi osobowymi oraz dodaj auto, po
                  zapisaniu będziesz mógł zapisywać się na kolejne imprezy
                  jednym kliknięciem.
                </h6>
              )}
              {loading && (
                <div className="text-center">
                  <Spinner animation="border" variant="secondary" size="lg" />
                </div>
              )}
              {!disable && (
                <Button
                  className={"m-1"}
                  variant="success"
                  type="submit"
                  disabled={addingToEvent}
                >
                  {mode === "teamPanel" ? "Zapisz zmiany" : "Zapisz się"}
                </Button>
              )}
              {disable && (
                <div className="row">
                  <div className="col-xl-6 justify-content-center">
                    <p className="mb-0">Odbiór administracyjny</p>
                    <Button
                      className={"m-1"}
                      variant="success"
                      onClick={() =>
                        fetchTeamChecked(
                          myEvent?.eventId,
                          myEvent?.teamId,
                          true,
                          (data) => setOkModal(data)
                        )
                      }
                    >
                      OA pozytywne
                    </Button>
                    <Button
                      className={"m-1"}
                      variant="danger"
                      onClick={() =>
                        fetchTeamChecked(
                          myEvent?.eventId,
                          myEvent?.teamId,
                          false,
                          (data) => setOkModal(data)
                        )
                      }
                    >
                      OA negatywne
                    </Button>
                  </div>
                  <div className="col-xl-6 justify-content-center">
                    <p className="mb-0">Badanie kontrolne</p>
                    <Button
                      className={"m-1"}
                      variant="success"
                      onClick={() =>
                        fetchBkChecked(
                          myEvent?.eventId,
                          myEvent?.teamId,
                          true,
                          (data) => setOkModal(data)
                        )
                      }
                    >
                      BK pozytywne
                    </Button>
                    <Button
                      className={"m-1"}
                      variant="danger"
                      onClick={() =>
                        fetchBkChecked(
                          myEvent?.eventId,
                          myEvent?.teamId,
                          false,
                          (data) => setOkModal(data)
                        )
                      }
                    >
                      BK negatywne
                    </Button>
                  </div>
                </div>
              )}
              <Button
                className={"m-1"}
                variant="secondary"
                onClick={handleClose}
              >
                Zamknij okno
              </Button>
            </div>
          </form>
        </Modal.Body>
      </Modal>
      <CarPanelModal
        show={addCar}
        handleClose={() => {
          setAddCar(false);
          fetchTeam(team?.teamId);
        }}
        teamId={team?.teamId}
        carToEdit={addCar}
        mode={mode}
      />
      <OkModal
        show={okModal !== undefined}
        title={""}
        text={okModal ? "Akcja zakończona pomyślnie" : "Akcja nieudana"}
        handleAccept={() => setOkModal()}
      />
    </>
  );
};
