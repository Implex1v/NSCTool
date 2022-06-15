import Layout from "../Layout";
import {signIn} from "next-auth/react";
import useTranslation from "next-translate/useTranslation";
import Image from "next/image";

export default function AccessDenied() {
    const { t } = useTranslation('common');
    return (
        <Layout>
            <div className="h-fill row justify-content-center">
                <div className="col-md-4">
                    <h1 className="text-center">{t('accessDenied')}</h1>
                    <p>{t('accessDeniedText')}</p>
                    <Image src="https://http.cat/401" className="col-md-12 mb-4" alt="meow"  />
                    <button onClick={() => signIn("keycloak")} className="btn btn-primary btn-block w-100">Login</button>
                </div>
            </div>
        </Layout>
    )
}