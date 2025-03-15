// app/auth/auth.guard.ts
import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
 import { Router } from '@angular/router';
import {AuthService} from './service/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }
  router.navigate(['/login']);
  return false;
};
