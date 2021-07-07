import React, {Component} from "react";
import "./Clients.scss";
import {Container, Jumbotron} from "react-bootstrap";

interface Client {
    id: number;
    name: string;
    email: string;
}

interface ClientsProps {
}

interface ClientsState {
    clients: Array<Client>;
    isLoading: boolean;
}

export default class Clients extends Component<ClientsProps, ClientsState> {

    constructor(props: ClientsProps) {
        super(props);

        this.state = {
            clients: [],
            isLoading: false
        };
    }

    async componentDidMount() {
        this.setState({isLoading: true});

        fetch("/api/clients_fetch")
            .then(response => {
                console.log(response)
                return response.json()
            })
            .then(data => this.setState({clients: data, isLoading: false}));
    }

    render() {
        return (
            <Jumbotron fluid className="Home">
                <Container className="lander">
                    <h1>Clients</h1>
                    {this.state.isLoading
                    ? this.renderLoading()
                    : this.renderClients()}
                </Container>
            </Jumbotron>
        );
    }

    renderLoading() {
        return (
            <h1>Loading</h1>
        );
    }

    renderClients() {
        return (
            <Container>
                {this.state.clients.map(client =>
                    <div key={client.id}>
                        {client.name} ({client.email})
                        <img className="client_picture" src={"/api/clients_fetch/" + client.id + "/image"} />
                    </div>
                )}
            </Container>
        );
    }
}
