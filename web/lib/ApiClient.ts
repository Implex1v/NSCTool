import {
    CharacterControllerApi,
    createConfiguration,
    Middleware, RequestContext, ResponseContext, ServerConfiguration, UserControllerApi
} from "./client";
import {ConfigurationParameters} from "./client/configuration";
import {PromiseMiddlewareWrapper} from "./client/middleware";
import {useSession} from "next-auth/react";

export class ApiClient {
    protected readonly baseUri= "http://localhost:8080"
    protected config: ConfigurationParameters

    public constructor() {
        this.config = {
            baseServer: new ServerConfiguration(this.baseUri, null),
        }
    }

    characterApi() {
        return new CharacterControllerApi(createConfiguration(this.config));
    }

    userApi() {
        return new UserControllerApi(createConfiguration(this.config))
    }
}

export class AuthenticatedApiClient extends ApiClient {
    public constructor(jwt: string) {
        super()
        this.config = {
            baseServer: new ServerConfiguration(this.baseUri, null),
            middleware: [
                new PromiseMiddlewareWrapper(new BearerTokenMiddleware(jwt))
            ]
        }
    }
}

export function createApiClient(session, status): ApiClient {
    if(session) {
        return new AuthenticatedApiClient(session.user.accessToken)
    }

    return new ApiClient();
}

class BearerTokenMiddleware implements Middleware {
    private readonly jwt: string

    constructor(jwt: string) {
        this.jwt = jwt
    }

    post(context: ResponseContext): Promise<ResponseContext> {
        return Promise.resolve(context);
    }

    pre(context: RequestContext): Promise<RequestContext> {
        context.setHeaderParam("Authorization", `Bearer ${this.jwt}`)

        return Promise.resolve(context);
    }
}