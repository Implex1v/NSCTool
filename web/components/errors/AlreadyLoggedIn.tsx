import Layout from "../Layout";
import Link from "next/link";
import useTranslation from "next-translate/useTranslation";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser} from "@fortawesome/free-solid-svg-icons";

export default function AlreadyLoggedIn() {
    const { t } = useTranslation('common')
    return (
        <Layout>
            <div className="container d-flex justify-content-center">
                <div className="col-md-4 alert alert-info">
                    <div>
                        <h4 className="alert-heading">{t('alreadyLoggedIn_title')}</h4>
                        <p>{t('alreadyLoggedIn_text')}</p>
                    </div>
                    <Link href="/pages/profile">
                        <a className="btn btn-primary w-100">
                            <FontAwesomeIcon icon={faUser} />
                            {t('profile')}
                        </a>
                    </Link>
                </div>
            </div>
        </Layout>
    )
}