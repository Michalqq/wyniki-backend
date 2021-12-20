/* eslint-disable react/display-name */
import React, { useState, useMemo, useEffect } from "react";
import ResultTable from "../common/table/ResultTable";
import axios from "axios";
import { formatTableDate } from "../utils/tableUtils";
import { useNavigate } from "react-router-dom";

const HomePage = (props) => {
  const [events, setEvents] = useState([]);
  const navigate = useNavigate();

  const fetchTeams = () => {
    axios.get("http://localhost:8080/event/getAll").then((res) => {
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
        id: "Data",
        Header: "#",
        accessor: (cellInfo) => formatTableDate(cellInfo.date),
        disableFilters: true,
      },
    ],
    []
  );

  const onRowClick = (row) => {
    console.log(row.allCells[0].value);
    navigate("event", { state: { eventId: row.allCells[0].value } });
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
