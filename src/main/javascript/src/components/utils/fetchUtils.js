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

export const checkReferee = (eventId, handleResponse) => {
  fetchGet(
    `${backendUrl()}/event/checkReferee?eventId=${eventId}`,
    handleResponse
  );
};

export const fetchTeamChecked = (eventId, teamId, checked, handleResponse) => {
  axios
    .post(
      `${backendUrl()}/event/teamChecked?eventId=${eventId}&teamId=${teamId}&checked=${checked}`,
      {},
      {
        headers: authHeader(),
      }
    )
    .then((res) => {
      handleResponse(res.data);
    });
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
