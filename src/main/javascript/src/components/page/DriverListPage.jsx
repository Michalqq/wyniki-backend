/* eslint-disable react/display-name */
import React, { useState, useMemo, useEffect } from "react";
import ResultTable from "../common/table/ResultTable";
import axios from "axios";
import { backendUrl } from "../utils/fetchUtils";

const DriverListPage = (props) => {
  const [teams, setTeams] = useState([]);

  const fetchTeams = () => {
    axios.get(`${backendUrl()}/event/getTeams?eventId=1`).then((res) => {
      setTeams(res.data);
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
        cursor={"pointer"}
      />
    </>
  );
};

export default DriverListPage;
