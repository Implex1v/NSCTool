import {useSession} from "next-auth/react";
import Layout from "../components/layout";
import Head from "next/head";
import AccessDenied from "../components/unauthenticated";

export default function Profile() {
    const { data: session, status } = useSession()
    const loading = status === "loading"

    if(typeof window !== "undefined" && loading) return null

    if(!session) {
        return (
            <AccessDenied />
        )
    }

    return (
        <Layout title="">
            <Head>
                <title>Profile - { session.user.name}</title>
            </Head>
        </Layout>
    )
}