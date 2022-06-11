import Layout from "./layout";
import {signIn} from "next-auth/react";
import useTranslation from "next-translate/useTranslation";

export default function AccessDenied() {
    const { t } = useTranslation('common');
    return (
        <Layout>
            <div className="h-fill row justify-content-center">
                <div className="col-md-4">
                    <h1>{t('accessDenied')}</h1>
                    <p>{t('accessDeniedText')}</p>
                    <button onClick={() => signIn("keycloak")} className="btn btn-primary btn-block w-100">Login</button>
                </div>
            </div>
        </Layout>
    )
}