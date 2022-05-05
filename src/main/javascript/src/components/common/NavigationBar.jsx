import React, { useEffect, useState } from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { checkReferee } from "../utils/fetchUtils";
import Card from "react-bootstrap/Card";

export const NavigationBar = () => {
  const [referee, setReferee] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  let username = sessionStorage.getItem("username");

  useEffect(
    () => {
      if (location.state?.eventId !== undefined)
        checkReferee(location.state?.eventId, setReferee);
    },
    [location.state?.eventId],
    username
  );

  return (
    <Navbar expand="lg">
      <Container>
        <Navbar.Brand className="nav_bar" href="/">
          Wyniki motorsportowe online AKBP
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">Lista wydarzeń</Nav.Link>
            <Nav.Link href="/message">Kontakt</Nav.Link>

            {referee && location.state?.eventId !== undefined && (
              <NavDropdown title="Administrator" id="basic-nav-dropdown">
                <NavDropdown.Item
                  onClick={() =>
                    navigate(`add_score`, {
                      state: { eventId: location.state.eventId },
                    })
                  }
                >
                  Dodaj wyniki
                </NavDropdown.Item>
                <NavDropdown.Item
                  onClick={() =>
                    navigate(`add_penalty`, {
                      state: { eventId: location.state.eventId },
                    })
                  }
                >
                  Dodaj kary
                </NavDropdown.Item>
              </NavDropdown>
            )}
          </Nav>
          <Nav>
            {sessionStorage.getItem("username") === null && (
              <>
                <Nav.Link href="/login">Zaloguj</Nav.Link>
                <Nav.Link href="/register">Rejestracja</Nav.Link>
              </>
            )}
            {sessionStorage.getItem("username") !== null && (
              <>
                <Nav.Link href="/teamPanel">{`Panel zawodnika - ${sessionStorage.getItem(
                  "username"
                )}`}</Nav.Link>

                <Nav.Link
                  onClick={() => {
                    sessionStorage.removeItem("username");
                    window.location.href = "/";
                  }}
                >
                  Wyloguj
                </Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export const Footer = () => {
  return (
    <div className="fixed-bottom">
      <Card style={{ borderRadius: "0" }} className="bg-black">
        <Card.Footer className="text-white text-start py-0">
          <div className="row font12">
            <div className="col-lg-3 text-end"></div>
            <div className="col-lg-6 text-white text-center">
              Potrzebujesz wyniki na własną imprezę? &nbsp;
              <Link to="message">Wyślij wiadomość</Link>
            </div>
            <div className="col-lg-3 text-center">
              Wszystkie prawa zastrzeżone
            </div>
          </div>
        </Card.Footer>
      </Card>
    </div>
  );
};
