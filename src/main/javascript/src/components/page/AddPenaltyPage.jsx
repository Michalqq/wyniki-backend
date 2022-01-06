import React, { useState, useEffect } from "react";
import { Selector } from "../common/Selector";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { InputLabeled } from "../common/InputLabeled";
import { RadioButton } from "../common/Button";
import { backendUrl } from "../utils/fetchUtils";
import { useLocation } from "react-router-dom";

export const AddScorePage = (props) => {
  const location = useLocation();

  const [eventId, setEventId] = useState();

  const [psOptions, setPsOptions] = useState([]);
  const [teamOptions, setTeamOptions] = useState([]);

  const [teamId, setTeamId] = useState();
  const [stage, setStage] = useState();

  const [penaltySec, setPenaltySec] = useState("");
  const [penaltyDesc, setPenaltyDesc] = useState("");

  const [loadingTeams, setLoadingTeams] = useState(false);
  const [disable, setDisable] = useState(false);

  const fetchPsOptions = () => {
    axios
      .get(`${backendUrl()}/event/getPsOptions?eventId=${eventId}`)
      .then((res) => {
        setPsOptions(res.data);
        setStage(res.data[0]?.value);
        fetchTeamsOptions();
      });
  };

  const fetchTeamsOptions = () => {
    setLoadingTeams(true);
    axios
      .get(`${backendUrl()}/score/getTeamOptions?stageId=${stage}&mode=EDIT`)
      .then((res) => {
        setTeamOptions(res.data);
        setLoadingTeams(false);
        setDisable(false);
        if (res.data.length === 0) {
          resetData();
        }
      });
  };

  const postPenalty = (data) => {
    axios.post(`${backendUrl()}/score/addPenalty`, data).then((res) => {});
  };

  useEffect(() => {
    setEventId(location.search.replace("?", ""));
  }, []);

  useEffect(() => {
    if (eventId !== undefined) fetchPsOptions();
  }, [eventId]);

  useEffect(() => {
    if (teamOptions.length > 0) setTeamId(teamOptions[0].value);
  }, [teamOptions]);

  const addPenalty = () => {
    const data = {
      teamId: teamId,
      stageId: stage,
      penaltySec: penaltySec,
      description: penaltyDesc,
    };
    postPenalty(data);
    setPenaltyDesc("");
    setPenaltySec("");
  };

  const resetData = () => {
    setDisable(true);

    setPenaltyDesc("");
    setPenaltySec("");
  };

  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="col-xl-12">
          <h4 className="pb-2 mb-3 border-bottom">Dodaj kare:</h4>
        </div>
        <div className="row">
          <div className="col-lg-6 pb-3 border-bottom">
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
              isLoading={loadingTeams}
            />
          </div>

          <div className="col-lg-6 pb-1 border-bottom">
            <div className="row">
              <div className="col-xl-12">
                <h4>Kara</h4>

                <div className="inline-flex">
                  <InputLabeled
                    label="Sekundy"
                    inputPlaceholder="00"
                    value={penaltySec}
                    handleChange={(e) => setPenaltySec(e)}
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
                    onClick={addPenalty}
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
    </div>
  );
};

export default AddScorePage;
