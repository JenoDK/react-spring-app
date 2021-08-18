import React, {Component, useContext} from 'react';
import './App.scss';
import {Nav, Navbar} from "react-bootstrap";
import {Redirect, Route, RouteComponentProps, Switch, withRouter} from "react-router-dom";
import {LinkContainer} from "react-router-bootstrap";
import Home from "./containers/Home";
import Clients from "./containers/clients/Clients";
import Login from "./containers/auth/Login";
import Signup from "./containers/auth/Signup";
import OAuth2RedirectHandler from "./auth/OAuth2RedirectHandler";
import NotFound from "./containers/error/NotFound";
import ServerError from "./containers/error/ServerError";
import {defaultErrorLogging, getCurrentUser} from "./utils/ApiUtils";
import {ACCESS_TOKEN} from "./constants/Constants";

interface User {
    id: string;
    email: string;
    username: string;
}

interface Authentication {
    authenticated: boolean;
    user?: User;
}

interface AppState {
    authContext?: Authentication;
}

export const AuthContext = React.createContext<Authentication>({
    authenticated: false,
})

class App extends Component<RouteComponentProps<any>, AppState> {

    constructor(props: RouteComponentProps<any>) {
        super(props);
        this.state = {}

        this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this)
    }

    componentDidMount() {
        this.loadCurrentlyLoggedInUser();
    }

    loadCurrentlyLoggedInUser() {
        getCurrentUser()
            .then(response => {
                response.json()
                    .then(json => {
                        console.log(json)
                        this.setState({
                            authContext: {
                                authenticated: true,
                                user: json,
                            },
                        });
                    })
            })
            .catch(error => {
                this.setState({
                    authContext: {
                        authenticated: false,
                    },
                });
                defaultErrorLogging(error)
            });
    }

    render() {
        return (
            <div className="App">
                {/* Only render when we have our authContext */}
                { this.state.authContext &&
                    <AuthContext.Provider value={this.state.authContext}>
                        <Toolbar />
                        <Switch>
                            <Route path="/" exact render={props => <Home {...props}/>} />
                            <Route path="/clients" exact render={props => <Clients {...props}/>} />
                            <Route path="/login" exact render={props => <Login loginSuccess={this.loadCurrentlyLoggedInUser} {...props} />} />
                            <Route path="/signup" exact render={props => <Signup {...props} />} />
                            <Route path="/oauth2/redirect" exact render={props => <OAuth2RedirectHandler loginSuccess={this.loadCurrentlyLoggedInUser} {...props} />} />
                            <Route path="/404" component={NotFound} />
                            <Route path="/500" component={ServerError} />
                            <Route path="/logout" component={Logout} />
                            { /* Finally, catch all unmatched routes */}
                            <Route component={NotFound} />
                        </Switch>
                    </AuthContext.Provider>
                }
            </div>
        );
    }

}

function Toolbar() {
    const authContext = useContext(AuthContext)
    return <Navbar collapseOnSelect expand="sm" bg="dark" variant="dark">
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="mr-auto">
                <LinkContainer to="/">
                    <Nav.Link href="#">Home</Nav.Link>
                </LinkContainer>
                <LinkContainer to="/clients">
                    <Nav.Link href="#">Clients</Nav.Link>
                </LinkContainer>
                {authContext.authenticated ?
                    <LinkContainer to="/logout">
                        <Nav.Link href="#">Logout</Nav.Link>
                    </LinkContainer>
                    :
                    <LinkContainer to="/login">
                        <Nav.Link href="#">Login</Nav.Link>
                    </LinkContainer>
                }
            </Nav>
        </Navbar.Collapse>
    </Navbar>
}

function Logout(props: RouteComponentProps<any>) {
    const authContext = useContext(AuthContext)
    localStorage.removeItem(ACCESS_TOKEN)
    authContext.authenticated = false
    authContext.user = undefined
    return <Redirect to={{
        pathname: "/login",
        state: { from: props.location }
    }}/>;
}

export default withRouter(App);