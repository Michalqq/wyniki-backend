import authHeader from "../../service/auth-header";
import axios from "axios";

export const backendUrl = () => {
  //return "https://wyniki-backend.herokuapp.com";
  //return "http://localhost:8080";
  return "";
};

export const fetchGet = (url, handleResponse) => {
  axios
    .get(url, {
      headers: authHeader(),
    })
    .then((res) => {
      handleResponse(res.data);
    });
};

export const fetchPost = (url, data, handleResponse) => {
  axios
    .post(url, data, {
      headers: authHeader(),
    })
    .then((res) => {
      handleResponse(res.data);
    })
    .catch((err) => {
      if (err.response?.data) handleResponse(err.response.data);
    });
};

export const fetchSaveTeam = (team, handleResponse) => {
  fetchPost(`${backendUrl()}/team/saveTeam`, team, handleResponse);
};

export const checkReferee = (eventId, handleResponse) => {
  fetchGet(
    `${backendUrl()}/event/checkReferee?eventId=${eventId}`,
    handleResponse
  );
};

export const fetchGetScores = (stageId, handleResponse) => {
  fetchGet(
    `${backendUrl()}/score/getStageScores?stageId=${stageId}`,
    handleResponse
  );
};

export const fetchTeamChecked = (eventId, teamId, checked, handleResponse) => {
  fetchPost(
    `${backendUrl()}/event/teamChecked?eventId=${eventId}&teamId=${teamId}&checked=${checked}`,
    null,
    handleResponse
  );
};

export const fetchBkChecked = (eventId, teamId, checked, handleResponse) => {
  fetchPost(
    `${backendUrl()}/event/bkChecked?eventId=${eventId}&teamId=${teamId}&checked=${checked}`,
    null,
    handleResponse
  );
};

export const fetchCreateFinalList = (
  eventId,
  stageId,
  startTime,
  frequency,
  handleResponse
) => {
  fetchGet(
    `${backendUrl()}/event/fetchCreateFinalList?eventId=${eventId}&stageId=${stageId}&startTime=${startTime}&frequency=${frequency}`,
    handleResponse
  );
};

export const fetchRemoveFromEvent = (eventId, teamId, handleResponse) => {
  axios
    .post(
      `${backendUrl()}/event/removeTeam?eventId=${eventId}&teamId=${teamId}`,
      {},
      {
        headers: authHeader(),
      }
    )
    .then((res) => {
      handleResponse(res.data);
    });
};

export const fetchConfirmEntryFee = (eventId, teamId, handleResponse) => {
  axios
    .post(
      `${backendUrl()}/event/confirmEntryFee?eventId=${eventId}&teamId=${teamId}`,
      {},
      {
        headers: authHeader(),
      }
    )
    .then((res) => {
      handleResponse(res.data);
    });
};

export const fetchRemindPassword = (email, handleResponse) => {
  fetchPost(
    `${backendUrl()}/auth/remindPassword?email=${email}`,
    null,
    handleResponse
  );
};

export const fetchStatement = (eventId, handleResponse) => {
  fetchGet(
    `${backendUrl()}/statement/getStatements?eventId=${eventId}`,
    handleResponse
  );
};

export const fetchDriverCount = (eventId, handleResponse) => {
  fetchGet(
    `${backendUrl()}/event/getDriverCount?eventId=${eventId}`,
    handleResponse
  );
};

export const fetchLogoPath = (eventId, handleResponse) => {
  fetchGet(
    `${backendUrl()}/event/getLogoPath?eventId=${eventId}`,
    handleResponse
  );
};
