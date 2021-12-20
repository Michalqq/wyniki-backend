import React, { useState } from "react";
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import DriverListPage from "./components/page/DriverListPage";
import AddScorePage from "./components/page/AddScorePage";
import StageScorePage from "./components/page/StageScorePage";
import HomePage from "./components/page/HomePage";
import { NavigationBar } from "./components/common/NavigationBar";

function App() {
  const [addedNewScore, setAddedNewScore] = useState();

  return (
    <div className="App container">
      {/* <header className="App-header">RACE SCORE</header> */}
      <NavigationBar />
      <div className="shadow bg-body rounded pb-3">
        <div className="p-3">
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route
                path="/add_score"
                element={<AddScorePage setAddedNewScore={setAddedNewScore} />}
              />
              <Route
                path="/event"
                element={<StageScorePage addedNewScore={addedNewScore} />}
              />
              <Route path="/drivers" element={<DriverListPage />} />
            </Routes>
          </BrowserRouter>
        </div>
      </div>
    </div>
  );
}

export default App;
