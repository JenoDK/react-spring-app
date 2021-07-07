import React, {Component} from "react";
import {Container, Jumbotron} from "react-bootstrap";

export default class Home extends Component {
    render() {
        return (
            <Jumbotron fluid className="Home">
                <Container className="lander">
                    <h1>My App</h1>
                    <p>My custom app</p>
                </Container>
            </Jumbotron>
        );
    }
}
