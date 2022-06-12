import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUserPlus} from "@fortawesome/free-solid-svg-icons";
import useTranslation from "next-translate/useTranslation";
import Link from "next/link";

export default function CharacterAddButton() {
    const { t } = useTranslation('common');

    return (
        <Link href="/characters/new">
            <button className="btn btn-success">
                <FontAwesomeIcon icon={faUserPlus} />
                {t('newCharacter')}
            </button>
        </Link>
    )
}