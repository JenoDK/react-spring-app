import React, {Component} from "react";
import "./Clients.scss";
import {Container, Jumbotron} from "react-bootstrap";
import {callProtectedApi} from "../../utils/ApiUtils";
import clientPlaceHolder from '../../images/clients/client_placeholder.jpg';

interface Client {
    id: number;
    name: string;
    email: string;
    imgSrc?: string;
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

        callProtectedApi("/api/clients")
            .then(response => response.json())
            .then(data => {
                let clients: Array<Client> = data;
                Promise.all(clients.map( client => {
                        return callProtectedApi("/api/pictures/" + client.id)
                            .then(response => response.blob())
                            .then(imgBlob => {
                                client.imgSrc = URL.createObjectURL(imgBlob)
                                return client
                            })
                            .catch(error => {
                                console.error("Unable to fetch image for client " + client.id, error)
                                client.imgSrc = clientPlaceHolder
                                return client
                            })
                    }))
                    .then(cs => this.setState({clients: cs, isLoading: false}))
            });
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
                        <img className="client_picture" src={client.imgSrc} />
                    </div>
                )}
            </Container>
        );
    }
}
