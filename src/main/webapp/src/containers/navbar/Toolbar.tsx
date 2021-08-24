import React, {useContext} from "react";
import {Nav, Navbar} from "react-bootstrap";
import {AuthContext} from "../../App";
import {ReactComponent as BellIcon} from '../../images/icons/bell.svg';
import {ReactComponent as MessengerIcon} from '../../images/icons/messenger.svg';
import {ReactComponent as Chevron} from '../../images/icons/chevron.svg';
import {ReactComponent as Cog} from '../../images/icons/cog.svg';
import {Link} from "react-router-dom";
import "./Toolbar.scss";

export function Toolbar() {
    const authContext = useContext(AuthContext)
    return <Navbar collapseOnSelect bg="light" variant="light">
            <Nav className="container-fluid">
                <Nav.Item>
                    <Navbar.Brand as={Link} to="/"><span className="navbar-item-contents" >Home</span><BellIcon className="navbar-icon" /></Navbar.Brand>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link as={Link} to="/clients"><span className="navbar-item-contents" >Clients</span><Chevron className="navbar-icon" /></Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link as={Link} to="/test"><span className="navbar-item-contents" >Protected link</span><Cog className="navbar-icon" /></Nav.Link>
                </Nav.Item>
                {authContext.authenticated ?
                    <Nav.Item className="ml-auto">
                        <Nav.Link as={Link} to="/logout"><span className="navbar-item-contents" >Logout</span><MessengerIcon className="navbar-icon" /></Nav.Link>
                    </Nav.Item>
                    :
                    <Nav.Item className="ml-auto">
                        <Nav.Link as={Link} to="/login"><span className="navbar-item-contents" >Login</span><MessengerIcon className="navbar-icon" /></Nav.Link>
                    </Nav.Item>
                }
            </Nav>
    </Navbar>
}

