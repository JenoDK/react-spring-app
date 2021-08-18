import {ACCESS_TOKEN, API_BASE_URL} from "../constants/Constants";

/**
 * This function just calls a fetch with the given request options on the given url appended to the API base url like so
 * {API_BASE_URL} + url.
 * @param url
 * @param opts
 * @return The response promise or the rejected response when the status is not ok
 */
export function callApi(url: String, opts?: RequestInit) {
    if (API_BASE_URL === undefined) {
        return Promise.reject("API URL is undefined")
    }
    return fetch(API_BASE_URL + url, opts)
        .then(response => {
            if (!response.ok) {
                return Promise.reject(response);
            }
            return response;
        })
}

/**
 * Calls {callApi} but appends the token to header when the user is authenticated
 * @param url
 * @param options
 */
export function callProtectedApi(url: String, options?: RequestInit) {
    let accessToken = localStorage.getItem(ACCESS_TOKEN)
    if (!accessToken) {
        return Promise.reject("Unauthenticated: access token is not set")
    }
    let opts;
    if (options) {
        if (options.headers) {
            const requestHeaders: HeadersInit = new Headers(options.headers)
            requestHeaders.set("Authorization", 'Bearer ' + accessToken)
            options.headers = requestHeaders
        } else {
            options.headers = { Authorization: 'Bearer ' + accessToken }
        }
        opts = options
    } else {
        opts = {
            headers: { Authorization: 'Bearer ' + accessToken }
        };
    }
    return callApi(url, opts);
}

export interface LoginBody {
    email: string;
    password: string;
}

export interface SignupBody {
    username: string;
    email: string;
    password: string;
}

export function login(b: LoginBody) {
    return callApi(
        "/api/public/login",
        {
            headers: {
                'Content-Type': 'application/json',
            },
            method: 'POST', body: JSON.stringify(b)
        })
}

export function signup(b: SignupBody) {
    return callApi(
        "/api/public/signup",
        {
            headers: {
                'Content-Type': 'application/json',
            },
            method: 'POST', body: JSON.stringify(b)
        })
}

export function getCurrentUser() {
    return callProtectedApi(
        "/api/users/me",
        {
            headers: {
                'Content-Type': 'application/json',
            },
        })
}

export function defaultErrorLogging(error: any) {
    if (error instanceof Response) {
        error.text()
            .then((error: any) => console.log(error))
    } else {
        console.log(error)
    }
}