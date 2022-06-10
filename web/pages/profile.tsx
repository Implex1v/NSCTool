import {useSession} from "next-auth/react";
import Layout from "../components/layout";
import Head from "next/head";
import AccessDenied from "../components/unauthenticated";
import useTranslation from "next-translate/useTranslation";

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
        <Layout title="">
            <Head>
                <title>{t('profile')}: { session.user.name}</title>
            </Head>
        </Layout>
    )
}