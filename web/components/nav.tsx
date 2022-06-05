
export default function Navbar(props) {
    const profileMenu = props.loggedIn ? UserMenu() : GuestMenu();
    return(
        <nav className="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
            <div className="container-fluid">
                <a href="#" className="navbar-brand">NSCTool</a>
                <div className="d-flex">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <a href="" className="nav-link">Foo</a>
                        </li>
                        <li className="nav-item">
                            <a href="" className="nav-link">Bar</a>
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

function UserMenu() {
    return (
        <div>
            fo
        </div>
    )
}

function GuestMenu() {
    return (
        <div>
            <a href="/login" className="btn btn-outline-primary">Login</a>
        </div>
    )
}