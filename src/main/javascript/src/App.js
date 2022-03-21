import React, { useState } from "react";
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import DriverListPage from "./components/page/DriverListPage";
import AddScorePage from "./components/page/AddScorePage";
import AddPenaltyPage from "./components/page/AddPenaltyPage";
import StageScorePage from "./components/page/StageScorePage";
import HomePage from "./components/page/HomePage";
import TeamCreatePage from "./components/page/TeamCreatePage";
import { NavigationBar } from "./components/common/NavigationBar";
import LoginPage from "./components/page/auth/LoginPage";
import RegisterPage from "./components/page/auth/RegisterPage";
import ReminderPage from "./components/page/auth/ReminderPage";
import ResetPasswordPage from "./components/page/auth/ResetPasswordPage";

function App() {
  const [addedNewScore, setAddedNewScore] = useState();

  return (
    <div>
      {/* <header className="App-header">RACE SCORE</header> */}

      <BrowserRouter>
        <NavigationBar />
        <div className="App container">
          <div className="shadow bg-body rounded pb-3">
            <div className="p-0">
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/reminder" element={<ReminderPage />} />
                <Route path="/passwordReset" element={<ResetPasswordPage />} />

                <Route
                  path="/add_score"
                  element={<AddScorePage setAddedNewScore={setAddedNewScore} />}
                />
                <Route
                  path="/add_penalty"
                  element={
                    <AddPenaltyPage setAddedNewScore={setAddedNewScore} />
                  }
                />
                <Route
                  path="/event"
                  element={<StageScorePage addedNewScore={addedNewScore} />}
                />
                <Route path="/drivers" element={<DriverListPage />} />
                <Route path="/joinToEvent" element={<TeamCreatePage />} />
              </Routes>
            </div>
          </div>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
