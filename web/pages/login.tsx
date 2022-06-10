import Head from "next/head";
import Layout from '../components/layout'
import Link from "next/link";
import {signIn, useSession} from "next-auth/react";

export default function Login() {
    async function login() {
        console.log("logging in")
        await signIn("keycloak", { callbackUrl: "/profile" })
        return false;
    }

    return (
        <Layout title="">
            <Head>
                <title>Login</title>
            </Head>
            <div className="h-fill row justify-content-center">
                <div className="col-md-4">
                    <div className="alert alert-info">
                        <h4>Before logging-in</h4>
                        <p>You will be redirected to a page where you need to enter your credentials used on the <Link href="/register"><a>register</a></Link> form.</p>
                    </div>

                    <button type="button" onClick={ () => login() } className="btn btn-primary btn-block mb-4 w-100">Login</button>
                    <div className="text-center">
                        <p>
                            Not a member?{' '}
                            <Link href="/register">
                                <a>register</a>
                            </Link>
                        </p>
                    </div>
                </div>
            </div>
        </Layout>
    )
}