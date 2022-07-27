import React from "react";
import "./PageButton.scss";
const PageButton = ({ pageNo, setPage, pageIndex, setPageIndex, index }) => {
  return (
    <button
      className={`page-btn ${pageIndex === index ? "selected" : ""}`}
      onClick={() => {
        setPageIndex(pageNo - 1);
        setPage(pageNo);
      }}
    >
      {pageNo}
    </button>
  );
};

export default PageButton;
