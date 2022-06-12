import NextAuth from "next-auth"
import {getEnvironmentData} from "worker_threads";
import KeycloakProvider from "next-auth/providers/keycloak";
import * as jose from "jose";

const URL = `${process.env.APP_AUTH_URL}/realms/nsctool/.well-known/openid-configuration`
const CLIENT_ID = process.env.APP_AUTH_CLIENT_ID
const CLIENT_SECRET = process.env.APP_AUTH_CLIENT_SECRET

export default NextAuth({
    // Configure one or more authentication providers
    debug: true,
    providers: [
        KeycloakProvider({
            id: "keycloak",
            name: "Keycloak",
            type: "oauth",
            wellKnown: URL,
            params: { grant_type: "password" },
            idToken: true,
            checks: ["pkce", "state"],
            authorization: { params: { scope: "openid email profile" } },
            clientId: CLIENT_ID,
            clientSecret: CLIENT_SECRET,
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
            // console.log(profile)

            if(account) {
                token.refreshToken = account.refresh_token
                token.accessToken = account.access_token
                // Sadly we have to extract the expiry date out of the jwt since the account.exp is 1 month in the future no one know why -.-''
                token.exp = account.expires_at
                token.roles = await getRoles(account.access_token)
            }

            token.exp = await getExp(token.accessToken)
            console.log(Date.now() / 1000)
            console.log(token.exp)
            if(Date.now() / 1000 >= token.exp) {
                token = refreshAccessToken(token)
            }

            return token;
        },
        async session({ session, token }){
            // console.log("------session--------")
            // console.log(session)
            // console.log(token)
            session.user.accessToken = token.accessToken
            session.user.id = token.sub
            session.error = token.error
            session.roles = token.roles
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

// https://next-auth.js.org/tutorials/refresh-token-rotation
async function refreshAccessToken(token) {
    try {
        const configRequest = await fetch(URL)
        const config = await configRequest.json()
        const tokenUrl = config.token_endpoint

        const body = new URLSearchParams({
                client_id: CLIENT_ID,
                client_secret: CLIENT_SECRET,
                refresh_token: token.refreshToken,
                grant_type: "refresh_token"
            })

        const response = await fetch(tokenUrl, {
            body: body,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            method: "POST",
        })

        const newToken = await response.json()
        if(!response.ok) {
            throw newToken
        }

        return {
            ...token,
            accessToken: newToken.access_token,
            refreshToken: newToken.refresh_token ?? token.refreshToken,
            exp: await getExp(newToken.access_token),
            iat: await getIat(newToken.access_token),
            roles: await getRoles(newToken.access_token),
        }
    } catch (e) {
        console.log(e)

        return {
            ...token,
            error: "RefreshAccessTokenError"
        }
    }
}

async function getExp(jwtString) {
    const jwt = jose.decodeJwt(jwtString)
    return jwt.exp
}

async function getIat(jwtString) {
    const jwt = jose.decodeJwt(jwtString)
    return jwt.iat
}

/**
 * Returns the roles contained in the given jwt with the jsonpath of `realm_access.roles`.
 * @param jwtString The jwt as encoded string
 * @returns {Promise<[]|*|Array<{id: string, name: string}>|*[]>}
 */
async function getRoles(jwtString) {
    const jwt = jose.decodeJwt(jwtString)
    if(!jwt.hasOwnProperty("realm_access")) {
        return []
    }

    const realmAccess = jwt.realm_access
    if(!realmAccess.hasOwnProperty("roles")) {
        return []
    }

    return realmAccess.roles;
}
