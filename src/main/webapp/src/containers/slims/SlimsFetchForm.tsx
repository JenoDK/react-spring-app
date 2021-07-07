import React, {Component, FormEvent} from "react";
import {Button, Form} from "react-bootstrap";
import {SlimsFetchFormState} from "./Slims";

interface SlimsFetchFormProps {
    onSubmit: (event: FormEvent) => void;
    onChange: (event: FormEvent) => void;
    defaultValues: SlimsFetchFormState;
}

export default class SlimsFetchForm extends Component<SlimsFetchFormProps> {

    constructor(props: SlimsFetchFormProps) {
        super(props);
    }

    render() {
        return (
            <Form onSubmit={this.props.onSubmit}>
                <Form.Group controlId="table">
                    <Form.Label>SLIMS table</Form.Label>
                    <Form.Control
                        placeholder="Enter SLIMS table"
                        defaultValue={this.props.defaultValues.table}
                        onChange={this.props.onChange}/>
                    <Form.Text className="text-muted">
                        What SLIMS table should be queried
                    </Form.Text>
                </Form.Group>

                <Form.Group controlId="columns">
                    <Form.Label>SLIMS columns</Form.Label>
                    <Form.Control
                        placeholder="Enter SLIMS columns"
                        defaultValue={this.props.defaultValues.columns}
                        onChange={this.props.onChange}/>
                    <Form.Text className="text-muted">
                        What SLIMS columns for the configured table should be shown, comma separated
                    </Form.Text>
                </Form.Group>

                <Form.Group controlId="pks">
                    <Form.Label>SLIMS pks</Form.Label>
                    <Form.Control
                        placeholder="Enter SLIMS pks to query"
                        defaultValue={this.props.defaultValues.pks}
                        onChange={this.props.onChange}/>
                    <Form.Text className="text-muted">
                        What SLIMS pks should be queried (-1 to fetch all), comma separated
                    </Form.Text>
                </Form.Group>

                <Button variant="primary" type="submit">
                    Fetch
                </Button>
            </Form>
        );
    }

}