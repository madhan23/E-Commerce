import React from "react";
import { Outlet } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";
const Layout = () => {
  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      <Header />
      <main style={{ flexGrow: 1, marginTop: "2rem" }}>
        <Outlet />
      </main>

      <Footer />
    </div>
  );
};

export default Layout;
