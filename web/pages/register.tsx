import {AuthOption} from "../lib/auth";
import Layout from "../components/Layout";
import {faUser, faEnvelope, faKey} from "@fortawesome/free-solid-svg-icons";
import {useState} from "react";
import {createApiClient} from "../lib/ApiClient";
import {CreateUserRequest} from "../lib/client";
import InformationBox from "../components/InformationBox";
import {Input, InputEmail, InputPassword, InputText, InputType} from "../components/forms/Input";
import Error from "../components/errors/Error";
import useTranslation from "next-translate/useTranslation";

export default function Register() {
    const { t } = useTranslation("common")
    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState(null)
    const [created, setCreated] = useState(false)

    function register(e) {
        e.preventDefault()

        const user = new CreateUserRequest()
        user.username = username
        user.password = password
        user.email = email

        try {
            createApiClient(null, null)
                .userApi()
                .create(user)
                .catch((e) => { console.log(e); setError(e) })
                .then(() => setCreated(true))
        } catch (e) {
            setError(e)
        }
    }

    if (error) {
        return (
            <Error errorMessageKey="register_error" />
        )
    }

    if (created) {
        return (
            <Layout>
                <div className="container d-flex justify-content-center">
                    <InformationBox titleKey="success" textKey="userCreated_text" button={null} />
                </div>
            </Layout>
        )
    }

    return (
        <Layout>
            <h2 className="text-center mb-5">Registrieren</h2>
            <div className="container d-flex justify-content-center">
                <form className="col-md-4" onSubmit={(e) => register(e)}>
                    <div className="mb-4">
                        <InputText icon={faUser} setValue={setUsername} label={t("register_username")} required={true} />
                    </div>
                    <div className="mb-4">
                        <InputEmail icon={faEnvelope} setValue={setEmail} label={t("register_email")} required={true} />
                    </div>
                    <div className="mb-5">
                        <InputPassword icon={faKey} label={t("register_password")} required={true} setValue={setPassword}/>
                    </div>
                    <button type="submit" className="btn btn-primary w-100">
                        {t("register_submit")}
                    </button>
                </form>
            </div>
        </Layout>
    )
}

Register.auth = {
    auth: AuthOption.None
}