/* eslint-disable react/display-name */
import React, { useState, useMemo, useEffect } from "react";
import axios from "axios";
import ResultTable from "../common/table/ResultTable";
import { useLocation, useNavigate } from "react-router-dom";
import { ScoreDiv, ScoreDivPenalty, TeamDiv, CarDiv } from "../common/Div";
import { Selector } from "../common/Selector";
import { backendUrl, checkReferee, fetchPsOptions } from "../utils/fetchUtils";
import PenaltyTable from "../tables/PenaltyTable";
import DisqualificationTable from "../tables/DisqualificationTable";
import { NrBadge } from "../common/NrBadge";
import Button from "react-bootstrap/Button";
import { MyButton } from "../common/Button";
import { calcTimeTo } from "../utils/utils";
import Tabs from "react-bootstrap/Tabs";
import Tab from "react-bootstrap/Tab";
import { CompareScoresModal } from "../scores/CompareScoresModal";
import {
  useGetScoresQuery,
  useGetSummedScoresQuery,
} from "../../service/rtk-fetch-api";

const StageScorePage = (props) => {
  const [activeTab, setActiveTab] = useState(1);
  const [markedNumbers, setMarkedNumbers] = useState([]);
  const [showCompareScoresModal, setShowCompareScoresModal] = useState(false);
  const location = useLocation();
  const eventRedirect = useLocation().search;
  const index = eventRedirect.includes("&")
    ? eventRedirect.indexOf("&")
    : eventRedirect.length;
  const eventFromRedirect = eventRedirect.substring(0, index).replace("?", "");

  const navigate = useNavigate();

  const eventId = eventFromRedirect
    ? eventFromRedirect
    : localStorage.getItem("eventId") || location.state?.eventId;

  const GENERAL = "GENERALNA";
  const GUEST = "GOŚĆ";

  const [event, setEvent] = useState();

  // const [scores, setScores] = useState([]);
  const [scoresByClass, setScoresByClass] = useState([]);
  const [referee, setReferee] = useState(false);

  // const [summedScores, setSummedScores] = useState([]);
  const [summedScoresByClass, setSummedScoresByClass] = useState([]);

  const [psOptions, setPsOptions] = useState([]);
  const [classesOptions, setClassesOptions] = useState([]);

  const [currentClass, setCurrentClass] = useState(GENERAL);
  const [stage, setStage] = useState();

  const [stageName, setStageName] = useState("");

  const [loading, setLoading] = useState(true);
  const [loadingScoreFile, setLoadingScoreFile] = useState(false);

  const {
    data: scores = [],
    isFetching,
    refetch: scoresRefetch,
  } = useGetScoresQuery(stage, {
    skip: stage === undefined,
  });

  const {
    data: summedScores = [],
    isFetching: summedScoresFetching,
    refetch: summedScoreRefetch,
  } = useGetSummedScoresQuery(
    { eventId: eventId, stageId: stage },
    {
      skip: stage === undefined || eventId === undefined,
    }
  );

  const fetchPsOptionsFnc = () => {
    fetchPsOptions(eventId, (data) => {
      setPsOptions(data.psOptions || []);
      setClassesOptions(data.classesOptions || []);
    });
  };

  const fetchEvent = () => {
    axios
      .get(`${backendUrl()}/event/getBasicEvent?eventId=${eventId}`)
      .then((res) => {
        setEvent({
          ...res.data,
          date: new Date(res.data.date),
          signDeadline: new Date(res.data.signDeadline),
        });
      });
  };

  const fetchData = () => {
    if (stage !== undefined) {
      fetchEvent();
    }
  };

  const getScoresFile = () => {
    // setLoadingScoreFile(true);
    window.open(
      `${backendUrl()}/file/getScoresFile?eventId=${eventId}`,
      "_blank"
    );
    // download(
    //   `${backendUrl()}/file/getScoresFile?eventId=${eventId}`,
    //   "wyniki_" + event.name + ".xlsx",
    //   () => setLoadingScoreFile(false)
    // );
  };

  useEffect(() => {
    if (eventId === undefined) navigate("/");
    checkReferee(eventId, setReferee);
  }, []);

  useEffect(() => {
    fetchData();
    setScoresByClass([]);
    setSummedScoresByClass([]);
  }, [stage]);

  useEffect(() => {
    fetchPsOptionsFnc();
    fetchData();
  }, []);

  useEffect(() => {
    if (isFetching || scores.length === 0) return;

    const tempScores =
      currentClass === `${GENERAL}+${GUEST}`
        ? scores
        : currentClass === GENERAL
        ? scores.filter((x) => x.className !== GUEST)
        : scores.filter(
            (x) => x.className === currentClass || x.driveType === currentClass
          );

    setScoresByClass(calcTimeTo(tempScores));
  }, [scores, currentClass]);

  useEffect(() => {
    if (summedScoresFetching || summedScores.length === 0) return;

    let tempScores =
      currentClass === `${GENERAL}+${GUEST}`
        ? summedScores
        : currentClass === GENERAL
        ? summedScores.filter((x) => x.className !== GUEST)
        : summedScores.filter(
            (x) => x.className === currentClass || x.driveType === currentClass
          );

    setSummedScoresByClass(calcTimeTo(tempScores));
  }, [summedScores, currentClass]);

  const columns = useMemo(
    () => [
      {
        width: "5%",
        id: "place",
        Header: "P.",
        accessor: (cellInfo) => cellInfo.place,
        disableFilters: true,
        disableSortBy: true,
        Cell: (row) => <> {row.row.index + 1}</>,
      },
      {
        width: "7%",
        id: "nr",
        Header: "Nr",
        accessor: (cellInfo) => cellInfo.number,
        disableFilters: true,
        disableSortBy: true,
        Cell: (row) => (
          <NrBadge
            value={row.value}
            isBold={markedNumbers.includes(row.value)}
            onClick={() => handleMarked(row.value)}
          ></NrBadge>
        ),
      },
      {
        width: "30%",
        id: "team",
        Header: "Załoga",
        disableFilters: true,
        disableSortBy: true,
        Cell: (cellInfo) => <TeamDiv team={cellInfo.row.original} />,
      },
      {
        width: "30%",
        id: "car",
        Header: "Samochód",
        disableFilters: true,
        disableSortBy: true,
        Cell: (cellInfo) => (
          <CarDiv
            line1={cellInfo.row.original.car}
            line2={cellInfo.row.original.className}
            carBrand={cellInfo.row.original.brand}
            driveType={cellInfo.row.original.driveType}
          />
        ),
      },
      {
        width: "15%",
        id: "score",
        Header: "Czas / kary",
        accessor: (cellInfo) => cellInfo.stageScore,
        disableFilters: true,
        disableSortBy: true,
        Cell: (cellInfo) => (
          <ScoreDivPenalty
            line1={cellInfo.row.original.stageScore}
            line2={cellInfo.row.original.totalPenalty}
          />
        ),
      },
      {
        width: "15%",
        id: "result",
        Header: "Wynik / straty",
        accessor: (cellInfo) => cellInfo.stageScore,
        disableFilters: true,
        disableSortBy: true,
        Cell: (cellInfo) => (
          <ScoreDiv
            line1={cellInfo.row.original.totalTime}
            line2={cellInfo.row.original.timeTo}
            line3={cellInfo.row.original.timeToFirst}
          />
        ),
      },
    ],
    []
  );

  const handleMarked = (number) => {
    if (!markedNumbers.includes(number)) markedNumbers.push(number);
    else markedNumbers.splice(markedNumbers.indexOf(number), 1);

    setMarkedNumbers(Array.from(markedNumbers));
  };

  const highlightRow = (row) => {
    if (markedNumbers.includes(row.values.nr)) return "#c9e9a7";
  };

  return (
    <>
      <div className="row card-body mx-0">
        <h4>{event?.name || ""}</h4>
        <div className="col-xl-8 d-flex justify-content-center">
          {(event?.logoPathFile || event?.logoPath) && (
            <div className="col-6 align-self-center">
              <div className="m-0 text-center">
                {event.logoPathFile ? (
                  <img
                    id={"eventImage" + event.eventId}
                    style={{ maxHeight: "150px" }}
                    className="img-fluid rounded float-left"
                    src={"data:image/jpg;base64," + event.logoPathFile}
                    alt="Logo"
                  ></img>
                ) : (
                  <img
                    style={{ maxHeight: "150px" }}
                    className="img-fluid rounded float-left"
                    src={event.logoPath}
                    alt="Logo"
                  ></img>
                )}
              </div>
            </div>
          )}
          <div className="col-6">
            <Selector
              label={"Klasyfikacja"}
              options={classesOptions}
              handleChange={(value) => {
                setCurrentClass(
                  classesOptions.find((e) => e.value === value).label
                );
              }}
              isValid={true}
            />
            <Selector
              label={"PS"}
              options={psOptions}
              value={sessionStorage.getItem("scoreStageId")}
              handleChange={(value) => {
                setStage(value);
                setStageName(psOptions.find((e) => e.value === value).label);
                sessionStorage.setItem("scoreStageId", value);
              }}
              isValid={true}
            />
          </div>
        </div>
        <div className="col-xl-4">
          <div className="m-2 text-center">
            {referee && (
              <>
                <Button
                  className={"m-1"}
                  variant="success"
                  onClick={() =>
                    navigate(`/add_score`, {
                      state: { eventId: eventId },
                    })
                  }
                >
                  Dodaj wynik
                </Button>
                <Button
                  className={"m-1"}
                  variant="primary"
                  onClick={() =>
                    navigate(`/add_penalty`, {
                      state: { eventId: eventId },
                    })
                  }
                >
                  Dodaj kare
                </Button>
                <MyButton
                  variant="secondary"
                  isLoading={loadingScoreFile}
                  onClick={() => getScoresFile()}
                  msg="Generuj plik z zestawieniem wyników"
                  loadingMsg="Trwa generowanie
                  pliku z wynikami"
                />
              </>
            )}
            <Button
              className={"m-1"}
              variant="primary"
              onClick={() => {
                summedScoreRefetch();
                scoresRefetch();
              }}
            >
              Odśwież
            </Button>
            <Button
              className={"m-1"}
              variant="success"
              onClick={() => setShowCompareScoresModal(true)}
            >
              Porównaj wyniki
            </Button>
          </div>
        </div>
      </div>
      <div className="row pt-0 mx-0 card-body">
        <div
          className="fw-bold alert alert-secondary p-1 mb-2"
          role="alert"
        >{`${stageName}`}</div>
        <Tabs
          activeKey={activeTab}
          onSelect={(key) => setActiveTab(key)}
          className="mb-1 fw-bold text-dark device-small"
        >
          <Tab eventKey={1} title="Czas NA" className="custom-tab">
            <div className="my-pe-1">
              <div className="shadow bg-body rounded">
                <ResultTable
                  columns={columns}
                  data={scoresByClass}
                  pageCount={3}
                  isLoading={isFetching}
                  isFooter={false}
                  isHeader={true}
                  cursor={"pointer"}
                  manualPagination={true}
                  highlightRow={(row) => highlightRow(row)}
                />
              </div>
            </div>
          </Tab>
          <Tab eventKey={2} title="Suma PO" className="custom-tab">
            <div className="my-ps-1">
              <div className="shadow bg-body rounded">
                <ResultTable
                  columns={columns}
                  data={summedScoresByClass}
                  pageCount={3}
                  isLoading={summedScoresFetching}
                  isFooter={false}
                  isHeader={true}
                  cursor={"pointer"}
                  manualPagination={true}
                  highlightRow={(row) => highlightRow(row)}
                />
              </div>
            </div>
          </Tab>
        </Tabs>
        <div className="col-xl-12 px-1">
          <div className="shadow bg-body rounded mt-4 p-0">
            <div className="fw-bold alert alert-secondary p-1 m-0" role="alert">
              {"Kary"}
            </div>
            <PenaltyTable
              eventId={eventId}
              onRemove={fetchData}
              referee={referee}
            />
          </div>
          <div className="shadow bg-body rounded mt-4">
            <div className="fw-bold alert alert-secondary p-1 m-0" role="alert">
              {`Dyskwalifikacje / Wycofania`}
            </div>
            <DisqualificationTable
              eventId={eventId}
              onRemove={fetchData}
              referee={referee}
            />
          </div>
        </div>
      </div>
      {showCompareScoresModal && (
        <CompareScoresModal
          show={true}
          handleClose={() => setShowCompareScoresModal()}
          eventId={eventId}
          markedNumbers={markedNumbers}
          psOptions={psOptions}
        />
      )}
    </>
  );
};

export default StageScorePage;
