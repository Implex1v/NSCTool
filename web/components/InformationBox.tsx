import useTranslation from "next-translate/useTranslation";

export default function InformationBox({ titleKey, textKey, button }) {
    const { t } = useTranslation("common")
    return (
        <div className="container d-flex justify-content-center">
            <div className="col-md-4">
                <div className="alert alert-info">
                    <h4 className="alert-heading">{t(titleKey)}</h4>
                    <p>{t(textKey)}</p>
                </div>
            </div>
            {button ? button : ""}
        </div>
    )
}