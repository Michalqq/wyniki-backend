import React from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";

export const NavigationBar = () => {
  const location = useLocation();
  const navigate = useNavigate();

  return (
    <Navbar bg="warning gradient" expand="lg">
      <Container>
        <Navbar.Brand href="#home">Wyniki online AKBP</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">Lista imprez</Nav.Link>
            <Nav.Link href="/">Todo</Nav.Link>

            {location.state?.eventId !== undefined && (
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
                <NavDropdown.Item href="/drivers">
                  Lista startowa
                </NavDropdown.Item>
              </NavDropdown>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};
