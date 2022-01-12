import React, { useState, useEffect } from "react";
import Button from "react-bootstrap/Button";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

import { InputLabeled } from "../common/InputLabeled";
import { backendUrl } from "../utils/fetchUtils";

export const LoginPage = (props) => {
  const [user, setUser] = useState({ username: "", password: null, email: "" });

  const registerUser = () => {
    console.log(user);
    axios.post(`${backendUrl()}/auth/signup`, user).then((res) => {});
  };

  const signIn = () => {
    axios.post(`${backendUrl()}/auth/signin`, user).then((res) => {});
  };

  const handleChange = (event) => {
    setUser({ ...user, [event.target.name]: event.target.value });
  };
  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="row justify-content-center">
          <div className="col-lg-4 pb-3 border-bottom">
            <div className="pb-3" />
            <InputLabeled
              label="Login"
              name="username"
              handleChange={handleChange}
              big={true}
              value={user.username}
            />
            <InputLabeled
              label="Email"
              name="email"
              handleChange={handleChange}
              big={true}
              value={user.email}
            />
            <InputLabeled
              label="Hasło"
              name="password"
              handleChange={handleChange}
              big={true}
              value={user.password}
              type="password"
            />
            <Button
              className={"px-4 mx-3"}
              variant="success"
              onClick={() => registerUser()}
            >
              Zarejestruj
            </Button>
            <Button className={"px-4 mx-3"} variant="success" onClick={signIn}>
              Zaloguj
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
