import React, { useState, useEffect } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import moment from "moment";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar, faEdit } from "@fortawesome/free-solid-svg-icons";
import {
  fetchDriverCount,
  fetchLogo,
  fetchStatement,
} from "../utils/fetchUtils";
import { Count } from "./Count";

export const EventCard = ({
  event,
  onJoin,
  onTeamList,
  onScore,
  onEdit,
  onStatement,
  mainAdmin,
}) => {
  const eventDeadlined =
    new Date().getTime() > new Date(event.signDeadline).getTime();
  const [statementCount, setStatementCount] = useState(0);
  const [driverCount, setDriverCount] = useState(0);
  const [logoDto, setLogoDto] = useState({});

  const signDeadlineCount = moment(event?.signDeadline).diff(
    new Date(),
    "days"
  );
  const eventCount = moment(event?.date)
    .startOf("day")
    .diff(moment().startOf("day"), "days");

  const getTxtCount = (count) => {
    return count === 0
      ? "DZISIAJ!"
      : count === 1
      ? "jutro!"
      : "za " + count + " dni";
  };

  useEffect(() => {
    if (event) {
      fetchStatement(event.eventId, (data) => setStatementCount(data.length));
      fetchDriverCount(event.eventId, (data) => setDriverCount(data));
      fetchLogo(event.eventId, (data) => setLogoDto(data));
    }
  }, []);

  return (
    <div className="col-lg-6 py-1 px-1 no-opacity-hover opacity-90a">
      <Card className="shadow-sm">
        <Card.Header className="bg-secondary-green text-white text-start fw-bold py-1">
          <div className="row px-1">
            <div className="col-11 px-0">{event?.name}</div>
            <div className="col-1 px-0 text-end">
              {onEdit !== undefined && mainAdmin && (
                <FontAwesomeIcon
                  className={"text-white fa-lg"}
                  icon={faEdit}
                  onClick={onEdit}
                  cursor={"pointer"}
                />
              )}
            </div>
          </div>
        </Card.Header>
        <Card.Body className="p-1">
          <div
            style={{ whiteSpace: "break-spaces" }}
            className="position-absolute end-0 mx-1 badge bg-primary-green text-wrap fst-italic"
          >
            {`${moment(event?.date).format("dddd, DD MMM YYYY, HH:mm")}
            ${eventCount >= 0 ? "(" + getTxtCount(eventCount) + ")" : ""}`}
          </div>
          <div className="container d-flex">
            <div
              className="col-lg-4 px-0 align-self-center"
              style={{
                width: "200px",
                maxHeight: "150px",
                display: "flex",
              }}
            >
              {logoDto?.file ? (
                <img
                  id={"eventImage" + event.eventId}
                  className="img-fluid rounded float-left"
                  style={{ objectFit: "contain" }}
                  src={"data:image/jpg;base64," + logoDto?.file}
                  alt="Logo"
                ></img>
              ) : (
                event.logoPath && (
                  <img
                    className="img-fluid rounded float-left"
                    style={{ objectFit: "contain" }}
                    src={event.logoPath}
                    alt="Logo"
                  ></img>
                )
              )}
            </div>
            <div className="col-lg-8 mt-3">
              {event.organizer !== undefined && event.organizer !== null && (
                <h6 className="m-3 ">{`Organizator: ${event.organizer}`}</h6>
              )}
              {new Date(event?.signDeadline) >= new Date() && (
                <>
                  <h6
                    style={{ whiteSpace: "break-spaces" }}
                    className="font13 fw-bold fst-italic mt-2"
                  >
                    {`Koniec zapisów ${getTxtCount(
                      signDeadlineCount
                    )}\n${moment(event?.signDeadline).format(
                      "dddd, DD MMM YYYY, HH:mm"
                    )}`}
                  </h6>
                </>
              )}
              {new Date(event?.signDeadline) < new Date() && (
                <p className="fw-bold fst-italic mt-2">{"Zapisy zamknięte"}</p>
              )}
              {event.joined && (
                <div className="d-inline-flex">
                  <FontAwesomeIcon
                    className={"text-success fa-lg"}
                    icon={faStar}
                  />
                  <h6>Jesteś zapisany</h6>
                </div>
              )}
            </div>
          </div>
        </Card.Body>
        <Card.Footer className="text-start py-0 px-1">
          <div className="row my-2 mx-0">
            <div className="col-6 px-0">
              {/* {new Date().getTime() <=
                new Date(event.signDeadline).getTime() && ( */}
              <Button
                className={"start-0 py-1 px-1 "}
                variant="success"
                onClick={onJoin}
              >
                {eventDeadlined
                  ? "Informacje"
                  : event.joined
                  ? "Moje zgłoszenie"
                  : "Info / Zgłoszenia"}
              </Button>
              {/* )} */}
            </div>
            <div className="col-6 px-0 d-flex justify-content-end">
              <Button
                className={"py-1 px-2 mx-1"}
                variant="dark"
                onClick={onTeamList}
              >
                Lista
              </Button>
              <Count
                count={driverCount}
                lPos="-3.5vh"
                bg="primary"
                title={"Liczba zapisanych"}
              />
              <Button
                className={"py-1 px-1 mx-1"}
                variant="warning"
                onClick={onStatement}
              >
                Komunikaty
              </Button>
              <Count count={statementCount} title={"Liczba komunikatów"} />
              <Button
                className={"py-1 px-1 mx-1"}
                variant="success"
                onClick={onScore}
              >
                Wyniki
              </Button>
            </div>
          </div>
        </Card.Footer>
      </Card>
    </div>
  );
};
