// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  api_url: 'https://localhost:8081/api/',
  auth_url: 'https://localhost:8081/auth/',
  auth: {
    clientID: 'y7wlc77pA9sceFHv7839BOMNCuw243uA',
    domain: 'dev-lsmn3kc2.eu.auth0.com',
    audience: 'https://localhost:8081',
    redirect: 'https://localhost:4201/homepage',
    logout: 'https://localhost:4201/homepage',
    scope: 'openid profile'
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
