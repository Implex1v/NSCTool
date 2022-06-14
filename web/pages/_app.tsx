import '../styles/global.scss'
import {SessionProvider, useSession} from "next-auth/react";

import { config } from '@fortawesome/fontawesome-svg-core'
import '@fortawesome/fontawesome-svg-core/styles.css'
import Layout from "../components/Layout";
import {AuthOption} from "../lib/auth";
import AccessDenied from "../components/errors/AccessDenied";
import AlreadyLoggedIn from "../components/errors/AlreadyLoggedIn";
import React from "react";
import Error from "../components/errors/Error";

config.autoAddCss = false

export default function App({
    Component,
    pageProps: { session, ...pageProps }
}) {
    return(
        <SessionProvider session={session}>
            <ErrorBoundary>
                { Component.auth ? (
                    <Auth config={Component.auth}>
                        <Component {...pageProps} />
                    </Auth>
                ) : (
                    <Component {...pageProps} />
                )}
            </ErrorBoundary>
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

    if (status === "authenticated" && config.auth === AuthOption.None) {
        return (<AlreadyLoggedIn />)
    }

    return children
}

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { error: null };
    }

    render() {
        // @ts-ignore
        if (this.state.error) {
            // @ts-ignore
            return (
                // @ts-ignore
                <Error errorMessage={this.state.error.message} />
            )
        }

        // @ts-ignore
        return this.props.children;
    }
}
