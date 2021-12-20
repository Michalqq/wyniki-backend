export const TeamDiv = ({ line1, line2, line3 }) => {
  return (
    <div className="float-left">
      <h6 className="text-left font14 fw-bolder text-primary m-0">{line1}</h6>
      <p className="text-left font12 fw-lighter m-0 p-0">{line2}</p>
      <p className="text-left font12 fw-bolder m-0 p-0">{line3}</p>
    </div>
  );
};

export const ScoreDiv = ({ line1, line2, line3 }) => {
  return (
    <div className="float-left">
      <h6 className="font12 fw-bolder m-0">{line1}</h6>
      <p className="font12 m-0 p-0">{line2}</p>
      <p className="font12 m-0 p-0">{line3}</p>
    </div>
  );
};
