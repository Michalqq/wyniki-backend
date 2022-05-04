import React, { useState, useEffect } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import moment from "moment";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar, faEdit } from "@fortawesome/free-solid-svg-icons";
import Badge from "react-bootstrap/Badge";
import { fetchStatement } from "../utils/fetchUtils";

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

  useEffect(() => {
    if (event)
      fetchStatement(event.eventId, (data) => setStatementCount(data.length));
  }, []);

  return (
    <div className="col-lg-6 pb-3 px-1 u-box-shadow">
      <Card className="">
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
          <div className="position-absolute end-0 mx-3 text-end badge bg-primary-green text-wrap fst-italic">
            {moment(event?.date).format("dddd, DD MMM YYYY, HH:mm")}
          </div>
          <div className="container d-flex">
            <div
              className="col-lg-2 px-0 align-self-center"
              style={{ width: "110px" }}
            >
              {event.logoPathFile ? (
                <img
                  id={"eventImage" + event.eventId}
                  className="img-fluid rounded float-left"
                  src={"data:image/jpg;base64," + event.logoPathFile}
                  alt="Logo"
                ></img>
              ) : event.logoPath !== undefined && event.logoPath !== null ? (
                <img
                  className="img-fluid rounded float-left"
                  src={event.logoPath}
                  alt="Logo"
                ></img>
              ) : (
                <img
                  src="/akbpLogo.png"
                  className="img-fluid rounded float-left"
                  alt="..."
                />
              )}
            </div>
            <div className="col-lg-10 mt-3">
              {event.organizer !== undefined && event.organizer !== null && (
                <p className="m-3">{`Organizator: ${event.organizer}`}</p>
              )}
              <p className="fw-bold fst-italic mt-2">
                {`Koniec zapisów:  ${moment(event?.signDeadline).format(
                  "dddd, DD MMM YYYY, HH:mm"
                )}`}
              </p>
              {new Date(event?.signDeadline) < new Date() &&
                new Date(event?.date) > new Date() && (
                  <p className="">{"Zapisy zamknięte"}</p>
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
              <Button
                className={"py-1 px-1 mx-1"}
                variant="warning"
                onClick={onStatement}
              >
                Komunikaty
              </Button>
              <div
                className="position-relative"
                style={{ left: "-3vh", top: "-10px" }}
              >
                {statementCount > 0 && (
                  <Badge className="position-absolute" pill bg="secondary">
                    {statementCount}
                  </Badge>
                )}
              </div>

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
