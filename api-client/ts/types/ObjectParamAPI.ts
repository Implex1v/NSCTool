import { ResponseContext, RequestContext, HttpFile } from '../http/http';
import * as models from '../models/all';
import { Configuration} from '../configuration'

import { Character } from '../models/Character';
import { CreateUserRequest } from '../models/CreateUserRequest';
import { User } from '../models/User';

import { ObservableCharacterControllerApi } from "./ObservableAPI";
import { CharacterControllerApiRequestFactory, CharacterControllerApiResponseProcessor} from "../apis/CharacterControllerApi";

export interface CharacterControllerApiCreate1Request {
    /**
     * 
     * @type Character
     * @memberof CharacterControllerApicreate1
     */
    character: Character
}

export interface CharacterControllerApiDelete1Request {
    /**
     * 
     * @type string
     * @memberof CharacterControllerApidelete1
     */
    uuid: string
}

export interface CharacterControllerApiGetAllRequest {
}

export interface CharacterControllerApiGetByIdRequest {
    /**
     * 
     * @type string
     * @memberof CharacterControllerApigetById
     */
    uuid: string
}

export class ObjectCharacterControllerApi {
    private api: ObservableCharacterControllerApi

    public constructor(configuration: Configuration, requestFactory?: CharacterControllerApiRequestFactory, responseProcessor?: CharacterControllerApiResponseProcessor) {
        this.api = new ObservableCharacterControllerApi(configuration, requestFactory, responseProcessor);
    }

    /**
     * @param param the request object
     */
    public create1(param: CharacterControllerApiCreate1Request, options?: Configuration): Promise<Character> {
        return this.api.create1(param.character,  options).toPromise();
    }

    /**
     * @param param the request object
     */
    public delete1(param: CharacterControllerApiDelete1Request, options?: Configuration): Promise<void> {
        return this.api.delete1(param.uuid,  options).toPromise();
    }

    /**
     * @param param the request object
     */
    public getAll(param: CharacterControllerApiGetAllRequest = {}, options?: Configuration): Promise<any> {
        return this.api.getAll( options).toPromise();
    }

    /**
     * @param param the request object
     */
    public getById(param: CharacterControllerApiGetByIdRequest, options?: Configuration): Promise<Character> {
        return this.api.getById(param.uuid,  options).toPromise();
    }

}

import { ObservableHealthControllerApi } from "./ObservableAPI";
import { HealthControllerApiRequestFactory, HealthControllerApiResponseProcessor} from "../apis/HealthControllerApi";

export interface HealthControllerApiHealthRequest {
}

export class ObjectHealthControllerApi {
    private api: ObservableHealthControllerApi

    public constructor(configuration: Configuration, requestFactory?: HealthControllerApiRequestFactory, responseProcessor?: HealthControllerApiResponseProcessor) {
        this.api = new ObservableHealthControllerApi(configuration, requestFactory, responseProcessor);
    }

    /**
     * @param param the request object
     */
    public health(param: HealthControllerApiHealthRequest = {}, options?: Configuration): Promise<{ [key: string]: string; }> {
        return this.api.health( options).toPromise();
    }

}

import { ObservableUserControllerApi } from "./ObservableAPI";
import { UserControllerApiRequestFactory, UserControllerApiResponseProcessor} from "../apis/UserControllerApi";

export interface UserControllerApiDeleteRequest {
    /**
     * 
     * @type string
     * @memberof UserControllerApi_delete
     */
    id: string
}

export interface UserControllerApiCreateRequest {
    /**
     * 
     * @type CreateUserRequest
     * @memberof UserControllerApicreate
     */
    createUserRequest: CreateUserRequest
}

export class ObjectUserControllerApi {
    private api: ObservableUserControllerApi

    public constructor(configuration: Configuration, requestFactory?: UserControllerApiRequestFactory, responseProcessor?: UserControllerApiResponseProcessor) {
        this.api = new ObservableUserControllerApi(configuration, requestFactory, responseProcessor);
    }

    /**
     * @param param the request object
     */
    public _delete(param: UserControllerApiDeleteRequest, options?: Configuration): Promise<void> {
        return this.api._delete(param.id,  options).toPromise();
    }

    /**
     * @param param the request object
     */
    public create(param: UserControllerApiCreateRequest, options?: Configuration): Promise<User> {
        return this.api.create(param.createUserRequest,  options).toPromise();
    }

}
