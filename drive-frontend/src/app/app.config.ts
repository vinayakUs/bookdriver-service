import { ApplicationConfig, provideZoneChangeDetection, inject } from '@angular/core';
import {provideRouter, withComponentInputBinding} from '@angular/router';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {routes} from './app.routes';
import {authInterceptor} from './interceptor/auth.interceptor';
import {MessageService} from 'primeng/api';
import {provideAnimations} from '@angular/platform-browser/animations';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {providePrimeNG} from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { provideApollo } from 'apollo-angular';
import { HttpLink } from 'apollo-angular/http';
import { InMemoryCache } from '@apollo/client/core';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes) ,MessageService,provideAnimations(),
    provideHttpClient(withInterceptors([authInterceptor])) ,
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Aura
      }
    }), provideHttpClient(), provideApollo(() => {
      const httpLink = inject(HttpLink);

      return {
        link: httpLink.create({
          uri: '<%= endpoint %>',
        }),
        cache: new InMemoryCache(),
      };
    }), provideHttpClient(), provideApollo(() => {
      const httpLink = inject(HttpLink);

      return {
        link: httpLink.create({
          uri: '<%= endpoint %>',
        }),
        cache: new InMemoryCache(),
      };
    }), provideHttpClient(), provideApollo(() => {
      const httpLink = inject(HttpLink);

      return {
        link: httpLink.create({
          uri: '<%= endpoint %>',
        }),
        cache: new InMemoryCache(),
      };
    })
  ]
};
