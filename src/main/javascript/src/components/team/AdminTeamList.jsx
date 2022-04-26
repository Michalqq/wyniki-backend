import React, { useState, useEffect } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Spinner from "react-bootstrap/Spinner";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";
import {
  backendUrl,
  fetchConfirmEntryFee,
  fetchRemoveFromEvent,
  fetchSaveTeam,
} from "../utils/fetchUtils";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faClipboard,
  faCoins,
  faDollarSign,
  faDownload,
  faExclamation,
  faTimesCircle,
  faUserEdit,
} from "@fortawesome/free-solid-svg-icons";
import { OkCancelModal } from "../common/Modal";
import authHeader from "../../service/auth-header";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import Card from "react-bootstrap/Card";
import { Selector } from "../common/Selector";
import { BasicTeamDataForm } from "./BasicTeamDataForm";
import { TeamModal } from "./TeamModal";
import { QuickJoinPanel } from "../join/QuickJoinPanel";
import { download } from "../utils/fileUtils";

export const AdminTeamList = ({
  show,
  handleClose,
  eventId,
  eventName,
  started,
}) => {
  const [teams, setTeams] = useState([]);
  const [startEvent, setStartEvent] = useState(false);
  const [loading, setLoading] = useState(true);
  const [referee, setReferee] = useState(false);
  const [refreshSelect, setRefreshSelect] = useState(false);
  const [teamToRemove, setTeamToRemove] = useState({
    driver: null,
    teamId: null,
  });
  const [teamToEntryFee, setTeamToEntryFee] = useState({
    driver: null,
    teamId: null,
  });
  const [msg, setMsg] = useState();
  const [eventClasses, setEventClasses] = useState([]);
  const [teamToEdit, setTeamToEdit] = useState();
  const [teamToPreview, setTeamToPreview] = useState();
  const [quickJoin, setQuickJoin] = useState();
  const [doSort, setDoSort] = useState(false);

  useEffect(() => {
    if (show) {
      fetchReferee();
      setLoading(true);
      setTeams([]);
      fetchEvent();
    }
    fetchTeams();
  }, [show]);

  const eraseTeamToRemove = () => {
    setTeamToRemove({
      driver: null,
      teamId: null,
    });
  };

  const eraseTeamToEntryFee = () => {
    setTeamToEntryFee({
      driver: null,
      teamId: null,
    });
  };

  const getDriverAndTeamId = (team) => {
    return {
      teamId: team.teamId,
      driver: team.driver + " / " + team.coDriver,
    };
  };

  const fetchTeams = () => {
    if (eventId === undefined) return;
    axios
      .get(`${backendUrl()}/event/getTeams?eventId=${eventId}`)
      .then((res) => {
        setTeams(res.data.sort((a, b) => a.order - b.order));
        setLoading(false);
      });
  };

  const fetchEvent = () => {
    axios
      .get(`${backendUrl()}/event/getEvent?eventId=${eventId}`)
      .then((res) => {
        setEventClasses(
          res.data.eventClasses.map((x) => {
            return { ...x, value: x.carClassId, label: x.carClass.name };
          })
        );
      });
  };

  const fetchEntryFeeFile = (teamId, teamName) => {
    download(
      `${backendUrl()}/event/getEntryFeeFile?eventId=${eventId}&teamId=${teamId}`,
      "potwierdzenie_wplaty_" + teamName + ".pdf"
    );
  };

  const fetchOaDocuments = () => {
    download(
      `${backendUrl()}/file/getEventTeamsData?eventId=${eventId}`,
      "dokumenty_oa_" + eventName + ".pdf"
    );
  };

  const fetchBkDocuments = () => {
    download(
      `${backendUrl()}/file/getBkFiles?eventId=${eventId}`,
      "dokumenty_bk_" + eventName + ".pdf"
    );
  };
  const fetchReferee = () => {
    axios
      .get(`${backendUrl()}/event/checkReferee?eventId=${eventId}`, {
        headers: authHeader(),
      })
      .then((res) => {
        setReferee(res.data);
      });
  };

  const fetchStartEvent = () => {
    axios
      .post(`${backendUrl()}/event/startEvent?eventId=${eventId}`, {
        headers: authHeader(),
      })
      .then(() => {});
  };

  const sortByClass = () => {
    setRefreshSelect(true);
    axios
      .post(`${backendUrl()}/event/sortByClass`, teams, {
        headers: authHeader(),
      })
      .then((res) => {
        setTeams(res.data);
        setRefreshSelect(false);
      });
  };

  const saveNumbersAndClasses = () => {
    axios
      .post(
        `${backendUrl()}/event/saveNumbersAndClasses?eventId=${eventId}`,
        teams,
        {
          headers: authHeader(),
        }
      )
      .then((res) => {
        if (res.data) setMsg("Kolejność i klasy zostały zapisane");
        else setMsg("Coś poszło nie tak");

        setTimeout(() => setMsg(), 5000);
      });
  };

  const handleOnDragEnd = (result) => {
    if (!result.destination) return;

    const items = Array.from(teams);
    const [reorderedItem] = items.splice(result.source.index, 1);
    items.splice(result.destination.index, 0, reorderedItem);

    let order = 1;
    items.map((x) => (x.order = order++));

    setTeams(getOrdered(items));
    setRefreshSelect(false);
  };

  const changeCarClass = (id, newCarClassId) => {
    const index = teams.findIndex((x) => x.id === id);

    if (teams[index].carClassId === newCarClassId) return;
    teams[index].carClassId = newCarClassId;
    setTeams(teams);
  };

  const saveTeam = (team) => {
    setLoading(true);
    fetchSaveTeam(team, () => {
      fetchTeams();
      setTeamToEdit();
    });
  };

  const numberChanged = (value, item) => {
    setSortedTeam(item, value !== "" ? Number(value) : " ");
  };

  const checkRepeated = (value, item) => {
    const numValue = Number(value);
    if (
      teams.find(
        (x) => x.id !== item.id && x.forcedNumber && x.number === numValue
      )
    )
      setSortedTeam(item, "");
    // else setSortedTeam(item, Number(value));
  };

  const setSortedTeam = (item, numValue) => {
    const tempTeams = teams.filter((x) => x.id !== item.id);
    item.number = numValue;
    item.forcedNumber = numValue !== "";

    tempTeams.push(item);
    tempTeams.sort((a, b) => a.order - b.order);

    setTeams(getOrdered(tempTeams));
  };

  const getOrdered = (tempTeams) => {
    const newTeams = [];
    const forcedNumbers = teams
      .filter((x) => x.forcedNumber)
      .map((x) => x.number);
    let index = 1;

    for (let i in tempTeams) {
      let item = tempTeams[i];
      if (item.forcedNumber) {
        newTeams.push(item);
        continue;
      }
      if (!item.forcedNumber && !forcedNumbers.includes(index))
        item.number = index++;
      else {
        while (forcedNumbers.includes(index)) index++;
        item.number = index++;
      }
      newTeams.push(item);
    }
    return newTeams;
  };

  return (
    <Modal
      style={{ zIndex: teamToEdit || teamToPreview || quickJoin ? 1000 : 1055 }}
      show={show}
      onHide={handleClose}
      backdrop="static"
      keyboard={false}
      size="xl"
    >
      <Modal.Header className="bg-dark text-white" closeButton>
        <Modal.Title>Lista zapisanych</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {loading && (
          <div className="text-center">
            <Spinner animation="border" variant="secondary" size="lg" />
          </div>
        )}
        {!loading && teams.length === 0 && (
          <h1 className="text-center">Brak zgłoszeń</h1>
        )}
        {!loading && teams?.length > 0 && (
          <>
            <table>
              <th className="">
                <td style={{ width: "70px" }}>Edycja</td>
                <td style={{ width: "60px" }}>Nr</td>
                <td style={{ width: "350px" }}>Kierowca</td>
                <td style={{ width: "350px" }}>Samochód</td>
                <td style={{ width: "100px" }}>Klasa</td>
                <td style={{ width: "100px" }}>Zapłacone</td>
                <td style={{ width: "110px" }}>Potwierdź wpisowe</td>
                <td style={{ width: "100px" }}>Plik</td>
                <td style={{ width: "80px" }}>OA</td>
                <td style={{ width: "80px" }}>Usuń załogę</td>
              </th>
            </table>
            <div style={{ overflowX: "scroll" }}>
              <DragDropContext
                onDragStart={() => setRefreshSelect(true)}
                onDragEnd={handleOnDragEnd}
              >
                <Droppable droppableId="selectedCases">
                  {(provided) => (
                    <ol
                      className="selectedCases fw-bold"
                      style={{ minWidth: "600px" }}
                      {...provided.droppableProps}
                      ref={provided.innerRef}
                    >
                      {teams?.map((item, index) => {
                        return (
                          <Draggable
                            key={item.order}
                            draggableId={item.order.toString()}
                            index={index}
                            draggable={true}
                          >
                            {(provided) => (
                              <li
                                ref={provided.innerRef}
                                {...provided.draggableProps}
                                {...provided.dragHandleProps}
                              >
                                <Card className="my-1">
                                  <Card.Body className="p-0">
                                    <table className="m-0">
                                      <th className="d-table-row fw-normal align-middle">
                                        <td
                                          className={
                                            item.teamChecked &&
                                            item.entryFeePaid
                                              ? "bg-success"
                                              : ""
                                          }
                                          style={{ width: "20px" }}
                                        >
                                          <FontAwesomeIcon
                                            icon={faUserEdit}
                                            onClick={() =>
                                              setTeamToEdit(item.team)
                                            }
                                            title={"Edytuj dane"}
                                            cursor={"pointer"}
                                          />
                                        </td>
                                        <td style={{ width: "55px" }}>
                                          {/* <NrBadge value={item.number}></NrBadge> */}
                                          <input
                                            style={{
                                              width: "90%",
                                              backgroundColor: "#ffad4f",
                                              fontWeight: item.forcedNumber
                                                ? "700"
                                                : "100",
                                              borderRadius: "20px",
                                              textAlign: "center",
                                            }}
                                            type="number"
                                            value={item.number}
                                            onChange={(e) =>
                                              numberChanged(
                                                e.target.value,
                                                item
                                              )
                                            }
                                            onBlur={(e) =>
                                              checkRepeated(
                                                e.target.value,
                                                item
                                              )
                                            }
                                          />
                                        </td>
                                        <td style={{ width: "270px" }}>
                                          {item.team.driver +
                                            (item.team?.coDriver === null ||
                                            item.team.coDriver === ""
                                              ? ""
                                              : " / " + item.team?.coDriver)}
                                        </td>
                                        <td style={{ width: "270px" }}>
                                          {(item.team.currentCar?.brand || "") +
                                            " " +
                                            (item.team.currentCar?.model || "")}
                                        </td>
                                        <td
                                          style={{
                                            width: "100px",
                                            display: "flex",
                                          }}
                                        >
                                          {item.carClassId}
                                          {!refreshSelect && (
                                            <Selector
                                              className={"m-0 p-0"}
                                              options={eventClasses}
                                              handleChange={(value) => {
                                                changeCarClass(item.id, value);
                                              }}
                                              isValid={true}
                                              value={item.carClassId}
                                            />
                                          )}
                                          {refreshSelect && (
                                            <Selector
                                              className={"m-0 p-0"}
                                              options={eventClasses}
                                              handleChange={(value) => {
                                                changeCarClass(item.id, value);
                                              }}
                                              isValid={true}
                                              skipDefault={true}
                                            />
                                          )}
                                        </td>
                                        <td
                                          style={{
                                            width: "90px",
                                            textAlign: "center",
                                          }}
                                        >
                                          {item.entryFeePaid ? (
                                            <FontAwesomeIcon
                                              className={"text-success"}
                                              icon={faDollarSign}
                                            />
                                          ) : (
                                            <FontAwesomeIcon
                                              className={"text-danger"}
                                              icon={faExclamation}
                                            />
                                          )}
                                        </td>
                                        <td style={{ width: "90px" }}>
                                          <FontAwesomeIcon
                                            icon={faCoins}
                                            onClick={() =>
                                              setTeamToEntryFee(
                                                getDriverAndTeamId(item.team)
                                              )
                                            }
                                            title={"Potwierdź wpisowe"}
                                            cursor={"pointer"}
                                          />
                                        </td>
                                        <td style={{ width: "90px" }}>
                                          {item.entryFeeFile !== null ? (
                                            <FontAwesomeIcon
                                              icon={faDownload}
                                              onClick={() =>
                                                fetchEntryFeeFile(
                                                  item.team.teamId,
                                                  item.team.driver
                                                )
                                              }
                                              title={"Pobierz plik"}
                                              cursor={"pointer"}
                                            />
                                          ) : (
                                            <></>
                                          )}
                                        </td>
                                        <td style={{ width: "80px" }}>
                                          {item.teamChecked ? (
                                            <FontAwesomeIcon
                                              className={"text-success"}
                                              icon={faClipboard}
                                              onClick={() =>
                                                setTeamToPreview(
                                                  item.team.teamId
                                                )
                                              }
                                              title={"Podgląd danych"}
                                              cursor={"pointer"}
                                            />
                                          ) : (
                                            <FontAwesomeIcon
                                              icon={faClipboard}
                                              onClick={() =>
                                                setTeamToPreview(
                                                  item.team.teamId
                                                )
                                              }
                                              title={"Podgląd danych"}
                                              cursor={"pointer"}
                                            />
                                          )}
                                        </td>
                                        <td
                                          style={{
                                            width: "60px",
                                            color: "red",
                                          }}
                                        >
                                          <FontAwesomeIcon
                                            icon={faTimesCircle}
                                            onClick={() =>
                                              setTeamToRemove(
                                                getDriverAndTeamId(item.team)
                                              )
                                            }
                                            title={"Usuń załoge"}
                                            cursor={"pointer"}
                                          />
                                        </td>
                                      </th>
                                    </table>
                                  </Card.Body>
                                </Card>
                              </li>
                            )}
                          </Draggable>
                        );
                      })}
                      {provided.placeholder}
                    </ol>
                  )}
                </Droppable>
              </DragDropContext>
            </div>
          </>
        )}
        {teamToRemove.teamId !== null && (
          <OkCancelModal
            show={true}
            title={"Usuwanie załogi"}
            text={`Czy napewno chcesz usunąć załoge: ${teamToRemove.driver}`}
            handleAccept={() => {
              fetchRemoveFromEvent(eventId, teamToRemove.teamId, () =>
                fetchTeams()
              );
              eraseTeamToRemove();
            }}
            handleClose={() => {
              eraseTeamToRemove();
            }}
          />
        )}
        {teamToEntryFee.teamId !== null && (
          <OkCancelModal
            show={true}
            title={"Potwierdzanie wpisowego"}
            text={`Czy napewno chcesz potwierdzić wpłatę wpisowego przez załoge: ${teamToEntryFee.driver}`}
            handleAccept={() => {
              fetchConfirmEntryFee(eventId, teamToEntryFee.teamId, () =>
                fetchTeams()
              );
              eraseTeamToEntryFee();
            }}
            handleClose={() => {
              eraseTeamToEntryFee();
            }}
          />
        )}
        {startEvent && (
          <OkCancelModal
            show={true}
            title={"Zamykanie listy / Rozpoczynanie wydarzenia"}
            text={`Czy napewno chcesz zamknąć listę i rozpocząć wydarzenie? Operacja nieodwracalna`}
            handleAccept={() => {
              fetchStartEvent();
              setStartEvent(false);
            }}
            handleClose={() => setStartEvent(false)}
          />
        )}
        {teamToEdit && (
          <BasicTeamDataForm
            show={true}
            myTeam={teamToEdit}
            eventId={eventId}
            onSave={(team) => saveTeam(team)}
            okBtnLabel={"Zapisz zmiany"}
            handleClose={() => setTeamToEdit()}
            title={"Edycja danych załogi"}
          />
        )}
        {doSort && (
          <OkCancelModal
            show={true}
            title={"Sortowanie"}
            text={`Czy chcesz posortować zawodników? Pamiętaj, że zresetuje to numery które nie zostały wpisane ręcznie (nie-pogrubione). Jeśli chcesz nadać komuś numer na stałe edytuj wartość na liście.`}
            handleAccept={() => {
              sortByClass();
              setDoSort(false);
            }}
            handleClose={() => setDoSort(false)}
          />
        )}
        {teamToPreview && (
          <TeamModal
            show={true}
            handleClose={() => {
              fetchTeams();
              setTeamToPreview();
            }}
            myEvent={{
              eventId: eventId,
              started: false,
              teamId: teamToPreview,
            }}
            mode="preview"
          />
        )}
        <QuickJoinPanel
          show={quickJoin}
          handleClose={() => {
            fetchTeams();
            setQuickJoin();
          }}
          eventId={eventId}
        />
        <div className="text-center">
          <Button
            className={"m-1"}
            variant="primary"
            onClick={() => setDoSort(true)}
          >
            Sortuj wstępnie wg. klas
          </Button>
          <Button
            className={"m-1"}
            variant="primary"
            onClick={() => fetchOaDocuments()}
          >
            Pobierz dokumenty OA
          </Button>{" "}
          <Button
            className={"m-1"}
            variant="primary"
            onClick={() => fetchBkDocuments()}
          >
            Pobierz dokumenty BK
          </Button>
          <p>{msg}</p>
        </div>
      </Modal.Body>
      <Modal.Footer className={"justify-content-center"}>
        <div className="d-flex">
          {referee && (
            <Button
              className={"m-2"}
              variant="success"
              onClick={() => setQuickJoin(true)}
            >
              Dodaj zawodnika
            </Button>
          )}
          {/* {referee && (
            <Button
              className={"m-2"}
              variant="success"
              onClick={() => setStartEvent(true)}
              disabled={started}
            >
              Zamknij liste / Rozpocznij wydarzenie
            </Button>
          )} */}
          <Button
            className={"m-2"}
            variant="success"
            onClick={saveNumbersAndClasses}
          >
            Zapisz klasy i kolejność załóg
          </Button>
          <Button className={"m-2"} variant="secondary" onClick={handleClose}>
            Wyjdź
          </Button>
        </div>
      </Modal.Footer>
    </Modal>
  );
};
