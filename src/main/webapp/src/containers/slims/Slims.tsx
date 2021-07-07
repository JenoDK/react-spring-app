import React, {Component, FormEvent} from "react";
import {Alert, Container, Jumbotron, Table} from "react-bootstrap";
import {Column, Record, value} from "./Record";
import SlimsFetchForm from "./SlimsFetchForm";

interface SlimsProps {
}

interface SlimsState {
    isLoading: boolean;
    records: Array<Record>;
    tableColumns: Array<Column>;
    fetchFormState: SlimsFetchFormState;
    errorMsg?: string;
}

export interface SlimsFetchFormState {
    table: string;
    columns: string;
    pks: string;
}

const DEFAULT_SLIMS_CONTENT_COLUMNS = ["cntn_id", "cntn_barCode", "cntn_fk_contentType", "cntn_fk_location", "cntn_position_row", "cntn_position_column"]
const DEFAULT_SLIMS_FETCH_VALUES: SlimsFetchFormState = {
    table: "Content",
    columns: DEFAULT_SLIMS_CONTENT_COLUMNS.join(),
    pks: "-1"
}

export default class Slims extends Component<SlimsProps, SlimsState> {

    constructor(props: SlimsProps) {
        super(props);
        this.state = {
            isLoading: false,
            records: [],
            tableColumns: [],
            fetchFormState: JSON.parse(localStorage.getItem('slimsRecordsFetchForm') as string) || DEFAULT_SLIMS_FETCH_VALUES
        };
    }

    async componentDidMount() {
        this.fetchRecords();
    }

    render() {
        return (
            <Jumbotron fluid className="Home">
                <Container className="lander">
                    <h1>SLIMS records</h1>
                    <SlimsFetchForm onSubmit={this.handleSubmit} onChange={this.handleChange} defaultValues={this.state.fetchFormState}/>
                    <Alert variant="light" show={this.state.isLoading}>Loading</Alert>
                    <Alert variant="warning" show={this.state.errorMsg !== undefined}>{this.state.errorMsg}</Alert>
                    {this.renderRecords()}
                </Container>
            </Jumbotron>
        );
    }

    renderRecords() {
        return (
            <Container>
                <Table responsive striped bordered hover size="sm">
                    <thead>
                    <tr>
                        {this.state.tableColumns.map(column =>
                            <th key={column.name}>{column.title}</th>
                        )}
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.records.map(record =>
                        <tr key={record.pk}>
                            {this.state.tableColumns.map(c =>
                                <td key={record.pk + '_' + c.name}>{value(record, c.name)}</td>
                            )}
                        </tr>
                    )}
                    </tbody>
                </Table>
            </Container>
        );
    }

    handleChange = (event: React.FormEvent<any>) => {
        if (event.currentTarget.id) {
            this.setState({ fetchFormState: { ...this.state.fetchFormState, [event.currentTarget.id]: event.currentTarget.value} },
                () => {
                localStorage.setItem('slimsRecordsFetchForm', JSON.stringify(this.state.fetchFormState))
            });
        }
    }

    private getTableColumns(records: Array<Record>) {
        let columns = this.state.fetchFormState.columns.split(",");
        if (records.length > 0) {
            return records[0].columns
                .filter(c => columns.includes(c.name))
                .sort((c1,c2) =>
                    columns.indexOf(c1.name) - columns.indexOf(c2.name)
                );
        } else {
            return [];
        }
    }

    handleSubmit = (event: FormEvent) => {
        event.preventDefault();
        this.fetchRecords();
    }

    fetchRecords = () => {
        this.setState({isLoading: true});
        fetch("/rest/" + this.state.fetchFormState.table + (this.state.fetchFormState.pks === "-1" ? "" : "/" + this.state.fetchFormState.pks))
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Failed to fetch from " + this.state.fetchFormState.table);
                }
            })
            .then(data => {
                let entities = data['entities'];
                let tableColumns = this.getTableColumns(entities);
                let error = undefined;
                if (entities.length == 0) {
                    error = "No records found.";
                } else if (tableColumns.length == 0) {
                    error = "No matching columns found."
                }
                this.setState({
                    tableColumns: tableColumns,
                    records: entities,
                    isLoading: false,
                    errorMsg: error
                })
                console.log(this.state)
            })
            .catch(error => {
                this.setState( {
                    errorMsg: error.message,
                    isLoading: false
                })
            });
    }

}