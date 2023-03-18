import React, { useState, useEffect, useMemo } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

import {
  backendUrl,
  checkReferee,
  fetchGetCompareScores,
  fetchStatement,
} from "../utils/fetchUtils";
import moment from "moment";
import { download, openFile } from "../utils/fileUtils";
import authHeader from "../../service/auth-header";
import { Spinner } from "react-bootstrap";
import { closeOnBack, timeToString } from "../utils/utils";
import { CarDiv, ScoreDivPenalty, TeamDiv } from "../common/Div";
import { NrBadge } from "../common/NrBadge";

export const CompareScoresModal = ({
  show,
  handleClose,
  eventId,
  markedNumbers,
}) => {
  const [loading, setLoading] = useState(false);
  const [scores, setScores] = useState([]);
  const [teams, setTeams] = useState([]);

  useEffect(() => {
    if (!show) return;

    closeOnBack(handleClose);
    if (markedNumbers.length === 2) {
      fetchTeams();
      fetchGetCompareScores(eventId, markedNumbers, (data) => {
        setScores(data);
        setLoading(false);
      });
    }
  }, [show]);

  const fetchTeams = () => {
    if (eventId === undefined) return;
    axios
      .get(`${backendUrl()}/event/getBasicTeams?eventId=${eventId}`)
      .then((res) => {
        setTeams(res.data);
      });
  };

  return (
    <>
      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="xl"
      >
        <Modal.Header closeButton className="bg-dark-green text-white">
          <Modal.Title className="text-white">{`Porównanie wyników`}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="my-px-3">
          {loading ? (
            <div className="text-center">
              <Spinner animation="border" variant="secondary" size="lg" />
            </div>
          ) : markedNumbers.length !== 2 ? (
            <h6 className="text-center pt-3">
              {markedNumbers.length < 2
                ? "Zaznacz 2 zawodników klikając na pomarańczowy numer startowy!"
                : `Porównanie możliwe tylko dla 2 zawodników - zaznaczyłeś ${markedNumbers.length}`}
            </h6>
          ) : (
            <div>
              <div className="row pt-0 my-px-3 bg-light">
                {markedNumbers.map((number) => {
                  const eventTeam = teams.find((x) => x.number === number);
                  const team = eventTeam?.team;
                  return (
                    <div className="col p-1 ps-4" style={{ scale: "1.1" }}>
                      {team && (
                        <div className="d-flex">
                          <div className="align-self-center pe-2">
                            <NrBadge value={number}></NrBadge>
                          </div>
                          <TeamDiv team={team}></TeamDiv>
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
              <div
                className="row pt-0 my-px-3"
                style={{ backgroundColor: "lightgray" }}
              >
                {markedNumbers.map((number) => {
                  const eventTeam = teams.find((x) => x.number === number);
                  const team = eventTeam?.team;
                  const car = eventTeam?.car;
                  return (
                    <div className="col p-3 ps-4" style={{ scale: "1.1" }}>
                      {team && (
                        <CarDiv
                          line1={(car?.brand || "") + " " + (car?.model || "")}
                          line2={eventTeam.carClass.name}
                          carBrand={car?.brand}
                          driveType={car?.driveTypeEnum}
                        ></CarDiv>
                      )}
                    </div>
                  );
                })}
              </div>
              <h6 className="pt-2 text-center">
                Wersja testowa - czasy nie zawierają kar:
              </h6>
              <div className="row pt-1">
                {markedNumbers
                  .sort((a, b) => a - b)
                  .map((number, i) => {
                    const isLeft = i === 0;
                    const divClassName = isLeft
                      ? "col px-0 text-right"
                      : "col px-0";
                    return (
                      <>
                        <div className={divClassName}>
                          {scores
                            .filter((x) => x.teamNumber === number)
                            .sort((a, b) => a.stageId - b.stageId)
                            .map((score, i) => {
                              const bg = i % 2 === 0 ? "bg-light-gray " : "";
                              return (
                                <div className="">
                                  <div className={bg + "p-1 px-3 d-flex "}>
                                    {isLeft && (
                                      <div style={{ marginRight: "auto" }}>
                                        <ScoreDivPenalty
                                          line1={"PS " + (i + 1)}
                                          line2={"0"}
                                        />
                                      </div>
                                    )}
                                    <ScoreDivPenalty
                                      line1={timeToString(score.score)}
                                      line2={"0"}
                                    />
                                  </div>
                                </div>
                              );
                            })}
                        </div>
                      </>
                    );
                  })}
              </div>
            </div>
          )}
        </Modal.Body>
        <Modal.Footer className={"justify-content-center"}>
          <div className="d-flex">
            <Button className={"m-1"} variant="secondary" onClick={handleClose}>
              Wyjdź
            </Button>
          </div>
        </Modal.Footer>
      </Modal>
    </>
  );
};
