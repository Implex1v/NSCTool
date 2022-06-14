import Layout from "./Layout";
import useTranslation from "next-translate/useTranslation";

export default function Error({ errorMessageKey }) {
    const { t } = useTranslation("common")
    return (
        <Layout>
            <div className="container row justify-content-center">
                <div className="col-md-6">
                    <div className="alert alert-danger" role="alert">
                        <h4 className="alert-heading">Error</h4>
                        <p>{t(errorMessageKey)}</p>
                        <img src="https://http.cat/500" className="col-md-12" alt="meow" />
                    </div>
                </div>
            </div>
        </Layout>
    )
}