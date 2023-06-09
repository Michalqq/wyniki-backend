import React, { useState, useEffect } from "react";
import { Selector } from "../common/Selector";
import axios from "axios";
import { InputLabeled } from "../common/InputLabeled";
import { MyButton, RadioButton } from "../common/Button";
import { backendUrl } from "../utils/fetchUtils";
import { useLocation, useNavigate } from "react-router-dom";
import authHeader from "../../service/auth-header";
import Button from "react-bootstrap/Button";
import { OkCancelModal } from "../common/Modal";

export const AddScorePage = (props) => {
  const location = useLocation();
  const navigate = useNavigate();
  const eventId = location.state.eventId;

  const mode = [
    { value: "NEW", desc: "Wprowadzanie nowych wyników" },
    { value: "EDIT", desc: "Tryb edycji wyników" },
  ];

  const [msg, setMsg] = useState("");
  const [editMode, setEditMode] = useState(mode[0].value);
  const [stageScoreId, setStageScoreId] = useState();

  const [psOptions, setPsOptions] = useState([]);
  const [teamOptions, setTeamOptions] = useState([]);

  const [stageStartHour, setStageStartHour] = useState();
  const [stageStartMin, setStageStartMin] = useState();

  const [scoreMin, setScoreMin] = useState();
  const [scoreSec, setScoreSec] = useState();
  const [scoreMiliSec, setScoreMiliSec] = useState();
  const [teamId, setTeamId] = useState();
  const [stage, setStage] = useState();

  const [loadingTeams, setLoadingTeams] = useState(false);
  const [disable, setDisable] = useState(false);

  const [valid, setValid] = useState(true);
  const [removingScore, setRemovingScore] = useState(false);

  const [calculatingTariff, setCalculatingTariff] = useState(false);

  const fetchPsOptions = () => {
    axios
      .get(`${backendUrl()}/event/getPsOptions?eventId=${eventId}`, {
        headers: authHeader(),
      })
      .then((res) => {
        setPsOptions(res.data);
        setStage(res.data[0]?.value);
      });
  };

  const fetchTeamsOptions = () => {
    setLoadingTeams(true);
    axios
      .get(
        `${backendUrl()}/team/getTeamOptions?eventId=${eventId}&stageId=${stage}&mode=${editMode}`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => {
        setTeamOptions(res.data);
        setLoadingTeams(false);
        setDisable(false);
        if (res.data.length === 0) {
          resetData();
          setMsg("Wszystkie wyniki wybranego odcinka zostały wprowadzone");
        }
      });
  };

  const addScore = (data) => {
    axios
      .post(`${backendUrl()}/score/addScore`, data, {
        headers: authHeader(),
      })
      .then((res) => {
        fetchTeamsOptions();
        setMsg(res.data);
        setTimeout(() => setMsg(), 10000);
      });
  };

  const calcTariff = () => {
    setCalculatingTariff(true);
    axios
      .post(`${backendUrl()}/score/calculateTariff?eventId=${eventId}`, {
        headers: authHeader(),
      })
      .then((res) => {
        setCalculatingTariff(false);
      });
  };

  const removeScore = () => {
    const data = {
      teamId: teamId,
      stageId: stage,
      stageScoreId: editMode === mode[1].value ? stageScoreId : null,
    };

    axios
      .post(`${backendUrl()}/score/removeScore`, data, {
        headers: authHeader(),
      })
      .then((res) => {
        fetchTeamsOptions();
        setMsg(res.data);
        setTimeout(() => setMsg(), 10000);
      });
  };

  useEffect(() => {
    if (eventId === undefined) navigate("/");
    fetchPsOptions();
  }, []);

  useEffect(() => {
    if (psOptions.length > 0 && stage !== undefined) fetchTeamsOptions();
  }, [stage, psOptions]);

  useEffect(() => {
    if (teamOptions.length === 0) return;

    const selectedTeam = teamOptions.find((x) => x.value === teamId);
    setTeamId(selectedTeam ? selectedTeam?.value : teamOptions[0].value);
  }, [teamOptions]);

  useEffect(() => {
    if (teamId !== undefined && editMode === mode[1].value) getTeamData();
  }, [teamId, editMode]);

  useEffect(() => {
    if (stage !== undefined) fetchTeamsOptions();
  }, [editMode]);

  const getTeamData = () => {
    axios
      .get(
        `${backendUrl()}/score/getTeamScore?eventId=${eventId}&stageId=${stage}&teamId=${teamId}`
      )
      .then((res) => {
        setScoreMin(res.data.scoreMin);
        setScoreSec(res.data.scoreSec);
        setScoreMiliSec(res.data.scoreMiliSec);
        setStageScoreId(res.data.stageScoreId);
      });
  };

  const addScoreClick = () => {
    if (notValid()) {
      setValid("Wprowadź pełny wynik");
      return;
    }
    setValid();
    const startStageInMin = stageStartHour * 60 + stageStartMin;
    const scoreInMilis =
      Number(scoreMin) * 60 * 1000 +
      Number(scoreSec) * 1000 +
      Number(scoreMiliSec) * 10;

    const data = {
      teamId: teamId,
      stageId: stage,
      stageStartTime: startStageInMin,
      score: scoreInMilis,
      stageScoreId: editMode === mode[1].value ? stageScoreId : null,
    };
    addScore(data);
    resetData();
  };

  const notValid = () => {
    return (
      scoreMin === undefined ||
      scoreMin === null ||
      scoreMin === "" ||
      scoreSec === undefined ||
      scoreSec === null ||
      scoreSec === "" ||
      scoreMiliSec === undefined ||
      scoreMiliSec === null ||
      scoreMiliSec === ""
    );
  };

  const checkboxChange = (e) => {
    if (e.target.checked && editMode !== e.target.value) {
      setEditMode(e.target.value);
      resetData();
      setTeamOptions([]);
    }
  };

  const resetData = () => {
    setDisable(true);
    setScoreMin("");
    setScoreSec("");
    setScoreMiliSec("");
  };

  return (
    <div className="u-text-center  card-body pb-5">
      <div className="u-box-shadow">
        <div className="col-xl-12">
          <h5 className="pb-1 mb-0 border-bottom">Dodaj wynik:</h5>
        </div>

        <div className="row justify-content-center">
          <div className="col-lg-4 pb-1 border-bottom">
            <div className="centered-grid form-group ">
              <RadioButton
                label={mode[0].desc}
                value={mode[0].value}
                isConfirmed={editMode === mode[0].value}
                onClick={(e) => checkboxChange(e)}
                name={"editMode"}
              />
              <RadioButton
                label={mode[1].desc}
                value={mode[1].value}
                isConfirmed={false}
                onClick={(e) => checkboxChange(e)}
                name={"editMode"}
              />
            </div>
            <div className="pb-0" />
            <Selector
              label={"Odcinek PS"}
              options={psOptions}
              value={sessionStorage.getItem("refereeStageId")}
              handleChange={(value) => {
                setStage(value);
                sessionStorage.setItem("refereeStageId", value);
              }}
              isValid={true}
            />
            <Selector
              label={"Załoga"}
              options={teamOptions}
              value={teamId}
              handleChange={(value) => setTeamId(value)}
              isValid={true}
              isLoading={loadingTeams}
            />
          </div>

          <div className="col-lg-4 pb-1 border-bottom">
            <div className="row">
              <div className="col-xl-12">
                <h5 className="mb-0">Czas</h5>
                <div className="inline-flex">
                  <InputLabeled
                    label="Minuty"
                    inputPlaceholder="00"
                    value={scoreMin}
                    handleChange={(e) => setScoreMin(e.target.value)}
                    disabled={disable}
                    onlyNumber={true}
                    type={"number"}
                  />
                  <InputLabeled
                    label="Sekundy"
                    inputPlaceholder="00"
                    value={scoreSec}
                    handleChange={(e) => setScoreSec(e.target.value)}
                    disabled={disable}
                    onlyNumber={true}
                    max={59}
                    type={"number"}
                  />
                  <InputLabeled
                    label="Setne"
                    inputPlaceholder="00"
                    value={scoreMiliSec}
                    handleChange={(e) => setScoreMiliSec(e.target.value)}
                    disabled={disable}
                    onlyNumber={true}
                    max={99}
                    type={"number"}
                  />
                </div>
                <div className="col-xl-12 pt-1 fw-bolder">{msg}</div>
                <div className="col-xl-12 pt-1 fw-bolder">{valid}</div>
                <div className="col-xl-12 pt-2">
                  <button
                    type="button"
                    className="btn btn-success mx-2 py-1"
                    onClick={addScoreClick}
                    disabled={disable}
                  >
                    Zapisz wynik
                  </button>
                  {editMode === mode[1].value && (
                    <button
                      type="button"
                      className="btn btn-danger mx-2"
                      onClick={() => setRemovingScore(true)}
                      disabled={disable}
                    >
                      Usuń wynik
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="col-sm pt-3"></div>
      <MyButton
        variant="secondary"
        isLoading={calculatingTariff}
        onClick={() => calcTariff()}
        msg="Przelicz wszystkie taryfy"
        loadingMsg="Przeliczanie taryf"
      />
      <Button
        className={"mx-2 py-1 px-2"}
        variant="primary"
        onClick={() =>
          navigate(`/add_penalty`, {
            state: { eventId: eventId },
          })
        }
      >
        Przejdź do dodawania kar
      </Button>
      <Button
        className={"mx-2 py-1 px-2"}
        variant="success"
        onClick={() =>
          navigate(`/event`, {
            state: { eventId: eventId },
          })
        }
      >
        Wyniki
      </Button>
      {removingScore && (
        <OkCancelModal
          show={true}
          title={"Usuwanie załogi"}
          text={`Czy napewno chcesz usunąć wynik załogi:  ${
            teamOptions.find((x) => x.value === teamId)?.label
          }`}
          handleAccept={() => {
            removeScore();
            setRemovingScore(false);
          }}
          handleClose={() => {
            setRemovingScore(false);
          }}
        />
      )}
    </div>
  );
};

export default AddScorePage;
