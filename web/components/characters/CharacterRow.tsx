import Link from "next/link";

export default function CharacterRow({ character }) {
    return (
        <tr className="text-white">
            <td>
                <Link href={"/characters/"+character.id}>
                    <a>
                        {character.name}
                    </a>
                </Link>
            </td>
            <td>{character.race}</td>
            <td>{character.profession}</td>
        </tr>
    )
}