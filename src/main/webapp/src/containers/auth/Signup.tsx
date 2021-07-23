import React, {Component} from "react";
import {Card, Form} from "react-bootstrap";
import {RouteComponentProps} from "react-router";
import {Link} from 'react-router-dom';
import LoaderButton from "../common/LoaderButton";
import "./Login.scss";
import {signup} from "../../utils/ApiUtils";

interface MainState {
    isLoading: boolean;
    showErrors: boolean;
    errors: Errors;
    serverErrors: Map<string, string>;
    formState: FormState;
    confirmationCode?: string;
}

interface FormState {
    username?: string;
    email?: string;
    password?: string;
    confirmPassword?: string;
}

interface Errors {
    error_username: string;
    error_email: string;
    error_password: string;
    error_confirmPassword: string;
}

export default class Signup extends Component<RouteComponentProps<any>, MainState> {
    constructor(props: RouteComponentProps<any>) {
        super(props);

        this.state = {
            isLoading: false,
            showErrors: false,
            serverErrors: new Map(),
            errors: {
                error_username: "Please enter a username",
                error_email: "Please enter a valid e-mail address",
                error_password: "Password needs to be min 8 characters and contain at least one uppercase and lowercase letter and one number",
                error_confirmPassword: "Needs to be the same as password",
            },
            formState: {}
        };
    }

    validateForm(): boolean {
        return (
            this.validateUsername() &&
            this.validateEmail() &&
            this.validatePassword() &&
            this.validateConfirmPassword()
        ) as boolean;
    }

    validateUsername = (): boolean => (this.state.formState.username !== undefined && this.state.formState.username.length > 0);

    validatePassword(): boolean {
        let re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;
        return (
            this.state.formState.password !== undefined && this.state.formState.password.length > 0 && re.test(this.state.formState.password)
        );
    }

    validateConfirmPassword(): boolean {
        return (
            this.state.formState.password !== undefined && this.state.formState.confirmPassword !== undefined && 
            this.state.formState.password === this.state.formState.confirmPassword
        );
    }

    validateEmail(): boolean {
        let re = /\S+@\S+\.\S+/;
        return (
            this.state.formState.email !== undefined
            && this.state.formState.email.length > 0
            && re.test(this.state.formState.email)
            && !this.state.serverErrors.has("email")
        );
    }

    getEmailError(): string {
        if (this.state.serverErrors.has("email")) {
            return this.state.serverErrors.get("email")!
        } else {
            return this.state.errors.error_email
        }
    }

    validateConfirmationForm(): boolean {
        return (
            this.state.confirmationCode !== undefined && this.state.confirmationCode.length > 0
        );
    }

    handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.currentTarget.id) {
            this.setState({ formState: { ...this.state.formState, [event.currentTarget.id] : event.currentTarget.value} });
        }
    }

    handleSubmit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();
        this.setState({ showErrors: true, serverErrors: new Map() }, () => {
            if (this.validateForm()) {
                this.setState({ isLoading: true });
                signup({
                    username: this.state.formState.username!,
                    email: this.state.formState.email!,
                    password: this.state.formState.password! })
                    .then(response => {
                        this.setState({ isLoading: false })
                        this.props.history.push({
                            pathname: "/login",
                            state: {
                                sign_up_success: true
                            }
                        });
                    })
                    .catch(response => {
                        this.setState({ isLoading: false })
                        response.json()
                            .then((json: any) => {
                                let localErrorMap = new Map<string, string>()
                                Object.keys(json).forEach((k: string) => {
                                    localErrorMap.set(k, json[k])
                                })
                                this.setState( { serverErrors: localErrorMap } )
                            })
                    })
            }
        })
    }

    renderForm() {
        return (
            <Card className="center-card login-content" >
                <h1 className="login-title">Sign up</h1>
                <Card.Body>
                    <form onSubmit={this.handleSubmit}>
                        <Form.Group controlId="username">
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                                autoFocus
                                type="text"
                                onChange={this.handleChange}
                                isValid={this.state.showErrors && this.validateUsername()}
                                isInvalid={(this.state.showErrors && !this.validateUsername())}
                            />
                            <Form.Control.Feedback type="invalid">
                                {this.state.errors.error_username}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="email">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                autoFocus
                                type="email"
                                onChange={this.handleChange}
                                isValid={this.state.showErrors && this.validateEmail()}
                                isInvalid={this.state.showErrors && !this.validateEmail()}
                            />
                            <Form.Text className="text-muted">
                                We'll never share your email with anyone else.
                            </Form.Text>
                            <Form.Control.Feedback type="invalid">
                                {this.getEmailError()}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="password">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                onChange={this.handleChange}
                                type="password"
                                isValid={this.state.showErrors && this.validatePassword()}
                                isInvalid={this.state.showErrors && !this.validatePassword()}
                            />
                            <Form.Control.Feedback type="invalid">
                                {this.state.errors.error_password}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="confirmPassword">
                            <Form.Label>Confirm Password</Form.Label>
                            <Form.Control
                                onChange={this.handleChange}
                                type="password"
                                isValid={this.state.showErrors && this.validateConfirmPassword()}
                                isInvalid={this.state.showErrors && !this.validateConfirmPassword()}
                            />
                            <Form.Control.Feedback type="invalid">
                                {this.state.errors.error_confirmPassword}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <LoaderButton
                            isLoading={this.state.isLoading}
                            text="Signup"
                            loadingText="Signing up…"
                        />
                        <Form.Text className="text-muted" style={{ fontSize: '80%' }}>
                            Already have an account? <Link to="/login">Sign in</Link>
                        </Form.Text>
                    </form>
                </Card.Body>
            </Card>
        );
    }

    render() {
        return (
            <div className="Signup">
                {this.renderForm()}
            </div>
        );
    }
}
