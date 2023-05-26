import React from "react";

import Badge from "react-bootstrap/Badge";

export const NrBadge = ({ value, isBold, onClick }) => {
  const scale = isBold ? "1.3" : "1";
  return (
    <Badge
      className="number-badge"
      bg=""
      onClick={() => onClick()}
      style={{
        scale: scale,
        paddingTop: "5px",
        justifyContent: "center",
        display: "inline-flex",
        width: "22px",
        height: "22px",
        borderRadius: "20px",
        fontSize: "11px",
      }}
    >
      {value}
    </Badge>
  );
};
