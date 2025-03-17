// app/auth/auth.guard.ts
import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
 import { Router } from '@angular/router';
import {AuthService} from './service/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log("🔑 Checking authGuard...");
  if (authService.isLoggedIn()) {
    console.log("✅ User is logged in. Access granted!");
    return true;
  }

  console.error("🚫 User is NOT logged in. Redirecting to /login");
  router.navigate(['/login']);
  return false;
};

