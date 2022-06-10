import {signIn, useSession} from "next-auth/react";
import {Dropdown} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faAddressCard,
    faArrowRightFromBracket,
    faDungeon,
    faPeopleGroup,
    faUser
} from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";

export default function Navbar() {
    const { data: session, status } = useSession()
    const profileMenu = status === "authenticated" ? UserMenu(session) : GuestMenu();

    return(
        <nav className="navbar navbar-expand-md fixed-top navbar-dark bg-black">
            <div className="container-fluid">
                <a href="#" className="navbar-brand">
                    <FontAwesomeIcon icon={faDungeon} />NSCTool
                </a>
                <div className="d-flex">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <Link href="/characters/">
                                <a className="nav-link text-white">
                                    <FontAwesomeIcon icon={faPeopleGroup} />Characters
                                </a>
                            </Link>
                        </li>
                    </ul>
                </div>
                <div className="ms-auto">
                    <div className="col-xl-12">
                        {profileMenu}
                    </div>
                </div>
            </div>
        </nav>
    )
}

function UserMenu(props) {
    return (
        <Dropdown>
            <Dropdown.Toggle variant="primary" id="dropdown-profile">
                <FontAwesomeIcon icon={faUser} />
                {props.user.name + ' '}
            </Dropdown.Toggle>
            <Dropdown.Menu className="bg-dark">
                <Dropdown.Item href="/profile" className="text-white"><FontAwesomeIcon icon={faAddressCard} />Profile</Dropdown.Item>
                <Dropdown.Item href="/characters" className="text-white"><FontAwesomeIcon icon={faPeopleGroup} />My characters</Dropdown.Item>
                <Dropdown.Divider />
                <Dropdown.Item href="/logout" className="text-danger"><FontAwesomeIcon icon={faArrowRightFromBracket} />Logout</Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    )
}

function GuestMenu() {
    return (
        <div>
            <a onClick={() => signIn("keycloak", { callbackUrl: "/profile" })} className="btn btn-outline-primary">Login</a>
        </div>
    )
}