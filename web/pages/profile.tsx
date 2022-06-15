import {useSession} from "next-auth/react";
import Layout from "../components/Layout";
import Head from "next/head";
import useTranslation from "next-translate/useTranslation";
import AccessDenied from "../components/errors/AccessDenied";
import CharacterTable from "../components/characters/CharacterTable";
import {Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import {createApiClient} from "../lib/ApiClient";
import {AuthOption} from "../lib/auth";

export default function Profile() {
    const { t } = useTranslation('common');
    const { data: session, status } = useSession()
    const [ characters, setCharacters ] = useState(null)
    const [ isLoading, setLoading ] = useState(false)

    useEffect(() => {
        setLoading(true);
        createApiClient(session, status)
            .characterApi()
            .getAll()
            .then((data) => {
                setCharacters(data)
                setLoading(false)
            })
    }, [status, session])

    if(!session) {
        return (
            <AccessDenied />
        )
    }

    if (isLoading) {
        return (<p>Loading</p>)
    }

    return (
        <Layout>
            <Head>
                <title>{t('profile')}: { session.user.name }</title>
            </Head>
            <div className="container">
                <h2>{t('profile')}: { session.user.name }</h2>
                <div className="row pt-5">
                    <div className="col-md-6"></div>
                    <div className="col-md-6">
                        <Table>
                            <tbody>
                                <tr className="profile-row">
                                    <td>Id</td>
                                    <td>{ session.user.id }</td>
                                </tr>
                                <tr className="profile-row">
                                    <td>{t('name')}</td>
                                    <td>{ session.user.name }</td>
                                </tr>
                                <tr className="profile-row">
                                    <td>{t('email')}</td>
                                    <td>{ session.user.email }</td>
                                </tr>
                            </tbody>
                        </Table>
                    </div>
                    <div className="col-md-12">
                        <div className="pt-5">
                            <h4>{t('myCharacters')}</h4>
                        </div>
                        <CharacterTable characters={characters} />
                    </div>
                </div>
            </div>
        </Layout>
    )
}

Profile.auth = {
    auth: AuthOption.Required,
}
