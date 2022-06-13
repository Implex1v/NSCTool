import '../styles/global.scss'
import {SessionProvider, useSession} from "next-auth/react";

import { config } from '@fortawesome/fontawesome-svg-core'
import '@fortawesome/fontawesome-svg-core/styles.css'
import Layout from "../components/Layout";
import {AuthOption} from "../lib/auth";
import AccessDenied from "../components/unauthenticated";

config.autoAddCss = false

export default function App({
    Component,
    pageProps: { session, ...pageProps }
}) {
    return(
        <SessionProvider session={session}>
            {Component.auth ? (
                <Auth config={Component.auth}>
                    <Component {...pageProps} />
                </Auth>
            ) : (
                <Component {...pageProps} />
            )}
        </SessionProvider>
    )
}

function Auth({ children, config }) {
    const { data: session, status } = useSession()

    if (status === "loading") {
        return (
            <Layout>
                <h3>Loading ...</h3>
            </Layout>
        )
    }

    if (!session && config.auth === AuthOption.Required) {
        return (<AccessDenied />)
    }

    // @ts-ignore
    if (config.role && !session.roles.includes(config.role)) {
        return (<AccessDenied />)
    }

    return children
}


