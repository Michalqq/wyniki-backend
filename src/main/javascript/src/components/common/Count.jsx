import React from "react";

import Badge from "react-bootstrap/Badge";

export const Count = ({
  count,
  lPos = "-3vh",
  bg = "secondary",
  title = "",
}) => {
  return (
    <div
      data-bs-toggle="tooltip"
      title={title}
      className="user-select-none position-relative"
      style={{ left: lPos, top: "-15px" }}
    >
      {count > 0 && (
        <>
          <Badge className="position-absolute" pill bg={bg}>
            {count}
          </Badge>
        </>
      )}
    </div>
  );
};
