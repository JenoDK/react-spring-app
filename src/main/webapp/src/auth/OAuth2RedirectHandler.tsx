import React, {Component} from 'react';
import {ACCESS_TOKEN} from '../constants/Constants';
import {Redirect, RouteComponentProps} from 'react-router-dom'

interface OAuth2RedirectHandlerProps extends RouteComponentProps {
    loginSuccess: () => void;
}

class OAuth2RedirectHandler extends Component<OAuth2RedirectHandlerProps, {}> {

    constructor(props: OAuth2RedirectHandlerProps) {
        super(props);
    }

    useQuery() {
        return new URLSearchParams(this.props.location.search)
    }

    render() {
        let query = this.useQuery();
        const token = query.get("token");
        const error = query.get("error");

        if (token) {
            localStorage.setItem(ACCESS_TOKEN, token);
            this.props.loginSuccess()
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