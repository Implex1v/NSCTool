import {useSession} from "next-auth/react";
import Layout from "../../components/Layout";
import Head from "next/head";
import useTranslation from "next-translate/useTranslation";
import {CharacterControllerApi, createConfiguration} from "../../lib/client";
import {useEffect, useState} from "react";
import {Table} from "react-bootstrap";
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUserPlus} from "@fortawesome/free-solid-svg-icons";
import CharacterRow from "../../components/characters/CharacterRow";

export default function Characters() {
    const { t } = useTranslation('common');
    const { data: session } = useSession()
    const [ characters, setCharacters ] = useState(null)
    const [ isLoading, setLoading ] = useState(false)

    useEffect(() => {
        setLoading(true)
        const config = createConfiguration()
        const api = new CharacterControllerApi(config)

        api
            .getAll()
            .then((data) => {
                setCharacters(data)
                setLoading(false)
            })
    }, [])
    if(isLoading) return null

    return (
        <Layout>
            <Head>
                <title>NSCTool Characters</title>
            </Head>
            <div className="container">
                <div className="col-md-12 text-center">
                    <h1>{t('characters')}</h1>
                </div>
                <div className="col-md-12">
                    <div className="float-end">
                        <Link href="/characters/new">
                            <button className="btn btn-success">
                                <FontAwesomeIcon icon={faUserPlus} />
                                {t('newCharacter')}
                            </button>
                        </Link>
                    </div>
                </div>
                <Table className="col-md-12 mt-4" bordered={false}>
                    <thead>
                        <tr>
                            <th className="text-white">{t("name")}</th>
                            <th className="text-white">{t("race")}</th>
                            <th className="text-white">{t("profession")}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {characters?.map(c => {
                            return (<CharacterRow character={c} />)
                        })}
                    </tbody>
                </Table>
            </div>
        </Layout>
    )
}