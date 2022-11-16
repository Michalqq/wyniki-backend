import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { InputLabeled } from "../../common/InputLabeled";
import { fetchRemindPassword } from "../../utils/fetchUtils";
import Spinner from "react-bootstrap/Spinner";
import { useNavigate } from "react-router-dom";

export const RegisterPage = (props) => {
  const navigate = useNavigate();
  const [email, setEmail] = useState();
  const [error, setError] = useState();
  const [loading, setLoading] = useState(false);

  const handleSubmit = (event) => {
    setLoading(true);
    setError();
    event.preventDefault();
    fetchRemindPassword(email, (response) => {
      setError(response);
      setLoading(false);
    });
  };

  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="row justify-content-center">
          <div className="col-lg-4 pb-3 u-box-shadow">
            <Card className="text-center">
              <Card.Header className="bg-dark-green text-white">
                Resetowanie hasła
              </Card.Header>
              <Card.Body>
                <form onSubmit={handleSubmit}>
                  <InputLabeled
                    label="Email"
                    name="email"
                    handleChange={(e) => {
                      setEmail(e.target.value);
                      setError();
                    }}
                    big={true}
                    value={email}
                    type="email"
                    required={true}
                    autoComplete="new-password"
                  />
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
                    Resetuj hasło
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
