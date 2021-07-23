import React, { Component } from 'react';
import { ACCESS_TOKEN } from '../constants/Constants';
import { RouteComponentProps, Redirect } from 'react-router-dom'
import {RouteParams} from "../Routes";

interface OAuth2RedirectHandlerProps extends RouteComponentProps<RouteParams> {}
interface OAuth2RedirectHandlerState {}

class OAuth2RedirectHandler extends Component<OAuth2RedirectHandlerProps, OAuth2RedirectHandlerState> {

    useQuery() {
        return new URLSearchParams(this.props.location.search)
    }

    render() {
        let query = this.useQuery();
        const token = query.get("token");
        const error = query.get("error");

        if (token) {
            localStorage.setItem(ACCESS_TOKEN, token);
            return <Redirect to={{
                pathname: "/",
                state: { from: this.props.location }
            }}/>;
        } else {
            return <Redirect to={{
                pathname: "/login",
                state: {
                    from: this.props.location,
                    loginError: error
                }
            }}/>;
        }
    }
}

export default OAuth2RedirectHandler;