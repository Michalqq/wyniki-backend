import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import axios from "axios";
import { InputLabeled } from "../../common/InputLabeled";
import { backendUrl } from "../../utils/fetchUtils";
import Spinner from "react-bootstrap/Spinner";
import { useLocation, useNavigate } from "react-router-dom";

export const ResetPasswordPage = (props) => {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    token: useLocation().search,
    username: "",
    password: "",
    password2: "",
  });
  const [error, setError] = useState();
  const [loading, setLoading] = useState(false);

  const updatePassword = () => {
    setLoading(true);
    setError();
    axios
      .post(`${backendUrl()}/auth/updatePassword`, user)
      .then((res) => {
        if (res.status === 200) {
          setError("Hasło zostało zaktualizowane. Proszę się zalogować");
        } else {
          setError(res.data);
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err.response.data);
        setLoading(false);
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
    updatePassword();
  };
  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="row justify-content-center">
          <div className="col-lg-4 pb-3 u-box-shadow">
            <Card className="text-center">
              <Card.Header className="bg-dark-green text-white">
                Ustaw nowe hasło
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
                    autoComplete="new-password"
                  />
                  {error && <p>{`${error}`}</p>}
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
                    Zapisz nowe hasło
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

export default ResetPasswordPage;
