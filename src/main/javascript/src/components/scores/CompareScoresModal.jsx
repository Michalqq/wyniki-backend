import React, { useState, useEffect, useMemo } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

import { backendUrl, fetchGetCompareScores } from "../utils/fetchUtils";
import { Spinner } from "react-bootstrap";
import { closeOnBack, timeToString } from "../utils/utils";
import {
  CarDiv,
  LightScoreDiv,
  LightScoreDivPenalty,
  ScoreDiv,
  ScoreDivPenalty,
  TeamDiv,
} from "../common/Div";
import { NrBadge } from "../common/NrBadge";

export const CompareScoresModal = ({
  show,
  handleClose,
  eventId,
  markedNumbers,
  psOptions,
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

  const sumByTeamNumber = (teamNumber) => {
    return scores
      .filter((x) => x.teamNumber === teamNumber)
      .map((x) => x.score + x.penalty * 1000)
      .reduce((prev, next) => prev + next);
  };

  useEffect(() => {
    if (teams.length > 0 && scores.length > 0) {
      setLoading(false);
    }
  }, [teams, scores]);

  const getTeamDiv = (number) => {
    const eventTeam = teams.find((x) => x.number === number);

    if (!eventTeam) return <></>;

    return (
      <div className="d-inline-flex">
        <div className="align-self-center pe-2">
          <NrBadge value={number}></NrBadge>
        </div>
        <TeamDiv
          team={{
            driver: eventTeam.driver,
            coDriver: eventTeam.coDriver,
            club: eventTeam.club,
            coClub: eventTeam.coClub,
            teamName: eventTeam.teamName,
          }}
        ></TeamDiv>
      </div>
    );
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
              <div
                className="row pt-0 my-px-3"
                style={{ backgroundColor: "lightgray" }}
              >
                {markedNumbers.map((number) => {
                  const eventTeam = teams.find((x) => x.number === number);
                  const car = eventTeam?.car;
                  return (
                    <div
                      className="col p-3 ps-4 d-inline-flex"
                      style={{ scale: "1.1" }}
                    >
                      {eventTeam && (
                        <CarDiv
                          line1={(car?.brand || "") + " " + (car?.model || "")}
                          line2={eventTeam.carClass?.name}
                          carBrand={car?.brand}
                          driveType={car?.driveTypeEnum}
                        ></CarDiv>
                      )}
                    </div>
                  );
                })}
              </div>
              <div className="row pt-1 px-3">
                <table className="table table-stripe">
                  <thead className="thead-dark">
                    <tr>
                      <th style={{ width: "10%" }}>PS</th>
                      <th
                        style={{ width: "38%" }}
                        className="text-end align-middle"
                      >
                        {getTeamDiv(markedNumbers[0])}
                      </th>
                      <th className="text-start align-middle">
                        {getTeamDiv(markedNumbers[1])}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {psOptions.map((psOption, i) => {
                      const bg = i % 2 === 0 ? "bg-light-gray " : "";
                      let scoresLine = scores
                        .filter((x) => x.stageId === Number(psOption.value))
                        .sort((a, b) => (a.teamNumber < b.teamNumber ? -1 : 1));

                      let totalScore1 =
                        scoresLine[0]?.score + scoresLine[0]?.penalty * 1000;
                      let totalScore2 =
                        scoresLine[1]?.score + scoresLine[1]?.penalty * 1000;

                      let sign =
                        totalScore1 === totalScore2
                          ? ""
                          : totalScore1 > totalScore2
                          ? "+ "
                          : "- ";

                      return (
                        <>
                          <tr className={bg}>
                            <th className="p-1">
                              <div style={{ marginRight: "auto" }}>
                                <ScoreDivPenalty
                                  line1={"PS " + (i + 1)}
                                  line2={"0"}
                                />
                              </div>
                            </th>
                            {scoresLine.length == 2 && (
                              <>
                                <th
                                  className={
                                    "text-end align-middle " +
                                    (totalScore1 < totalScore2
                                      ? "text-success"
                                      : "fw-normal")
                                  }
                                >
                                  <LightScoreDiv
                                    line1={timeToString(Math.abs(totalScore1))}
                                    line3={
                                      totalScore1 > totalScore2
                                        ? "+" +
                                          timeToString(
                                            totalScore1 - totalScore2
                                          )
                                        : ""
                                    }
                                  />
                                </th>
                                <th
                                  className={
                                    "align-middle " +
                                    (totalScore1 > totalScore2
                                      ? "text-success"
                                      : "fw-normal")
                                  }
                                >
                                  <LightScoreDiv
                                    line1={timeToString(Math.abs(totalScore2))}
                                    line3={
                                      totalScore1 < totalScore2
                                        ? "+" +
                                          timeToString(
                                            totalScore2 - totalScore1
                                          )
                                        : ""
                                    }
                                  />
                                </th>
                              </>
                            )}
                          </tr>
                        </>
                      );
                    })}
                  </tbody>

                  {markedNumbers.length == 2 && scores.length > 0 && (
                    <tfoot>
                      <tr>
                        <th className="p-1">
                          <ScoreDivPenalty line1={"Suma"} line2={"0"} />
                        </th>
                        <th
                          className={
                            "text-end fw-normal " +
                            (sumByTeamNumber(markedNumbers[0]) <
                            sumByTeamNumber(markedNumbers[1])
                              ? "text-success"
                              : "")
                          }
                        >
                          <ScoreDiv
                            line1={timeToString(
                              sumByTeamNumber(markedNumbers[0])
                            )}
                            line2={
                              sumByTeamNumber(markedNumbers[0]) >
                              sumByTeamNumber(markedNumbers[1])
                                ? "+" +
                                  timeToString(
                                    sumByTeamNumber(markedNumbers[0]) -
                                      sumByTeamNumber(markedNumbers[1])
                                  )
                                : ""
                            }
                          />
                        </th>
                        <th
                          className={
                            "fw-normal " +
                            (sumByTeamNumber(markedNumbers[0]) >
                            sumByTeamNumber(markedNumbers[1])
                              ? "text-success"
                              : "")
                          }
                        >
                          <ScoreDiv
                            line1={timeToString(
                              sumByTeamNumber(markedNumbers[1])
                            )}
                            line2={
                              sumByTeamNumber(markedNumbers[0]) <
                              sumByTeamNumber(markedNumbers[1])
                                ? "+" +
                                  timeToString(
                                    sumByTeamNumber(markedNumbers[1]) -
                                      sumByTeamNumber(markedNumbers[0])
                                  )
                                : ""
                            }
                          />
                        </th>
                      </tr>
                    </tfoot>
                  )}
                </table>
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
