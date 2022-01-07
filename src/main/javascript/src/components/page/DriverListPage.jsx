/* eslint-disable react/display-name */
import React, { useState, useMemo, useEffect } from "react";
import ResultTable from "../common/table/ResultTable";
import axios from "axios";
import { backendUrl } from "../utils/fetchUtils";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimesCircle } from "@fortawesome/free-solid-svg-icons";
import { OkCancelModal } from "../common/Modal";
import "bootstrap/dist/css/bootstrap.min.css";

const DriverListPage = (props) => {
  const [teams, setTeams] = useState([]);
  const [teamToRemove, setTeamToRemove] = useState({
    driver: null,
    teamId: null,
  });

  const eraseTeamToRemove = () => {
    setTeamToRemove({
      driver: null,
      teamId: null,
    });
  };

  const fetchTeams = () => {
    axios.get(`${backendUrl()}/event/getTeams?eventId=1`).then((res) => {
      setTeams(res.data);
    });
  };

  const fetchRemoveTeam = (teamId) => {
    axios.get(`${backendUrl()}/event/removeTeam?teamId=${teamId}`).then(() => {
      fetchTeams();
    });
  };

  useEffect(() => {
    fetchTeams();
  }, []);

  const columns = useMemo(
    () => [
      {
        width: "5%",
        id: "index",
        Header: "L.p.",
        accessor: (cellInfo) => cellInfo.teamId,
        disableFilters: true,
      },
      {
        width: "1%",
        id: "#",
        Header: "#",
        accessor: (cellInfo) => cellInfo.teamId,
        disableFilters: true,
      },
      {
        width: "14%",
        id: "team",
        Header: "Załoga",
        accessor: (cellInfo) => cellInfo.driver,
        disableFilters: true,
      },
      {
        width: "12%",
        id: "car",
        Header: "Samochód",
        accessor: (cellInfo) => cellInfo.car,
        disableFilters: true,
      },
      {
        width: "12%",
        id: "entryFee",
        Header: "Wpisowe",
        accessor: (cellInfo) => cellInfo.entryFee,
        disableFilters: true,
      },
      {
        width: "12%",
        id: "delete",
        Header: "Usuń załoge",
        accessor: (cellInfo) => cellInfo.car,
        disableFilters: true,
        Cell: (cellInfo) => (
          <FontAwesomeIcon
            icon={faTimesCircle}
            onClick={() =>
              setTeamToRemove({
                teamId: cellInfo.row.original.teamId,
                driver: cellInfo.row.original.driver,
              })
            }
            title={"Usuń kare"}
          />
        ),
      },
      {
        width: "12%",
        id: "confirmEntryFee",
        Header: "Potwierdź wpisowe",
        accessor: (cellInfo) => cellInfo.car,
        disableFilters: true,
        Cell: (cellInfo) => (
          <FontAwesomeIcon
            icon={faTimesCircle}
            onClick={() => console.log(teamToRemove)}
            title={"Usuń kare"}
          />
        ),
      },
    ],
    []
  );

  return (
    <>
      <ResultTable
        columns={columns}
        data={teams}
        pageCount={3}
        isLoading={false}
        isFooter={false}
        isHeader={true}
      />
      {teamToRemove.teamId !== null && (
        <OkCancelModal
          show={true}
          title={"Usuwanie załogi"}
          text={`Czy napewno chcesz usunąć załoge ${teamToRemove.driver}`}
          handleAccept={() => {
            fetchRemoveTeam(teamToRemove.teamId);
            eraseTeamToRemove();
          }}
          handleClose={() => {
            eraseTeamToRemove();
          }}
        />
      )}
    </>
  );
};

export default DriverListPage;
