import {signIn, signOut, useSession} from "next-auth/react";
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
import useTranslation from "next-translate/useTranslation";
import Emoji from "./Emoji";

export default function Navbar() {
    const { data: session, status } = useSession()
    const { t } = useTranslation('common');
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
                                    <FontAwesomeIcon icon={faPeopleGroup} />{t('characters')}
                                </a>
                            </Link>
                        </li>
                    </ul>
                </div>
                <div className="ms-auto">
                    <div className="row">
                        <div className="col-md-2 d-flex align-items-center">
                            <Link href="#" locale="en">
                                <a>
                                    <Emoji symbol="🇬🇧" label="en" />
                                </a>
                            </Link>
                        </div>
                        <div className="col-md-2 d-flex align-items-center">
                            <Link href="#" locale="de">
                                <a>
                                    <Emoji symbol="🇩🇪" label="de" />
                                </a>
                            </Link>
                        </div>
                        <div className="col-md-8">
                            {profileMenu}
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    )
}

function UserMenu(props) {
    const { t } = useTranslation('common')

    return (
        <Dropdown>
            <Dropdown.Toggle variant="primary" id="dropdown-profile">
                <FontAwesomeIcon icon={faUser} />
                {props.user.name + ' '}
            </Dropdown.Toggle>
            <Dropdown.Menu className="bg-dark">
                <Dropdown.Item href="/profile" className="text-white">
                    <FontAwesomeIcon icon={faAddressCard} />{t('profile')}
                </Dropdown.Item>
                <Dropdown.Item href="/characters" className="text-white">
                    <FontAwesomeIcon icon={faPeopleGroup} />{t('myCharacters')}
                </Dropdown.Item>
                <Dropdown.Divider />
                <Dropdown.Item onClick={() => signOut({ callbackUrl: "/" })} className="text-danger">
                    <FontAwesomeIcon icon={faArrowRightFromBracket} />{t('logout')}
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    )
}

function GuestMenu() {
    const { t } = useTranslation('common')

    return (
        <div>
            <a onClick={() => signIn("keycloak", { callbackUrl: "/profile" })} className="btn btn-outline-primary">Login</a>
        </div>
    )
}