export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

export const ACCESS_TOKEN = "accessToken";

export const OAUTH2_REDIRECT_URI = process.env.REACT_APP_OAUTH2_REDIRECT_URI;

export const GOOGLE_AUTH_URL = API_BASE_URL + "/oauth2/authorization/google?redirect_uri=" + OAUTH2_REDIRECT_URI;

export const FACEBOOK_AUTH_URL = API_BASE_URL + "/oauth2/authorization/facebook?redirect_uri=" + OAUTH2_REDIRECT_URI;

