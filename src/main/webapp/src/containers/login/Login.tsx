import React, {Component} from "react";
import "./Login.scss";
import {GOOGLE_AUTH_URL} from "../../constants/Constants";
import googleLogo from "../../images/login/btn_google_signin_dark_normal_web.png"
import googleLogoHover from "../../images/login/btn_google_signin_dark_focus_web.png"
import googleLogoPressed from "../../images/login/btn_google_signin_dark_pressed_web.png"


class Login extends Component {
    render() {
        return (
            <div className="social-login">
                <a className="google" href={GOOGLE_AUTH_URL}>
                    <img src={googleLogo}
                         onMouseOver={e => (e.currentTarget.src = googleLogoHover)}
                         onMouseOut={e => (e.currentTarget.src = googleLogo)}
                         onMouseDown={e => (e.currentTarget.src = googleLogoPressed)}
                    />
                </a>
            </div>
        );
    }
}

export default Login;