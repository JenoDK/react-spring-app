import React, {Component} from 'react';
import './App.css';
import {Nav, Navbar} from "react-bootstrap";
import { withRouter, RouteComponentProps } from "react-router-dom";
import { LinkContainer } from "react-router-bootstrap";
import Routes from "./Routes";

export interface State {}

class App extends Component<RouteComponentProps<any>, State> {

    render() {
        return (
            <div className="App">
                <Navbar collapseOnSelect expand="sm" bg="dark" variant="dark">
                    <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                    <Navbar.Collapse id="responsive-navbar-nav">
                        <Nav className="mr-auto">
                            <LinkContainer to="/">
                                <Nav.Link href="#">Home</Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/clients">
                                <Nav.Link href="#">Clients</Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/slims">
                                <Nav.Link href="#">SLIMS</Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/login">
                                <Nav.Link href="#">Login</Nav.Link>
                            </LinkContainer>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Routes/>
            </div>
        );
    }

}

export default withRouter(App);