import React, { Suspense, lazy } from "react";
import "./App.css";
//import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Footer, NavigationBar } from "./components/common/NavigationBar";
import { TeamPanel } from "./components/team/TeamPanel";
import Spinner from "react-bootstrap/Spinner";

const AddScorePage = lazy(() => import("./components/page/AddScorePage"));
const AddPenaltyPage = lazy(() => import("./components/page/AddPenaltyPage"));
const StageScorePage = lazy(() => import("./components/page/StageScorePage"));
const TeamCreatePage = lazy(() => import("./components/page/TeamCreatePage"));
const MessagePage = lazy(() => import("./components/page/MessagePage"));
const LoginPage = lazy(() => import("./components/page/auth/LoginPage"));
const HomePage = lazy(() => import("./components/page/HomePage"));
const RegisterPage = lazy(() => import("./components/page/auth/RegisterPage"));
const ReminderPage = lazy(() => import("./components/page/auth/ReminderPage"));
const ResetPasswordPage = lazy(() =>
  import("./components/page/auth/ResetPasswordPage")
);

function App() {
  return (
    <div>
      {/* <header className="App-header">RACE SCORE</header> */}

      <BrowserRouter>
        <NavigationBar />
        <Suspense
          fallback={
            <div className="text-center align-middle">
              <Spinner animation="border" variant="secondary" size="lg" />
            </div>
          }
        >
          <div className="App container">
            <div className="bg-body rounded pb-2 opacity-1">
              <div className="p-0">
                <Routes>
                  <Route path="/" element={<HomePage />} />
                  <Route path="/login" element={<LoginPage />} />
                  <Route path="/register" element={<RegisterPage />} />
                  <Route path="/reminder" element={<ReminderPage />} />
                  <Route
                    path="/passwordReset"
                    element={<ResetPasswordPage />}
                  />
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
        </Suspense>
      </BrowserRouter>
    </div>
  );
}

export default App;
