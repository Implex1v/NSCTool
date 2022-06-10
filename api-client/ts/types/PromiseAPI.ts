import { ResponseContext, RequestContext, HttpFile } from '../http/http';
import * as models from '../models/all';
import { Configuration} from '../configuration'

import { Character } from '../models/Character';
import { CreateUserRequest } from '../models/CreateUserRequest';
import { User } from '../models/User';
import { ObservableCharacterControllerApi } from './ObservableAPI';

import { CharacterControllerApiRequestFactory, CharacterControllerApiResponseProcessor} from "../apis/CharacterControllerApi";
export class PromiseCharacterControllerApi {
    private api: ObservableCharacterControllerApi

    public constructor(
        configuration: Configuration,
        requestFactory?: CharacterControllerApiRequestFactory,
        responseProcessor?: CharacterControllerApiResponseProcessor
    ) {
        this.api = new ObservableCharacterControllerApi(configuration, requestFactory, responseProcessor);
    }

    /**
     * @param character 
     */
    public create1(character: Character, _options?: Configuration): Promise<Character> {
        const result = this.api.create1(character, _options);
        return result.toPromise();
    }

    /**
     * @param uuid 
     */
    public delete1(uuid: string, _options?: Configuration): Promise<void> {
        const result = this.api.delete1(uuid, _options);
        return result.toPromise();
    }

    /**
     */
    public getAll(_options?: Configuration): Promise<any> {
        const result = this.api.getAll(_options);
        return result.toPromise();
    }

    /**
     * @param uuid 
     */
    public getById(uuid: string, _options?: Configuration): Promise<Character> {
        const result = this.api.getById(uuid, _options);
        return result.toPromise();
    }


}



import { ObservableHealthControllerApi } from './ObservableAPI';

import { HealthControllerApiRequestFactory, HealthControllerApiResponseProcessor} from "../apis/HealthControllerApi";
export class PromiseHealthControllerApi {
    private api: ObservableHealthControllerApi

    public constructor(
        configuration: Configuration,
        requestFactory?: HealthControllerApiRequestFactory,
        responseProcessor?: HealthControllerApiResponseProcessor
    ) {
        this.api = new ObservableHealthControllerApi(configuration, requestFactory, responseProcessor);
    }

    /**
     */
    public health(_options?: Configuration): Promise<{ [key: string]: string; }> {
        const result = this.api.health(_options);
        return result.toPromise();
    }


}



import { ObservableUserControllerApi } from './ObservableAPI';

import { UserControllerApiRequestFactory, UserControllerApiResponseProcessor} from "../apis/UserControllerApi";
export class PromiseUserControllerApi {
    private api: ObservableUserControllerApi

    public constructor(
        configuration: Configuration,
        requestFactory?: UserControllerApiRequestFactory,
        responseProcessor?: UserControllerApiResponseProcessor
    ) {
        this.api = new ObservableUserControllerApi(configuration, requestFactory, responseProcessor);
    }

    /**
     * @param id 
     */
    public _delete(id: string, _options?: Configuration): Promise<void> {
        const result = this.api._delete(id, _options);
        return result.toPromise();
    }

    /**
     * @param createUserRequest 
     */
    public create(createUserRequest: CreateUserRequest, _options?: Configuration): Promise<User> {
        const result = this.api.create(createUserRequest, _options);
        return result.toPromise();
    }


}



