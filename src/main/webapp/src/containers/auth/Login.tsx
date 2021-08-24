import React, {Component} from "react";
import "./Login.scss";
import {ACCESS_TOKEN, FACEBOOK_AUTH_URL, GOOGLE_AUTH_URL} from "../../constants/Constants";
import fbLogo from '../../images/login/fb-logo.png';
import googleLogo from '../../images/login/google-logo.png';
import * as H from "history";
import {Link, Redirect, Route, RouteComponentProps} from "react-router-dom";
import {Alert, Card, Form, FormControl, FormGroup} from "react-bootstrap";
import LoaderButton from "../common/LoaderButton";
import {login} from "../../utils/ApiUtils";
import {AuthContext} from "../../App";

interface LoginProps extends RouteComponentProps {
    location: H.Location<any>;
    history: H.History<any>;
    loginSuccess: () => void;
}

interface LoginState {
    error: string;
    success_msg: string;
}

class Login extends Component<LoginProps, LoginState> {

    constructor(props: LoginProps) {
        super(props);
        this.state = {
            error: "",
            success_msg: ""
        };
    }

    useQuery() {
        return new URLSearchParams(this.props.location.search)
    }

    componentDidMount() {
        if (this.props.history.location && this.props.history.location.state && this.props.history.location.state.sign_up_success) {
            this.setState({ success_msg: "Sign up complete, please log in to continue." })
            // Empty the history state again
            this.props.history.replace({state: {}});
        }
        if (this.props.location.state && this.props.location.state.loginError) {
            setTimeout(() => {
                this.setState({ error: this.props.location.state.loginError})
                this.props.history.replace({
                    pathname: this.props.location.pathname,
                    state: {}
                });
            }, 100);
        }
    }

    render() {
        return (
            <AuthContext.Consumer>
                {value =>
                    {if (value.authenticated) {
                        let query = this.useQuery();
                        const redirect_url = query.get("redirect_url");
                        if (redirect_url) {
                            window.location.href = redirect_url + '?token=' + localStorage.getItem(ACCESS_TOKEN);
                            return (<div/>);
                        } else {
                            return <Redirect to={{
                                pathname: "/",
                                state: { from: this.props.location }
                            }}/>;
                        }
                    } else {
                        return <Card className="center-card login-content">
                            <h1 className="login-title">Login to Jeno</h1>
                            <SocialLogin />
                            <Card.Body>
                                <Alert variant="success" show={this.state.success_msg != ""} onClose={() => this.setState({ success_msg: "" })} dismissible>
                                    <p>{this.state.success_msg}</p>
                                </Alert>
                                <Alert variant="danger" show={this.state.error != ""} onClose={() => this.setState({ error: "" })} dismissible>
                                    <Alert.Heading>Error while trying to log in:</Alert.Heading>
                                    <p>{this.state.error}</p>
                                </Alert>
                                <LoginForm {...this.props} />
                            </Card.Body>
                        </Card>
                    }}
                }
            </AuthContext.Consumer>
        );
    }
}

class SocialLogin extends Component {
    render() {
        return (
            <Card.Body>
                <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
                    <img src={googleLogo} alt="Google" />Log in with Google</a>
                <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                    <img src={fbLogo} alt="Facebook" />Log in with Facebook</a>
            </Card.Body>
        );
    }
}

interface LoginFormState extends LoginState {
    email: string;
    password: string;
    isLoading: boolean;
}

class LoginForm extends Component<LoginProps, LoginFormState> {
    constructor(props: LoginProps) {
        super(props);
        this.state = {
            email: "",
            password: "",
            isLoading: false,
            error: "",
            success_msg: "",
        };
    }

    validateForm() {
        return this.state.email.length > 0 && this.state.password.length > 0;
    }

    handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.currentTarget.id) {
            this.setState<never>({ [event.currentTarget.id]: event.currentTarget.value });
        }
    }

    handleSubmit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();

        this.setState({ isLoading: true });

        login({email: this.state.email, password: this.state.password})
            .then(response => {
                response.json()
                    .then(json => {
                        localStorage.setItem(ACCESS_TOKEN, json.accessToken);
                        this.props.loginSuccess()
                    })
                this.setState({ isLoading: false })
            })
            .catch(error => {
                this.setState({ isLoading: false })
                if (error instanceof Response) {
                    error.text()
                        .then(errorText => {
                            this.setState({error: errorText})
                        })
                } else {
                    this.setState({ error: "Something went wrong, please try again." })
                }
            })
    }

    render() {
        return (
            <form onSubmit={event => this.handleSubmit(event)}>
                <FormGroup controlId="email">
                    <Form.Label>Email</Form.Label>
                    <FormControl
                        autoFocus
                        type="email"
                        value={this.state.email}
                        onChange={this.handleChange}
                    />
                </FormGroup>
                <FormGroup controlId="password">
                    <Form.Label>Password</Form.Label>
                    <FormControl
                        value={this.state.password}
                        onChange={this.handleChange}
                        type="password"
                        isInvalid={this.state.error !== undefined}
                    />
                    <Form.Text className="text-muted">
                        Forgot your password? <Link to="/login/reset">Reset password</Link>
                    </Form.Text>
                    <Form.Control.Feedback type="invalid">
                        {this.state.error}
                    </Form.Control.Feedback>
                </FormGroup>
                <LoaderButton
                    disabled={!this.validateForm()}
                    isLoading={this.state.isLoading}
                    text="Login"
                    loadingText="Logging in…"
                />
                <Form.Text className="text-muted" style={{ fontSize: '80%' }}>
                    No account? <Link to="/signup">Sign up</Link>
                </Form.Text>
            </form>
        );
    }
}

export default Login;