import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import {provideRouter, withComponentInputBinding} from '@angular/router';

 import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {authInterceptor} from './auth.interceptor';
import {routes} from './app.routes';

// export const appConfig: ApplicationConfig = {
//   providers: [, provideRouter(routes),provideHttpClient()]
// };

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
};
