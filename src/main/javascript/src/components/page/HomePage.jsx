/* eslint-disable react/display-name */
import React, { useState, useMemo, useEffect } from "react";
import ResultTable from "../common/table/ResultTable";
import axios from "axios";
import { formatTableDate } from "../utils/tableUtils";
import { useNavigate } from "react-router-dom";
import { backendUrl } from "../utils/fetchUtils";
import { SubmitButton } from "../common/Button";

const HomePage = (props) => {
  const [events, setEvents] = useState([]);
  const navigate = useNavigate();

  const fetchTeams = () => {
    axios.get(`${backendUrl()}/event/getAll`).then((res) => {
      setEvents(res.data);
    });
  };

  useEffect(() => {
    fetchTeams();
  }, []);

  const columns = useMemo(
    () => [
      {
        width: "10%",
        id: "id",
        Header: "Id",
        accessor: (cellInfo) => cellInfo.eventId,
        disableFilters: true,
      },
      {
        width: "30%",
        id: "name",
        Header: "Nazwa",
        accessor: (cellInfo) => cellInfo.name,
        disableFilters: true,
      },
      {
        width: "30%",
        id: "date",
        Header: "Data wydarzenia",
        accessor: (cellInfo) => formatTableDate(cellInfo.date),
        disableFilters: true,
      },
      {
        width: "30%",
        id: "deadlineDate",
        Header: "Zapisy",
        disableSortBy: true,
        disableFilters: true,
        accessor: (cellInfo) => cellInfo.date,
        disableRowClick: true,
        Cell: (row) => (
          <SubmitButton
            action={() => addTeamToEvent(row)}
            label={"Zapisz się"}
            disabled={new Date().getTime() < new Date(row.value).getTime()}
            paddingTop={" button_in_table"}
            small={true}
          />
        ),
      },
    ],
    []
  );

  const onRowClick = (row) => {
    navigate("event", { state: { eventId: row.allCells[0].value } });
  };

  const addTeamToEvent = (row) => {
    navigate("joinToEvent", { state: { eventId: row.data[0].eventId } });
  };

  return (
    <>
      <ResultTable
        onRowClick={onRowClick}
        columns={columns}
        data={events}
        pageCount={3}
        isLoading={false}
        isFooter={false}
        isHeader={true}
        cursor={"pointer"}
      />
    </>
  );
};

export default HomePage;
