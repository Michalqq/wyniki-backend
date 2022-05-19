/* eslint-disable react/display-name */
import React, { useState, useMemo, useEffect } from "react";
import axios from "axios";
import ResultTable from "../common/table/ResultTable";
import { useLocation, useNavigate } from "react-router-dom";
import { ScoreDiv, ScoreDivPenalty, TeamDiv, CarDiv } from "../common/Div";
import { Selector } from "../common/Selector";
import {
  backendUrl,
  checkReferee,
  fetchGetScores,
  fetchLogo,
} from "../utils/fetchUtils";
import PenaltyTable from "../tables/PenaltyTable";
import DisqualificationTable from "../tables/DisqualificationTable";
import { NrBadge } from "../common/NrBadge";
import Button from "react-bootstrap/Button";
import { download } from "../utils/fileUtils";
import Spinner from "react-bootstrap/Spinner";
import { MyButton } from "../common/Button";

const StageScorePage = (props) => {
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

  const [event, setEvent] = useState();
  const [logoDto, setLogoDto] = useState({});

  const [scores, setScores] = useState([]);
  const [referee, setReferee] = useState(false);

  const [summedScores, setSummedScores] = useState([]);
  const [psOptions, setPsOptions] = useState([]);
  const [classesOptions, setClassesOptions] = useState([]);

  const [currentClass, setCurrentClass] = useState(GENERAL);
  const [stage, setStage] = useState();

  const [stageName, setStageName] = useState("");

  const [loading, setLoading] = useState(true);
  const [loadingScoreFile, setLoadingScoreFile] = useState(false);

  const fetchScores = () => {
    fetchGetScores(stage, (data) => {
      setScores(data);
      setLoading(false);
    });
  };

  const fetchScoresAndUpdate = () => {
    fetchGetScores(stage, (data) => {
      setScores(data);
      fetchSummedScores();
    });
  };

  const fetchSummedScores = () => {
    axios
      .get(
        `${backendUrl()}/score/getStagesSumScores?eventId=${eventId}&stageId=${stage}`
      )
      .then((res) => {
        setSummedScores(res.data);
        setLoading(false);
      });
  };

  const fetchPsOptions = () => {
    axios
      .get(`${backendUrl()}/event/getStagesAndClasses?eventId=${eventId}`)
      .then((res) => {
        setPsOptions(res.data.psOptions || []);
        setClassesOptions(res.data.classesOptions || []);
      });
  };

  const fetchEvent = () => {
    axios
      .get(`${backendUrl()}/event/getEvent?eventId=${eventId}`)
      .then((res) => {
        setEvent({
          ...res.data,
          date: new Date(res.data.date),
          signDeadline: new Date(res.data.signDeadline),
        });
      });
  };
  const fetchData = () => {
    setLoading(true);
    if (stage !== undefined) {
      fetchScores();
      fetchSummedScores();
      fetchEvent();
    }
  };

  const getScoresFile = () => {
    setLoadingScoreFile(true);
    download(
      `${backendUrl()}/file/getScoresFile?eventId=${eventId}`,
      "wyniki_" + event.name + ".xlsx",
      () => setLoadingScoreFile(false)
    );
  };

  useEffect(() => {
    if (eventId === undefined) navigate("/");
    checkReferee(eventId, setReferee);
    fetchLogo(eventId, (data) => setLogoDto(data));
  }, []);

  // useEffect(() => {
  //   setTimeout(() => fetchScoresAndUpdate(), 1000 * 20);
  // }, [scores]);

  useEffect(() => {
    fetchData();
  }, [stage]);

  useEffect(() => {
    fetchPsOptions();
    fetchData();
  }, []);

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
        Cell: (row) => <NrBadge value={row.value}></NrBadge>,
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

  return (
    <>
      <div className="row card-body mx-0">
        <h4>{event?.name || ""}</h4>
        <div className="col-xl-8 d-flex justify-content-center">
          {(event?.logoPathFile || event?.logoPath) && (
            <div className="col-6 align-self-center">
              <div className="m-2 text-center">
                {logoDto?.file ? (
                  <img
                    id={"eventImage" + event.eventId}
                    style={{ maxHeight: "120px" }}
                    className="img-fluid rounded float-left"
                    src={"data:image/jpg;base64," + logoDto?.file}
                    alt="Logo"
                  ></img>
                ) : (
                  <img
                    style={{ maxHeight: "120px" }}
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
              onClick={() => fetchData()}
            >
              Odśwież
            </Button>
          </div>
        </div>
      </div>
      <div className="row pt-2 mx-0 card-body">
        <div className="col-xl-6 px-0 pe-1">
          <div className="shadow bg-body rounded">
            <div
              className="fw-bold alert alert-secondary p-1 m-0 "
              role="alert"
            >
              {`Czas NA odcinku - ${stageName}`}
            </div>
            <ResultTable
              columns={columns}
              data={
                currentClass !== GENERAL
                  ? scores.filter(
                      (x) =>
                        x.className === currentClass ||
                        x.driveType === currentClass
                    )
                  : scores
              }
              pageCount={3}
              isLoading={loading}
              isFooter={false}
              isHeader={true}
              cursor={"pointer"}
              manualPagination={true}
            />
          </div>
        </div>
        <div className="col-xl-6 px-0 ps-1">
          <div className="shadow bg-body rounded">
            <div className="fw-bold alert alert-secondary p-1 m-0" role="alert">
              {`Suma czasów PO odcinku - ${stageName}`}
            </div>
            <ResultTable
              columns={columns}
              data={
                currentClass !== GENERAL
                  ? summedScores.filter(
                      (x) =>
                        x.className === currentClass ||
                        x.driveType === currentClass
                    )
                  : summedScores
              }
              pageCount={3}
              isLoading={loading}
              isFooter={false}
              isHeader={true}
              cursor={"pointer"}
              manualPagination={true}
            />
          </div>
        </div>
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
    </>
  );
};

export default StageScorePage;
