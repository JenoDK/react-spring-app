import React from "react";
import "./NotFound.scss";
import { Jumbotron, Container } from "react-bootstrap";

export default () =>
    <Jumbotron>
        <Container>
            <h1>404</h1>
            <h3>Sorry, page not found!</h3>
        </Container>
    </Jumbotron>;
