import React from "react";
import {Route, Switch} from "react-router-dom";
import Clients from "./containers/clients/Clients";
import Home from "./containers/Home";
import NotFound from "./containers/error/NotFound";
import ServerError from "./containers/error/ServerError";
import Slims from "./containers/slims/Slims";
import Login from "./containers/login/Login";

export default ({...childProps}) =>
    <Switch>
        <Route path="/" exact render={props => <Home {...childProps} {...props}/>} />
        <Route path="/clients" exact render={props => <Clients {...childProps} {...props}/>} />
        <Route path="/slims" exact render={props => <Slims {...childProps} {...props}/>} />
        <Route path="/login" exact render={props => <Login {...childProps} {...props}/>} />
        <Route path="/404" component={NotFound} />
        <Route path="/500" component={ServerError} />
        { /* Finally, catch all unmatched routes */}
        <Route component={NotFound} />
    </Switch>;

