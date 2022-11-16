import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import axios from "axios";
import { InputLabeled } from "../../common/InputLabeled";
import { backendUrl } from "../../utils/fetchUtils";
import Spinner from "react-bootstrap/Spinner";
import { useNavigate } from "react-router-dom";

export const RegisterPage = (props) => {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    username: "",
    password: null,
    password2: null,
    email: "",
  });
  const [registred, setRegistred] = useState();
  const [error, setError] = useState();
  const [loading, setLoading] = useState(false);

  const registerUser = () => {
    setLoading(true);
    axios
      .post(`${backendUrl()}/auth/signup`, user)
      .then((res) => {
        setRegistred(res.data.username);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.response.data);
        setLoading(false);
      });
  };

  const handleChange = (event) => {
    setUser({ ...user, [event.target.name]: event.target.value });
    if (event.target.name === "password" || event.target.name === "password2")
      setError();
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
              <Card.Header className="bg-dark-green text-white">
                Rejestracja
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
                    autoComplete="new-password"
                  />
                  <InputLabeled
                    label="Email"
                    name="email"
                    handleChange={handleChange}
                    big={true}
                    value={user.email}
                    required={true}
                    type={"email"}
                    autoComplete="new-password"
                  />
                  <InputLabeled
                    label="Hasło"
                    name="password"
                    handleChange={handleChange}
                    big={true}
                    value={user.password}
                    type="password"
                    required={true}
                    autoComplete="new-password"
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
                  {registred && (
                    <p>{`Zarejestrowano użytkownika: ${registred}`}</p>
                  )}
                  {error && <p>{`${error}.`}</p>}
                  {loading && (
                    <div>
                      <Spinner
                        animation="border"
                        variant="secondary"
                        size="lg"
                      />
                    </div>
                  )}
                  <Button
                    className={"px-4 mt-2"}
                    variant="success"
                    type="submit"
                  >
                    Zarejestruj
                  </Button>
                </form>
              </Card.Body>
              <Card.Footer className="text-muted">
                {`Masz konto - `}
                <a role="button" class="a" onClick={() => navigate(`/login`)}>
                  zaloguj się
                </a>
              </Card.Footer>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
