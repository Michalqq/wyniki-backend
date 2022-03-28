import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import Spinner from "react-bootstrap/Spinner";
import { InputLabeled } from "../common/InputLabeled";
import { backendUrl } from "../utils/fetchUtils";

export const MessagePage = (props) => {
  const [msg, setMsg] = useState({
    email: "",
    phone: "",
    title: "",
    message: "",
  });
  const [error, setError] = useState();
  const [loading, setLoading] = useState(false);

  const updatePassword = () => {
    setLoading(true);
    setError();
    axios
      .post(`${backendUrl()}/message/sendMsg`, msg)
      .then((res) => {
        setError(res.data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.response.data);
        setLoading(false);
      });
  };

  const handleChange = (event) => {
    setMsg({ ...msg, [event.target.name]: event.target.value });
  };
  const handleSubmit = (event) => {
    event.preventDefault();
    if (!msg.email.includes(".", "@")) {
      setError(
        "Wprowadź poprawny email - nie będziemy mieli możliwości odpowiedzieć"
      );
      return;
    }
    updatePassword();
  };
  return (
    <div className="u-text-center">
      <div className="u-box-shadow">
        <div className="row justify-content-center">
          <div className="col-lg-6 pb-3 u-box-shadow">
            <Card className="text-center">
              <Card.Header className="bg-dark text-white">
                Wyślij wiadomość
              </Card.Header>
              <Card.Body>
                <form onSubmit={handleSubmit}>
                  <InputLabeled
                    label="Tytuł"
                    name="title"
                    handleChange={handleChange}
                    big={true}
                    value={msg.title}
                    required={true}
                  />
                  <InputLabeled
                    label="Email (gdzie mamy odpisać)"
                    name="email"
                    handleChange={handleChange}
                    big={true}
                    value={msg.email}
                    required={true}
                    autoComplete="new-password"
                  />
                  <InputLabeled
                    label="Telefon (opcjonalnie)"
                    name="phone"
                    type="number"
                    handleChange={handleChange}
                    big={true}
                    value={msg.phone}
                    required={false}
                    autoComplete="new-password"
                  />
                  <InputLabeled
                    label="Wiadomość"
                    name="message"
                    handleChange={handleChange}
                    big={true}
                    value={msg.message}
                    required={true}
                    multiline={6}
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
                    Wyślij wiadomość
                  </Button>
                </form>
              </Card.Body>
              <Card.Footer className="text-muted">
                Lub po prostu wyślij do nas maila: wynikionline0@gmail.com
              </Card.Footer>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MessagePage;
