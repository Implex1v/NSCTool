import {useSession} from "next-auth/react";
import Layout from "../../components/Layout";
import Head from "next/head";
import CharacterTable from "../../components/characters/CharacterTable";
import useTranslation from "next-translate/useTranslation";
import {useEffect, useState} from "react";
import CharacterAddButton from "../../components/characters/CharacterAddButton";
import {createApiClient} from "../../lib/ApiClient";

export default function Characters() {
    const { data: session, status } = useSession()
    const { t } = useTranslation('common');
    const [ characters, setCharacters ] = useState(null)
    const [ _, setLoading ] = useState(false)

    useEffect(() => {
        if(status === "loading") {
            return
        }

        setLoading(true);
        createApiClient(session, status)
            .characterApi()
            .getAll()
            .then((data) => {
                setCharacters(data)
                setLoading(false)
            })
    }, [status, session])

    return (
        <Layout>
            <div>
                <Head>
                    <title>NSCTool Characters</title>
                </Head>
                <div className="container">
                    <div className="col-md-12 text-center">
                        <h1>{t('characters')}</h1>
                    </div>
                    <div className="col-md-12">
                        <div className="float-end">
                            <CharacterAddButton />
                        </div>
                    </div>
                    <CharacterTable characters={characters} />
                </div>
            </div>
        </Layout>
    )
}