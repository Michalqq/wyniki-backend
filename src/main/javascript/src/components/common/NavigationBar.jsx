import React from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { useLocation, Link } from "react-router-dom";

export const NavigationBar = () => {
  const location = useLocation();

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
                  href={`/add_score?${location.state?.eventId}`}
                >
                  Dodaj wynik/karę
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
