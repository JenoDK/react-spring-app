import {Redirect, Route} from "react-router-dom";
import React from "react";

export const PrivateRoute = ({component, isAuthenticated, ...rest}: any) => {
    const routeComponent = (props: any) => (
        isAuthenticated
            ? React.createElement(component, props)
            : <Redirect to={{
                pathname: '/login',
                state: {
                    redirectAfterSuccess: rest.path
                }
            }}/>
    );
    return <Route {...rest} render={routeComponent}/>;
};