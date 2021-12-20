import React from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";

export const NavigationBar = () => {
  return (
    <Navbar bg="light" expand="lg">
      <Container>
        <Navbar.Brand href="#home">Wyniki online AKBP</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">Lista imprez</Nav.Link>
            <Nav.Link href="/">Todo</Nav.Link>

            <NavDropdown title="Administrator" id="basic-nav-dropdown">
              <NavDropdown.Item href="/add_score">
                Dodaj wyniki/kary
              </NavDropdown.Item>
              <NavDropdown.Item href="#action/3.2">
                Another action
              </NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};
