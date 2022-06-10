import NextAuth from "next-auth"
import {getEnvironmentData} from "worker_threads";
import KeycloakProvider from "next-auth/providers/keycloak";

export default NextAuth({
    // Configure one or more authentication providers
    debug: true,
    providers: [
        KeycloakProvider({
            id: "keycloak",
            name: "Keycloak",
            type: "oauth",
            wellKnown: `${process.env.APP_AUTH_URL}/realms/nsctool/.well-known/openid-configuration`,
            params: { grant_type: "password" },
            idToken: true,
            checks: ["pkce", "state"],
            authorization: { params: { scope: "openid email profile" } },
            clientId: process.env.APP_AUTH_CLIENT_ID,
            clientSecret: process.env.APP_AUTH_CLIENT_SECRET,
            profile(profile) {
                return {
                    id: profile.sub,
                    name: profile.name ?? profile.preferred_username,
                    email: profile.email,
                    image: profile.picture,
                }
            }
        })
    ],
    callbacks: {
        async jwt({ token, user, account, profile }) {
            // console.log("------jwt--------")
            // console.log(token)
            // console.log(account)
            // console.log(user)
            if(account) {
                token.accessToken = account.access_token
            }
            return token;
        },
        async session({ session, token }){
            // console.log("------session--------")
            // console.log(session)
            // console.log(token)
            session.user.accessToken = token.accessToken
            session.user.id = token.sub
            return session
        }
    },
    theme: {
        colorScheme: "dark",
    },
    session: {
        jwt: true
    },
})
