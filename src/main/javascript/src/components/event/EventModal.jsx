import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import Tabs from "react-bootstrap/Tabs";
import Tab from "react-bootstrap/Tab";
import {
  backendUrl,
  fetchLogo,
  fetchRemoveFromEvent,
} from "../utils/fetchUtils";
import { useNavigate } from "react-router-dom";
import Spinner from "react-bootstrap/Spinner";
import authHeader from "../../service/auth-header";
import moment from "moment";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from "@fortawesome/free-solid-svg-icons";
import { QuickJoinPanel } from "../join/QuickJoinPanel";
import { TeamModal } from "../team/TeamModal";
import { download } from "../utils/fileUtils";
import { closeOnBack } from "../utils/utils";

export const EventModal = ({ show, handleClose, event }) => {
  const navigate = useNavigate();

  const [team, setTeam] = useState(undefined);
  const [file, setFile] = useState();
  const [uploading, setUploading] = useState(false);
  const [fileMsg, setFileMsg] = useState();
  const [notJoined, setNotJoined] = useState(false);
  const [myEvent, setMyEvent] = useState();
  const [quickJoin, setQuickJoin] = useState();
  const [activeTab, setActiveTab] = useState(1);
  const [fillTeam, setFillTeam] = useState();
  const [logoDto, setLogoDto] = useState({});

  const loggedUser = sessionStorage.getItem("username") !== null;

  useEffect(() => {
    if (!show) {
      setLogoDto({});
      return;
    }
    closeOnBack(handleClose);

    fetchGetTeam();
    setUploading(false);
    setFileMsg();
    setNotJoined(false);
    setMyEvent(event);
    fetchLogo(event.eventId, (data) => setLogoDto(data));
  }, [show]);

  const signDeadlined = event
    ? new Date().getTime() > new Date(event.signDeadline).getTime()
    : false;

  const fetchGetTeam = () => {
    axios
      .get(`${backendUrl()}/team/getTeam`, {
        headers: authHeader(),
      })
      .then((res) => {
        setNotJoined(res.data === "");
        if (res.data !== "") setTeam(res.data);
      });
  };

  useEffect(() => {
    if (event?.eventId !== undefined && team?.teamId !== undefined)
      axios
        .get(
          `${backendUrl()}/event/getEntryFeeFile?eventId=${
            event.eventId
          }&teamId=${team.teamId}`
        )
        .then((res) => {
          if (res.status === 204) setNotJoined(true);
          else setFileMsg("Mamy już Twoje potwierdzenie");
        })
        .catch((error) => {
          setFileMsg();
        });
  }, [team]);

  const doRemoveFromEvent = () => {
    fetchRemoveFromEvent(event.eventId, team?.teamId, () => {
      setMyEvent({ ...myEvent, joined: false });
      setNotJoined(true);
    });
  };

  const fetchUpload = (eventId, teamId) => {
    if (file.size > 2 * 1024 * 1024) {
      setFileMsg("Plik jest zbyt duży (maksymalnie 2mb)");
      return;
    }
    setUploading(true);

    const formData = new FormData();
    formData.append("file", file);
    axios
      .post(
        `${backendUrl()}/event/addEntryFeeFile?eventId=${eventId}&teamId=${teamId}`,
        formData,
        {
          headers: authHeader(),
        }
      )
      .then((res) => {
        setUploading(false);
        res.data
          ? setFileMsg("Plik został dodany")
          : setFileMsg("Plik NIE został dodany - błąd");
      });
  };

  const downloadFile = (e, file) => {
    e.preventDefault();
    download(`${backendUrl()}/event/getEventFile?id=${file.id}`, file.fileName);
  };

  return (
    <div>
      <Modal
        style={{ zIndex: quickJoin || fillTeam ? 1000 : 1055 }}
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="xl"
      >
        <Modal.Header className="bg-dark-green text-white" closeButton>
          <Modal.Title className="w-100 row">
            <div className="col-lg-8">{`Informacje o wydarzeniu`}</div>
            {myEvent?.joined && (
              <div className="col-lg-4">
                <FontAwesomeIcon
                  className={"text-success fa-lg"}
                  icon={faStar}
                />
                Jesteś zapisany
              </div>
            )}
          </Modal.Title>
        </Modal.Header>

        <Tabs
          activeKey={activeTab}
          onSelect={(key) => setActiveTab(key)}
          className="m-0 fw-bold text-dark"
        >
          <Tab eventKey={1} title="Informacje">
            <div className="row text-center px-2">
              <div className="col-lg-7 px-0">
                <h3>{event?.name || ""}</h3>
                <h6>{`Data wydarzenia:  `}</h6>
                <h6 className="fw-bold">
                  {moment(event?.date).format(" dddd, DD MMM YYYY, HH:mm")}
                </h6>
                <p className="fw-bold fst-italic m-2">
                  {`Koniec zapisów:  ${moment(event?.signDeadline).format(
                    "dddd, DD MMM YYYY, HH:mm"
                  )}`}
                </p>
                <h6 style={{ whiteSpace: "pre-line" }} className="ps-2 py-2">
                  {event?.headDescription || ""}
                </h6>
              </div>
              <div className="col-lg-5 py-1  align-items-center d-flex">
                {logoDto?.file ? (
                  <img
                    id={"eventImage" + event?.eventId}
                    style={{
                      maxHeight: "450px",
                    }}
                    className="pt-2 img-fluid rounded float-left"
                    src={"data:image/jpg;base64," + logoDto.file}
                    alt="Logo"
                  ></img>
                ) : (
                  event?.logoPath !== undefined &&
                  event?.logoPath !== null && (
                    <div className="">
                      <img
                        style={{
                          maxHeight: "450px",
                          position: "relative",
                          top: "30%",
                        }}
                        className="img-fluid rounded float-left"
                        src={event.logoPath}
                        alt="Logo"
                      ></img>
                    </div>
                  )
                )}
              </div>
              <div className="col-lg-12 py-2">
                <p
                  style={{ whiteSpace: "pre-line" }}
                  className="font14 m-1 py-2"
                >
                  {event?.description || ""}
                </p>
                <p style={{ whiteSpace: "pre-line" }} className="font12 m-1">
                  {event?.footerDescription || ""}
                </p>
              </div>
            </div>
            <Card className="text-center">
              <Card.Header className="bg-dark-green text-white">
                Do pobrania:
              </Card.Header>
              <Card.Body>
                <div className="row ">
                  <div className="col-lg-12 px-0">
                    {event?.eventPaths?.map((path, index) => (
                      <h6 key={index} className="my-1">
                        <a
                          target="_blank"
                          rel="noopener noreferrer"
                          href={
                            path.path.startsWith("http")
                              ? path.path
                              : "http://" + path.path
                          }
                        >
                          {path.description}
                        </a>
                      </h6>
                    ))}
                    {event?.eventFiles?.map((file, index) => (
                      <h6 key={index} className="my-1">
                        <a href={"www"} onClick={(e) => downloadFile(e, file)}>
                          {file.description}
                        </a>
                      </h6>
                    ))}
                  </div>
                </div>
              </Card.Body>
            </Card>
          </Tab>
          {!loggedUser ? (
            <></>
          ) : (
            <Tab eventKey={2} title="Wpisowe">
              <div className="row mx-2 justify-content-center">
                <div className="col-lg-6 pb-3 px-1">
                  <h5 className="text-center py-4">
                    Tutaj możesz dodać plik z potwierdzeniem wpisowego,
                    przyspieszy to proces odbioru administracyjnego
                  </h5>
                  <Card className="text-center ">
                    <Card.Header className="bg-dark-green text-white">
                      Wpisowe
                    </Card.Header>
                    <Card.Body>
                      {notJoined ? (
                        <p>
                          Zapisz się a następnie będziesz miał możliwość
                          przesłania pliku
                        </p>
                      ) : (
                        <p>Dodaj plik z potwierdzeniem opłacenia wpisowego</p>
                      )}
                      <input
                        disabled={notJoined}
                        type="file"
                        name="file"
                        accept="application/pdf,application/vnd.ms-excel"
                        onChange={(e) => setFile(e.target.files[0])}
                      />
                      {fileMsg && <p className="fw-bold m-2">{fileMsg}</p>}
                      <div>
                        {uploading ? (
                          <Spinner animation="border" variant="secondary" />
                        ) : (
                          <Button
                            className={"mt-2 mb-0"}
                            variant="secondary"
                            onClick={() =>
                              fetchUpload(event.eventId, team.teamId)
                            }
                            disabled={file === undefined}
                          >
                            Wyślij plik
                          </Button>
                        )}
                      </div>
                    </Card.Body>
                  </Card>
                </div>
              </div>
            </Tab>
          )}
        </Tabs>
        <Modal.Footer>
          <div className="row justify-content-center">
            <div className="col-xl-12 d-flex">
              <div className="">
                <Button
                  className={"m-1"}
                  variant="success"
                  form="teamForm"
                  onClick={() => {
                    loggedUser
                      ? setFillTeam(true)
                      : navigate(`login?${event.eventId}`);
                  }}
                  disabled={signDeadlined && notJoined}
                >
                  {notJoined ? "Zapisz się" : "Edytuj dane"}
                </Button>
              </div>
              <div className="">
                {/* <Button
                className={"m-1"}
                variant="success"
                onClick={() => {
                  loggedUser ? setFillTeam(true) : navigate(`login?${event.eventId}`);
                }}
              >
                {myEvent?.joined ? "Ok" : "Zapisz (po zalogowaniu)"}
              </Button> */}
                <Button
                  className={"m-1"}
                  variant="secondary"
                  onClick={handleClose}
                >
                  Zamknij okno
                </Button>
                {myEvent?.joined && (
                  <Button
                    className={"mx-3"}
                    variant="danger"
                    onClick={() => doRemoveFromEvent()}
                    disabled={signDeadlined}
                  >
                    Wypisz się
                  </Button>
                )}
              </div>
            </div>
          </div>
        </Modal.Footer>
      </Modal>
      <QuickJoinPanel
        show={quickJoin}
        handleClose={() => setQuickJoin()}
        eventId={event?.eventId}
      />
      <TeamModal
        show={fillTeam}
        handleClose={() => setFillTeam()}
        handleOk={() => handleClose()}
        myEvent={event}
      />
    </div>
  );
};
