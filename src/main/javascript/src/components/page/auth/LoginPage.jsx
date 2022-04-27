import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import axios from "axios";
import { InputLabeled } from "../../common/InputLabeled";
import { backendUrl } from "../../utils/fetchUtils";
import { useLocation, useNavigate } from "react-router-dom";

export const LoginPage = (props) => {
  const [user, setUser] = useState({ username: "", password: null, email: "" });
  const [logged, setLogged] = useState(sessionStorage.getItem("username"));
  const [error, setError] = useState();
  const navigate = useNavigate();
  const eventRedirect = useLocation().search;

  const signIn = () => {
    setError();
    axios
      .post(`${backendUrl()}/auth/signin`, user)
      .then((res) => {
        if (res.status === 200) {
          setLogged(res.data.username);
          sessionStorage.setItem("username", res.data.username);
          sessionStorage.setItem("token", res.data.token);
          sessionStorage.setItem("roles", res.data.roles);
          navigate("/" + eventRedirect || "");
        } else {
          setError(res.data);
        }
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
    signIn();
  };

  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="row justify-content-center">
          <div className="col-lg-4 pb-3 u-box-shadow">
            <Card className="text-center">
              <Card.Header className="bg-dark text-white">
                Logowanie
              </Card.Header>
              <Card.Body>
                <form onSubmit={handleSubmit}>
                  <InputLabeled
                    label="Login"
                    name="username"
                    handleChange={handleChange}
                    big={true}
                    value={user.username}
                    required={true}
                  />
                  <InputLabeled
                    label="Hasło"
                    name="password"
                    handleChange={handleChange}
                    big={true}
                    value={user.password}
                    type="password"
                    required={true}
                  />
                  {logged && logged !== null && (
                    <p>{`Zalogowany użytkownik: ${logged}`}</p>
                  )}
                  {error && <p>{`${error}`}</p>}
                  <Button
                    className={"px-4 mt-2"}
                    variant="success"
                    type="submit"
                    disabled={logged}
                  >
                    Zaloguj
                  </Button>
                </form>
              </Card.Body>
              <Card.Footer className="text-muted">
                Nie masz konta -<a href="register"> zarejestruj się</a>
                <br></br>
                <a style={{ fontSize: "13px" }} href="reminder">
                  Odzyskaj hasło
                </a>
              </Card.Footer>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
