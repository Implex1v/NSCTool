import Layout from "./layout";
import {signIn} from "next-auth/react";

export default function AccessDenied() {
    return (
        <Layout title>
            <div className="h-fill row justify-content-center">
                <div className="col-md-4">
                    <h1>Access Denied</h1>
                    <p>You must login to view this page content.</p>
                    <button onClick={() => signIn("keycloak")} className="btn btn-primary btn-block w-100">Login</button>
                </div>
            </div>
        </Layout>
    )
}