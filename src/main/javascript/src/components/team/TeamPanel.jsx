import React from "react";
import { useNavigate } from "react-router-dom";
import { TeamModal } from "../team/TeamModal";

export const TeamPanel = () => {
  const navigate = useNavigate();

  return (
    <TeamModal
      show={true}
      handleClose={() => navigate(-1)}
      handleOk={() => console.log()}
      mode="teamPanel"
      //   myEvent={event}
    />
  );
};
