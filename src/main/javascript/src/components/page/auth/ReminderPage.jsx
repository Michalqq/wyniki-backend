import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";

import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

import { InputLabeled } from "../../common/InputLabeled";
import { backendUrl } from "../../utils/fetchUtils";

export const RegisterPage = (props) => {
  const [user, setUser] = useState({
    username: "",
    password: null,
    password2: null,
  });
  const [registred, setRegistred] = useState();
  const [error, setError] = useState();

  const registerUser = () => {
    axios
      .post(`${backendUrl()}/auth/signup`, user)
      .then((res) => {
        setRegistred(res.data.username);
      })
      .catch((err) => {
        setError(err.response.data);
      });
  };

  const handleChange = (event) => {
    setUser({ ...user, [event.target.name]: event.target.value });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (user.password !== user.password2) {
      setError("Wprowadź jednakowe hasła");
      return;
    }
    registerUser();
  };

  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="row justify-content-center">
          <div className="col-lg-4 pb-3 u-box-shadow">
            <Card className="text-center">
              <Card.Header className="bg-dark text-white">
                Rejestracja
              </Card.Header>
              <Card.Body>
                <form onSubmit={handleSubmit}>
                  <InputLabeled
                    label="Hasło"
                    name="password"
                    handleChange={handleChange}
                    big={true}
                    value={user.password}
                    type="password"
                    required={true}
                  />
                  <InputLabeled
                    label="Powtórz hasło"
                    name="password2"
                    handleChange={handleChange}
                    big={true}
                    value={user.password2}
                    type="password"
                    required={true}
                  />
                  {error && <p>{`${error}.`}</p>}
                  <Button
                    className={"px-4 mt-2"}
                    variant="success"
                    type="submit"
                  >
                    Resetuj hasło
                  </Button>
                </form>
              </Card.Body>
              <Card.Footer className="text-muted">
                Masz konto -<a href="login"> zaloguj się</a>
              </Card.Footer>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
