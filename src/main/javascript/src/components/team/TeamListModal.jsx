import React, { useState, useEffect, useMemo } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Spinner from "react-bootstrap/Spinner";
import Button from "react-bootstrap/Button";
import "bootstrap/dist/css/bootstrap.min.css";
import { backendUrl } from "../utils/fetchUtils";
import ResultTable from "../common/table/ResultTable";
import { NrBadge } from "../common/NrBadge";
import { CarDiv, TeamDiv } from "../common/Div";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faQuestion } from "@fortawesome/free-solid-svg-icons";

export const TeamListModal = ({ show, handleClose, eventId, started }) => {
  const [teams, setTeams] = useState([]);
  const [startEvent, setStartEvent] = useState(false);
  const [loading, setLoading] = useState(true);
  const [referee, setReferee] = useState(false);

  const fetchTeams = () => {
    if (eventId === undefined) return;
    axios
      .get(`${backendUrl()}/event/getTeams?eventId=${eventId}`)
      .then((res) => {
        setTeams(res.data);
        console.log(res.data);
        setLoading(false);
      });
  };

  useEffect(() => {
    if (show) {
      setLoading(true);
      setTeams([]);
    }
    fetchTeams();
  }, [show]);

  useEffect(() => {
    setTeams([]);
    fetchTeams();
  }, [referee]);

  const columns = useMemo(
    () => [
      {
        width: "8%",
        id: "index",
        Header: "L.p",
        accessor: (cellInfo) => cellInfo.number,
        disableFilters: true,
        Cell: (row) => <> {row.row.index + 1}</>,
      },
      {
        width: "9%",
        id: "#",
        Header: "#Nr",
        accessor: (cellInfo) => cellInfo.number,
        disableFilters: true,
        Cell: (row) => <NrBadge value={row.value}></NrBadge>,
      },
      {
        width: "30%",
        id: "team",
        Header: "Załoga",
        accessor: (cellInfo) => cellInfo.team,
        disableFilters: true,
        Cell: (row) => <TeamDiv team={row.value}></TeamDiv>,
      },
      {
        width: "30%",
        id: "car",
        Header: "Samochód",
        accessor: (cellInfo) => cellInfo.team.currentCar,
        disableFilters: true,
        Cell: (row) => (
          <CarDiv
            line1={(row.value?.brand || "") + " " + (row.value?.model || "")}
            line2={row.row.original.carClass.name}
            carBrand={row.value?.brand}
            driveType={row.value?.driveTypeEnum}
          ></CarDiv>
        ),
      },
      {
        width: "10%",
        id: "engine",
        Header: "Silnik",
        accessor: (cellInfo) =>
          cellInfo.team.currentCar?.engineCapacity + " cm3",
        disableFilters: true,
      },
      {
        width: "15%",
        id: "entryFee",
        Header: "Potwierdz.",
        accessor: (cellInfo) => cellInfo.entryFeePaid,
        disableFilters: true,
        Cell: (row) => {
          console.log(row);
          return (
            <FontAwesomeIcon
              icon={row.value ? faCheck : faQuestion}
              style={{ color: row.value ? "green" : "black" }}
              title={
                row.value ? "Potwierdzony" : "Dodaj potwierdzenie przelewu"
              }
              cursor={"pointer"}
            />
          );
        },
      },
    ],
    []
  );
  return (
    <Modal
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
        <p style={{ fontSize: "11px" }} className="text-center my-0 py-0">
          Aplikacja w fazie testów
        </p>
        {loading && (
          <div className="text-center">
            <Spinner animation="border" variant="secondary" size="lg" />
          </div>
        )}
        {!loading && teams.length === 0 && (
          <h1 className="text-center">Brak zgłoszeń</h1>
        )}
        {!loading && teams?.length > 0 && (
          <ResultTable
            columns={columns}
            data={teams}
            isLoading={false}
            isFooter={false}
            isHeader={true}
            manualPagination={true}
          />
        )}
      </Modal.Body>
      <Modal.Footer className={"justify-content-center"}>
        <div className="d-grid">
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
          <Button className={"m-1"} variant="secondary" onClick={handleClose}>
            Anuluj
          </Button>
        </div>
      </Modal.Footer>
    </Modal>
  );
};
