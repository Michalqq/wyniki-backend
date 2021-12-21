import React, { useState, useEffect } from "react";
import { Selector } from "../common/Selector";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { InputLabeled } from "../common/InputLabeled";
import { RadioButton } from "../common/Button";
import { backendUrl } from "../utils/fetchUtils";

export const AddUserPanel = (props) => {
  const mode = [
    { value: "NEW", desc: "Wprowadzanie nowych wyników" },
    { value: "EDIT", desc: "Tryb edycji wyników" },
  ];

  const [editMode, setEditMode] = useState(mode[0].value);

  const [psOptions, setPsOptions] = useState([]);
  const [teamOptions, setTeamOptions] = useState([]);

  const [stageStartHour, setStageStartHour] = useState();
  const [stageStartMin, setStageStartMin] = useState();

  const [scoreMin, setScoreMin] = useState();
  const [scoreSec, setScoreSec] = useState();
  const [scoreMiliSec, setScoreMiliSec] = useState();
  const [teamId, setTeamId] = useState("");
  const [stage, setStage] = useState("");

  const [penaltySec, setPenaltySec] = useState("");
  const [penaltyDesc, setPenaltyDesc] = useState("");

  const [loadingTeams, setLoadingTeams] = useState(false);
  const [disable, setDisable] = useState(false);

  const fetchPsOptions = () => {
    axios.get(`${backendUrl()}/event/getPsOptions?eventId=1`).then((res) => {
      setPsOptions(res.data);
    });
  };

  const fetchTeamsOptions = (stageId) => {
    setLoadingTeams(true);
    axios
      .get(
        `${backendUrl()}/score/getTeamOptions?stageId=${stageId}&mode=${editMode}`
      )
      .then((res) => {
        setTeamOptions(res.data);
        setLoadingTeams(false);
      });
  };

  const startEvent = () => {
    axios.post(`${backendUrl()}/event/startEvent?eventId=1`).then((res) => {
      console.log(res);
    });
  };

  const addScore = (data) => {
    axios.post(`${backendUrl()}/score/addScore`, data).then((res) => {
      fetchTeamsOptions(stage);
    });
  };

  const setScoreInMilis = (value) => {
    if (value.length === 1) value = value + "0";
    setScoreMiliSec(value);
  };

  useEffect(() => {
    fetchPsOptions();
  }, []);

  useEffect(() => {
    if (psOptions.length > 0) fetchTeamsOptions(stage);
  }, [stage, psOptions]);

  useEffect(() => {
    if (teamOptions.length > 0) setTeamId(teamOptions[0].value);
  }, [teamOptions]);

  useEffect(() => {
    if (editMode === mode[1].value) getTeamData();
  }, [teamId]);

  const getTeamData = () => {
    axios
      .post(`${backendUrl()}/score/getTeamScore?eventId=1&teamId=1`)
      .then((res) => {
        setScoreMin(res.data.scoreMin);
        setScoreSec(res.data.scoreSec);
        setScoreMiliSec(res.data.scoreMiliSec);
      });
  };

  const addScoreClick = () => {
    const startStageInMin = stageStartHour * 60 + stageStartMin;
    const scoreInMilis =
      scoreMin * 60 * 1000 + scoreSec * 1000 + scoreMiliSec * 10;

    const data = {
      teamId: teamId,
      stageId: stage,
      stageStartTime: startStageInMin,
      score: scoreInMilis,
    };
    addScore(data);
    props.setAddedNewScore(data);
    resetData();
  };

  const checkboxChange = (e) => {
    if (e.target.checked && editMode !== e.target.value) {
      setEditMode(e.target.value);
      resetData();
    }
  };

  const resetData = () => {
    setDisable(true);
    setScoreMin("");
    setScoreSec("");
    setScoreMiliSec("");

    setPenaltyDesc("");
    setPenaltySec("");
  };

  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="col-xl-12">
          <h4 className="pb-2 mb-3 border-bottom">Dodaj wynik:</h4>
        </div>

        <div className="row">
          <div className="col-lg-4 pb-3  border-bottom">
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

            <div className="pb-3" />
            <Selector
              label={"PS"}
              options={psOptions}
              handleChange={(value) => setStage(value)}
              isValid={true}
            />
            <Selector
              label={"Załoga"}
              options={teamOptions}
              handleChange={(value) => setTeamId(value)}
              isValid={true}
              isLoading={teamOptions.length === 0 || loadingTeams}
            />
          </div>

          <div className="col-lg-4 pb-3 border-bottom">
            <div className="row">
              <div className="col-xl-12">
                <h4>Wynik</h4>

                <div className="inline-flex">
                  <InputLabeled
                    label="Minuty"
                    inputPlaceholder="00"
                    handleChange={(e) => setScoreMin(e.target.value)}
                    disabled={disable}
                    onlyNumber={true}
                  />
                  <InputLabeled
                    label="Sekundy"
                    inputPlaceholder="00"
                    handleChange={(e) => setScoreSec(e.target.value)}
                    disabled={disable}
                    onlyNumber={true}
                    max={59}
                  />
                  <InputLabeled
                    label="Setne"
                    inputPlaceholder="00"
                    handleChange={(e) => setScoreInMilis(e.target.value)}
                    disabled={disable}
                    onlyNumber={true}
                    max={99}
                  />
                </div>
                <div className="col-xl-12 pt-5">
                  <button
                    type="button"
                    className="btn btn-success"
                    onClick={addScoreClick}
                    disabled={disable}
                  >
                    Dodaj wynik
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-4 pb-1 border-bottom">
            <div className="row">
              <div className="col-xl-12">
                <h4>Kara</h4>

                <div className="inline-flex">
                  <InputLabeled
                    label="Sekundy"
                    inputPlaceholder="00"
                    handleChange={(e) => setPenaltySec(e.target.value)}
                    disabled={disable}
                  />
                  <textarea
                    value={penaltyDesc}
                    placeholder={"Opis kary"}
                    onChange={(e) => setPenaltyDesc(e.target.value)}
                    className={"form-control centered-grid "}
                    rows={2}
                    disabled={disable}
                  />
                </div>

                <div className="col-xl-12 pt-5">
                  <button
                    type="button"
                    className="btn btn-success"
                    onClick={addScoreClick}
                    disabled={disable}
                  >
                    Dodaj karę
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="col-sm pt-5"></div>
      <button type="button" className="btn btn-success" onClick={startEvent}>
        Start event
      </button>
    </div>
  );
};

export default AddUserPanel;
