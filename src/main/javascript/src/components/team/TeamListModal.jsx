import React, { useState, useEffect, useMemo } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Spinner from "react-bootstrap/Spinner";
import Button from "react-bootstrap/Button";
import { backendUrl, fetchPsOptions } from "../utils/fetchUtils";
import ResultTable from "../common/table/ResultTable";
import { NrBadge } from "../common/NrBadge";
import { CarDiv, TeamDiv } from "../common/Div";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faQuestion } from "@fortawesome/free-solid-svg-icons";
import { closeOnBack } from "../utils/utils";
import { Selector } from "../common/Selector";

export const TeamListModal = ({ show, handleClose, eventId, started }) => {
  const [teams, setTeams] = useState([]);
  const [startEvent, setStartEvent] = useState(false);
  const [loading, setLoading] = useState(true);
  const [referee, setReferee] = useState(false);
  const [classesOptions, setClassesOptions] = useState([]);
  const [classFilter, setClassFilter] = useState("0");

  const fetchTeams = () => {
    if (eventId === undefined) return;
    axios
      .get(`${backendUrl()}/event/getTeams?eventId=${eventId}`)
      .then((res) => {
        setTeams(res.data);
        setLoading(false);
      });
  };

  useEffect(() => {
    if (show) {
      closeOnBack(handleClose);
      setLoading(true);
      setTeams([]);
      fetchPsOptionsFnc();
    }
    fetchTeams();
  }, [show]);

  useEffect(() => {
    setTeams([]);
    fetchTeams();
  }, [referee]);

  const fetchPsOptionsFnc = () => {
    fetchPsOptions(eventId, (data) => {
      const arr = [{ label: "WSZYSTKIE KLASY", value: "0", defValue: true }];
      data.classesOptions
        .filter((v) => v.label !== "GENERALNA")
        .forEach((x) => arr.push(x));
      setClassesOptions(arr || []);
    });
  };

  const getEngineDesc = (car) => {
    const turbo = car?.turbo ? " (T)" : "";

    return car?.engineCapacity + " cm3" + turbo;
  };

  const columns = useMemo(
    () => [
      {
        width: "8%",
        id: "index",
        Header: "L.p",
        accessor: (cellInfo) => cellInfo.number,
        disableFilters: true,
        Cell: (row) => <div className="ps-2"> {row.row.index + 1}</div>,
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
        disableFilters: true,
        Cell: (row) => (
          <TeamDiv
            team={{
              driver: row.row.original.driver,
              coDriver: row.row.original.coDriver,
              club: row.row.original.club,
              coClub: row.row.original.coClub,
              teamName: row.row.original.teamName,
            }}
          ></TeamDiv>
        ),
      },
      {
        width: "30%",
        id: "car",
        Header: "Samochód",
        accessor: (cellInfo) => cellInfo.car,
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
        width: "11%",
        id: "engine",
        Header: "Silnik",
        accessor: (cellInfo) => getEngineDesc(cellInfo.car),
        disableFilters: true,
      },
      {
        width: "11%",
        id: "entryFee",
        Header: "Potwierdz.",
        accessor: (cellInfo) => cellInfo.entryFeePaid,
        disableFilters: true,
        Cell: (row) => {
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
      <Modal.Header className="bg-dark-green text-white" closeButton>
        <Modal.Title>Lista zapisanych</Modal.Title>
      </Modal.Header>
      <Modal.Body className="px-0">
        {loading && (
          <div className="text-center">
            <Spinner animation="border" variant="secondary" size="lg" />
          </div>
        )}
        {!loading && teams.length === 0 && (
          <h2 className="text-center">Brak zgłoszeń - bądź pierwszy!</h2>
        )}
        {!loading && teams?.length > 0 && (
          <>
            <div className="row pt-0 mx-0 card-body">
              <div className="col-xl-4 px-0 pe-1"></div>
              <div className="col-xl-4 px-0 pe-1">
                <Selector
                  label={"Filtruj klasami"}
                  options={classesOptions}
                  handleChange={(value) => {
                    setClassFilter(value);
                  }}
                  isValid={true}
                />
              </div>
            </div>
            <ResultTable
              columns={columns}
              data={
                classFilter !== "0"
                  ? teams.filter((x) => x.carClass.name === classFilter)
                  : teams
              }
              isLoading={false}
              isFooter={false}
              isHeader={true}
              manualPagination={true}
            />
          </>
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
