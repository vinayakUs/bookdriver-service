import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import {AuthService} from '../service/auth.service';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const authService = inject(AuthService);
  let token = authService.getAccessToken();

  // ✅ Skip token for login & register requests
  if (req.url.includes('/api/auth/login') || req.url.includes('/api/auth/register')) {
    return next(req);
  }

  // ✅ Attach token if available
  let clonedRequest = req;
  if (token) {
    clonedRequest = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(clonedRequest).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) { // Token expired
        return authService.refreshToken().pipe(
          switchMap(() => {
            token = authService.getAccessToken(); // Get new token after refresh
            if (token) {
              clonedRequest = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
              });
            }
            return next(clonedRequest);
          }),
          catchError(() => {
            authService.logout(); // Logout if refresh token fails
            return throwError(() => new Error('Session expired. Please log in again.'));
          })
        );
      }
      return throwError(() => error);
    })
  );
};
