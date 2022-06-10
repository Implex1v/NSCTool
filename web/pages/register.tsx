import {useSession} from "next-auth/react";
import Profile from "./profile";

export default function Register() {
    const { data: session, status } = useSession()
    const loading = status === "loading"

    if(typeof window !== "undefined" && loading) return null

    if(session) {
        return (
            <Profile />
        )
    }

    return (
        <div>
            <h1>TODO</h1>
        </div>
    )
}