import { getCarLogo } from "../utils/car";

export const TeamDiv = ({ team }) => {
  const getWithBracketIfNotEmpty = (value) => {
    if (value !== undefined && value !== null && value !== "")
      return " (" + value + ")";

    return "";
  };

  return (
    <div className="float-left">
      <div className="d-flex pt-1" style={{ flexWrap: "wrap" }}>
        <h6 className="text-left font14 fw-bolder fst-italic m-0">
          {team.driver}
        </h6>
        <p className="text-left font12 p-0 div-club align-self-center">
          {getWithBracketIfNotEmpty(team.club)}
        </p>
      </div>
      <div className="d-flex" style={{ flexWrap: "wrap" }}>
        <p className="text-left font13 fw-bolder m-0 p-0">{team.coDriver}</p>
        <p className="text-left font12 p-0 div-club align-self-center">
          {getWithBracketIfNotEmpty(team.coClub)}
        </p>
      </div>
      <p className="text-left font13 fw-bolder m-0 p-0">
        {team.teamName || ""}
      </p>
    </div>
  );
};
export const CarDiv = ({ line1, line2, carBrand, driveType }) => {
  const carImg = carBrand ? getCarLogo(carBrand, 23) : <></>;

  return (
    <div className="col-12 d-flex">
      <div className="col-xl-3 col-4 py-2 align-self-center">{carImg}</div>
      <div className="col-xl-9 col-7">
        <h6 className="font13 fw-bolder m-0">{line1}</h6>
        {driveType && <p className="font12 m-0 p-0">{driveType}</p>}
        <p className="font12 m-0 p-0 fw-bolder">{line2}</p>
      </div>
    </div>
  );
};

export const ScoreDiv = ({ line1, line2, line3 }) => {
  return (
    <div className="float-left">
      <h6 className="font13 fw-bolder m-0">{line1}</h6>
      <p className="font12 m-0 p-0">{line2}</p>
      <p className="font12 m-0 p-0">{line3}</p>
    </div>
  );
};

export const LightScoreDiv = ({ line1, line2, line3 }) => {
  return (
    <div className="float-left fw-normal">
      <h6 className="font13 m-0">{line1}</h6>
      <p className="font12 m-0 p-0">{line2}</p>
      <p className="font12 m-0 p-0">{line3}</p>
    </div>
  );
};

export const ScoreDivPenalty = ({ line1, line2 }) => {
  return (
    <div className="float-left">
      <h6 className="font13 fw-bolder m-0">{line1}</h6>
      {line2 === "0" ? (
        <></>
      ) : (
        <p className="font11 m-0 p-0 fw-bolder text-danger">
          {line2 === "TARYFA" ? line2 : "+" + line2 + " s"}
        </p>
      )}
    </div>
  );
};

export const LightScoreDivPenalty = ({ line1, line2 }) => {
  return (
    <div className="float-left">
      <h6 className="font13 m-0">{line1}</h6>
      {line2 === "0" ? (
        <></>
      ) : (
        <p className="font11 m-0 p-0 text-danger">
          {line2 === "TARYFA" ? line2 : "+" + line2 + " s"}
        </p>
      )}
    </div>
  );
};

export const BkOaDiv = ({ oa, bk }) => {
  const oaColor = oa ? "text-success" : "text-danger";
  const bkColor = bk ? "text-success" : "text-danger";

  return (
    <div className="float-left">
      <>
        {oa === null ? (
          ""
        ) : (
          <p className={"font13 m-0 p-0 fw-bolder " + oaColor}>{"OA"}</p>
        )}
      </>{" "}
      <>
        {bk === null ? (
          ""
        ) : (
          <p className={"font13 m-0 p-0 fw-bolder " + bkColor}>{"BK"}</p>
        )}
      </>
    </div>
  );
};
