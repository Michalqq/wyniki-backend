import React, { useState, useEffect, useMemo } from "react";

import axios from "axios";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import ResultTable from "../common/table/ResultTable";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faDownload,
  faSearch,
  faTimesCircle,
} from "@fortawesome/free-solid-svg-icons";
import { AddStatementModal } from "./AddStatementModal";
import { backendUrl, checkReferee, fetchStatement } from "../utils/fetchUtils";
import moment from "moment";
import { openFile } from "../utils/fileUtils";
import { OkCancelModal, OkModal } from "../common/Modal";
import authHeader from "../../service/auth-header";
import { Spinner } from "react-bootstrap";

export const StatementModal = ({ show, handleClose, event }) => {
  const [addStatement, setAddStatement] = useState();
  const [descriptionModal, setDescriptionModal] = useState();
  const [deleteModal, setDeleteModal] = useState();
  const [loading, setLoading] = useState(true);
  const [statements, setStatements] = useState([]);
  const [referee, setReferee] = useState(false);
  const [hiddenColumns, setHiddenColumns] = useState(["file", "delete"]);

  useEffect(() => {
    if (!show) return;

    if (event?.eventId !== undefined) checkReferee(event?.eventId, setReferee);

    fetchGetStatements();
  }, [show]);

  useEffect(() => {
    if (referee) setHiddenColumns(["file"]);
  }, [referee]);

  const fetchGetStatements = () => {
    setLoading(show);
    fetchStatement(event?.eventId, (data) => {
      setStatements(data);
      setLoading(false);
    });
  };

  const deleteStatement = (statementId) => {
    axios
      .delete(
        `${backendUrl()}/statement/deleteStatement?statementId=${statementId}`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => fetchGetStatements());
  };

  const columns = useMemo(
    () => [
      {
        width: "7%",
        id: "index",
        Header: "L.p",
        accessor: (cellInfo) => cellInfo.number,
        disableFilters: true,
        Cell: (row) => <div className="ps-2"> {row.row.index + 1}</div>,
      },
      {
        width: "25%",
        id: "date",
        Header: "Data dodania",
        accessor: (cellInfo) => cellInfo.date,
        disableFilters: true,
        Cell: (row) => (
          <div className="ps-2">
            {moment(row.value).format("DD-MM-YYYY, HH:mm")}
          </div>
        ),
      },
      {
        width: "45%",
        id: "name",
        Header: "Nazwa",
        accessor: (cellInfo) => cellInfo.name,
        disableFilters: true,
      },
      {
        width: "5%",
        id: "file",
        Header: "Plik",
        accessor: (cellInfo) => cellInfo.file,
        disableFilters: true,
      },
      {
        width: "7%",
        id: "download",
        Header: "Plik",
        accessor: (cellInfo) => cellInfo.fileName,
        disableFilters: true,
        Cell: (row) => {
          return row.value && row.row.original.file ? (
            <FontAwesomeIcon
              icon={faDownload}
              title={"Pobierz"}
              cursor={"pointer"}
              onClick={() => {
                openFile(row.row.original.file, row.value);
              }}
            />
          ) : (
            <></>
          );
        },
      },
      {
        width: "9%",
        id: "view",
        Header: "Opis",
        accessor: (cellInfo) => cellInfo.description,
        disableFilters: true,
        Cell: (row) => {
          return row.value ? (
            <FontAwesomeIcon
              icon={faSearch}
              title={"Podgląd"}
              cursor={"pointer"}
              onClick={() => setDescriptionModal(row.row.original)}
            />
          ) : (
            <></>
          );
        },
      },
      {
        width: "10%",
        id: "delete",
        Header: "Usuń",
        accessor: (cellInfo) => cellInfo.description,
        disableFilters: true,
        Cell: (row) => {
          return (
            <FontAwesomeIcon
              icon={faTimesCircle}
              title={"Usuń komunikat"}
              cursor={"pointer"}
              className="text-danger"
              onClick={() => setDeleteModal(row.row.original)}
            />
          );
        },
      },
    ],
    [hiddenColumns]
  );

  return (
    <>
      <Modal
        show={show}
        style={{
          zIndex: addStatement || descriptionModal || deleteModal ? 1000 : 1055,
        }}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
        size="xl"
      >
        <Modal.Header closeButton className="bg-dark text-white">
          <Modal.Title className="text-white">{`Komunikaty`}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <h5 className="text-center"> {event.name}</h5>{" "}
          {loading ? (
            <div className="text-center">
              <Spinner animation="border" variant="secondary" size="lg" />
            </div>
          ) : statements.length === 0 ? (
            <h4 className="text-center pt-3">
              Organizator nie dodał komunikatów
            </h4>
          ) : (
            <ResultTable
              columns={columns}
              data={statements}
              hiddenColumns={hiddenColumns}
              isLoading={loading}
              isHeader={true}
              manualPagination={true}
            />
          )}
        </Modal.Body>
        <Modal.Footer className={"justify-content-center"}>
          <div className="d-flex">
            {referee && (
              <Button
                className={"m-1"}
                variant="success"
                onClick={() => setAddStatement(true)}
              >
                Dodaj komunikat
              </Button>
            )}
            <Button className={"m-1"} variant="secondary" onClick={handleClose}>
              Wyjdź
            </Button>
          </div>
        </Modal.Footer>
      </Modal>
      <AddStatementModal
        show={addStatement}
        handleClose={() => {
          setAddStatement();
          fetchGetStatements();
        }}
        eventId={event?.eventId}
      />
      {descriptionModal && (
        <OkModal
          show={true}
          title={"Komunikat " + descriptionModal.name}
          text={
            "Dodano: " +
            moment(descriptionModal.date).format("dddd, DD MMM YYYY, HH:mm") +
            "\n\n" +
            descriptionModal.description
          }
          handleAccept={() => setDescriptionModal()}
        />
      )}
      {deleteModal && (
        <OkCancelModal
          show={true}
          title={"Usuwanie komunikatu"}
          text={`Czy chcesz usunąć komunikat ${deleteModal.name} ?`}
          handleAccept={() => {
            deleteStatement(deleteModal.statementId);
            setDeleteModal();
          }}
          handleClose={() => setDeleteModal()}
        />
      )}
    </>
  );
};
