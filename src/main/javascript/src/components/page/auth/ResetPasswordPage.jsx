import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { InputLabeled } from "../../common/InputLabeled";
import { backendUrl } from "../../utils/fetchUtils";

export const ResetPasswordPage = (props) => {
  const [user, setUser] = useState({
    username: "",
    passss1: "",
    passss2: "",
  });
  const [error, setError] = useState();

  const updatePassword = () => {
    setError();
    axios
      .post(`${backendUrl()}/auth/updatePassword`, user)
      .then((res) => {
        if (res.status === 200) {
          setError("Hasło zostało zaktualizowane. Proszę się zalogować");
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
    if (user.passss1 !== user.passss2) {
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
              <Card.Header className="bg-dark text-white">
                Ustaw nowe hasło
              </Card.Header>
              <Card.Body>
                <form onSubmit={handleSubmit}>
                  <InputLabeled
                    label="Hasło"
                    name="passss1"
                    handleChange={handleChange}
                    big={true}
                    value={user.passss1}
                    type="password"
                    required={true}
                  />
                  <InputLabeled
                    label="Powtórz hasło"
                    name="passss2"
                    handleChange={handleChange}
                    big={true}
                    value={user.passss2}
                    type="password"
                    required={true}
                  />
                  {error && <p>{`${error}`}</p>}
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
                Masz konto -<a href="login"> zaloguj się</a>
              </Card.Footer>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ResetPasswordPage;
