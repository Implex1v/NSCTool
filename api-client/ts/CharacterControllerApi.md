# .CharacterControllerApi

All URIs are relative to *http://localhost:7090*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create1**](CharacterControllerApi.md#create1) | **POST** /characters | 
[**delete1**](CharacterControllerApi.md#delete1) | **DELETE** /characters/{uuid} | 
[**getAll**](CharacterControllerApi.md#getAll) | **GET** /characters | 
[**getById**](CharacterControllerApi.md#getById) | **GET** /characters/{uuid} | 


# **create1**
> Character create1(character)


### Example


```typescript
import {  } from '';
import * as fs from 'fs';

const configuration = .createConfiguration();
const apiInstance = new .CharacterControllerApi(configuration);

let body:.CharacterControllerApiCreate1Request = {
  // Character
  character: {
    id: "id_example",
    name: "name_example",
    description: "description_example",
    image: [
      'YQ==',
    ],
    race: "race_example",
    profession: "profession_example",
    tags: [
      "tags_example",
    ],
  },
};

apiInstance.create1(body).then((data:any) => {
  console.log('API called successfully. Returned data: ' + data);
}).catch((error:any) => console.error(error));
```


### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **character** | **Character**|  |


### Return type

**Character**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](README.md#documentation-for-api-endpoints) [[Back to Model list]](README.md#documentation-for-models) [[Back to README]](README.md)

# **delete1**
> void delete1()


### Example


```typescript
import {  } from '';
import * as fs from 'fs';

const configuration = .createConfiguration();
const apiInstance = new .CharacterControllerApi(configuration);

let body:.CharacterControllerApiDelete1Request = {
  // string
  uuid: "uuid_example",
};

apiInstance.delete1(body).then((data:any) => {
  console.log('API called successfully. Returned data: ' + data);
}).catch((error:any) => console.error(error));
```


### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uuid** | [**string**] |  | defaults to undefined


### Return type

**void**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](README.md#documentation-for-api-endpoints) [[Back to Model list]](README.md#documentation-for-models) [[Back to README]](README.md)

# **getAll**
> any getAll()


### Example


```typescript
import {  } from '';
import * as fs from 'fs';

const configuration = .createConfiguration();
const apiInstance = new .CharacterControllerApi(configuration);

let body:any = {};

apiInstance.getAll(body).then((data:any) => {
  console.log('API called successfully. Returned data: ' + data);
}).catch((error:any) => console.error(error));
```


### Parameters
This endpoint does not need any parameter.


### Return type

**any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](README.md#documentation-for-api-endpoints) [[Back to Model list]](README.md#documentation-for-models) [[Back to README]](README.md)

# **getById**
> Character getById()


### Example


```typescript
import {  } from '';
import * as fs from 'fs';

const configuration = .createConfiguration();
const apiInstance = new .CharacterControllerApi(configuration);

let body:.CharacterControllerApiGetByIdRequest = {
  // string
  uuid: "uuid_example",
};

apiInstance.getById(body).then((data:any) => {
  console.log('API called successfully. Returned data: ' + data);
}).catch((error:any) => console.error(error));
```


### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uuid** | [**string**] |  | defaults to undefined


### Return type

**Character**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](README.md#documentation-for-api-endpoints) [[Back to Model list]](README.md#documentation-for-models) [[Back to README]](README.md)


