import {useSession} from "next-auth/react";
import Layout from "../components/layout";
import Head from "next/head";
import useTranslation from "next-translate/useTranslation";
import AccessDenied from "../components/unauthenticated";

export default function Profile() {
    const { t } = useTranslation('common');
    const { data: session, status } = useSession()
    const loading = status === "loading"

    if(typeof window !== "undefined" && loading) return null

    if(!session) {
        return (
            <AccessDenied />
        )
    }

    return (
        <Layout>
            <Head>
                <title>{t('profile')}: { session.user.name}</title>
            </Head>
        </Layout>
    )
}