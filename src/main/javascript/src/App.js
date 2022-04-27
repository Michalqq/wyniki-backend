import React from "react";
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import AddScorePage from "./components/page/AddScorePage";
import AddPenaltyPage from "./components/page/AddPenaltyPage";
import StageScorePage from "./components/page/StageScorePage";
import HomePage from "./components/page/HomePage";
import TeamCreatePage from "./components/page/TeamCreatePage";
import { Footer, NavigationBar } from "./components/common/NavigationBar";
import LoginPage from "./components/page/auth/LoginPage";
import RegisterPage from "./components/page/auth/RegisterPage";
import ReminderPage from "./components/page/auth/ReminderPage";
import ResetPasswordPage from "./components/page/auth/ResetPasswordPage";
import MessagePage from "./components/page/MessagePage";
import { TeamPanel } from "./components/team/TeamPanel";

function App() {
  return (
    <div>
      {/* <header className="App-header">RACE SCORE</header> */}

      <BrowserRouter>
        <NavigationBar />
        <div className="App container">
          <div className="shadow bg-body rounded pb-5">
            <div className="p-0">
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/reminder" element={<ReminderPage />} />
                <Route path="/passwordReset" element={<ResetPasswordPage />} />
                <Route path="/message" element={<MessagePage />} />

                <Route path="/add_score" element={<AddScorePage />} />
                <Route path="/add_penalty" element={<AddPenaltyPage />} />
                <Route path="/event" element={<StageScorePage />} />
                <Route path="/joinToEvent" element={<TeamCreatePage />} />
                <Route path="/teamPanel" element={<TeamPanel />} />
              </Routes>
            </div>
          </div>
        </div>
        <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
