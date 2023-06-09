import React from "react";

const TableHeader = ({ isHeader, headerGroups }) => {
  const getSortIcon = (column) => {
    if (column.isSorted) {
      return column.isSortedDesc ? " ▼" : " ▲";
    } else {
      return "";
    }
  };
  if (isHeader) {
    return (
      <thead className="thead-dark">
        {headerGroups.map((headerGroup, index) => (
          <tr
            key={"tableHeader" + index}
            style={{ display: "table-row" }}
            {...headerGroup.getHeaderGroupProps()}
            className="l-table-row text-white bg-dark-green"
          >
            {headerGroup.headers.map((column) => (
              <th
                key={"tableHeaderCell" + column.id}
                {...column.getHeaderProps()}
                className="px-1 py-0 align-middle"
                style={{
                  width: column.width,
                  wordBreak: "break-word",
                }}
              >
                <div className="flex flex-column">
                  <div
                    {...column.getHeaderProps(column.getSortByToggleProps())}
                    className="py-1 my-0 flex align-items-end justify-content-center h6 l-row g-brand2-c table-column-header"
                  >
                    <span className="font13 flex">
                      {column.Header != null && column.render("Header")}
                    </span>
                    <span className="font13 g-gray4-c">
                      {getSortIcon(column)}
                    </span>
                  </div>
                  <div className="flex align-items-end">
                    {column.canFilter ? column.render("Filter") : null}
                  </div>
                </div>
              </th>
            ))}
          </tr>
        ))}
      </thead>
    );
  } else {
    return <></>;
  }
};
export default TableHeader;
