import {Table} from "react-bootstrap";
import CharacterRow from "./CharacterRow";
import useTranslation from "next-translate/useTranslation";

export default function CharacterTable({characters}) {
    const { t } = useTranslation('common');

    return (
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
                return (<CharacterRow character={c} key={c.id} />)
            })}
            </tbody>
        </Table>
    )
}