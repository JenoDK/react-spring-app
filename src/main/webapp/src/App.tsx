import React, {Component} from 'react';
import './App.scss';
import {Nav, Navbar} from "react-bootstrap";
import {Route, RouteComponentProps, Switch, withRouter} from "react-router-dom";
import {LinkContainer} from "react-router-bootstrap";
import Home from "./containers/Home";
import Clients from "./containers/clients/Clients";
import Login from "./containers/auth/Login";
import Signup from "./containers/auth/Signup";
import OAuth2RedirectHandler from "./auth/OAuth2RedirectHandler";
import NotFound from "./containers/error/NotFound";
import ServerError from "./containers/error/ServerError";

export type RouteParams = {
    loginError: string;
}

export type RouteHistoryLocationState = {
    sign_up_success?: boolean;
}

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
                            <LinkContainer to="/login">
                                <Nav.Link href="#">Login</Nav.Link>
                            </LinkContainer>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Switch>
                    <Route path="/" exact render={props => <Home {...props}/>} />
                    <Route path="/clients" exact render={props => <Clients {...props}/>} />
                    <Route path="/login" exact render={props => <Login {...props} />} />
                    <Route path="/signup" exact render={props => <Signup {...props} />} />
                    <Route path="/oauth2/redirect" component={OAuth2RedirectHandler} />
                    <Route path="/404" component={NotFound} />
                    <Route path="/500" component={ServerError} />
                    { /* Finally, catch all unmatched routes */}
                    <Route component={NotFound} />
                </Switch>
            </div>
        );
    }

}

export default withRouter(App);