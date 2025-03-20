import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {AuthService} from './service/auth.service';
import {catchError, map, of} from 'rxjs';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);



  if (authService.isLoggedIn()) {
    return true; // Allow access if authenticated
  }
  // else
  //   console.log("Jwt expired");
  //   router.navigate(['/login']); // Redirect to login
  //   return false;

  // If the access token is expired, attempt to refresh it
  console.log("1")
  console.log(authService.isAccessTokenExpired())
  if (authService.isAccessTokenExpired()) {
    console.log(2)
    return authService.refreshToken().pipe(
      map((newToken) => {
        console.log('New access token generated:', newToken);
        return true; // Allow access after refreshing
      }),
      catchError((error) => {
        console.error('Failed to refresh token:', error);
        authService.logout(); // Logout if refresh fails
        router.navigate(['/login']); // Redirect to login
        return of(false);
      })
    );
  }

  // If the user is not logged in and the token is not expired, redirect to login
  console.log('JWT expired or invalid');
  router.navigate(['/login']);
  return false;

};
