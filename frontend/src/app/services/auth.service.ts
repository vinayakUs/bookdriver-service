// // import { Injectable, inject } from '@angular/core';
// // import { HttpClient, HttpErrorResponse } from '@angular/common/http';
// // import { Router } from '@angular/router';
// // import { JwtHelperService } from '@auth0/angular-jwt';
// // import { catchError, Observable, throwError, tap } from 'rxjs';
//
// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {
//   private apiUrl = 'http://localhost:8080/auth/login';
//   private tokenKey = 'jwt_token';
//   private jwtHelper = new JwtHelperService();
//   private http = inject(HttpClient);
//   private router = inject(Router);
//
//   login(credentials: { username: string; password: string }): Observable<any> {
//     return this.http.post<{ token: string }>(this.apiUrl, credentials).pipe(
//       tap(response => {
//         localStorage.setItem(this.tokenKey, response.token);
//       }),
//       catchError(this.handleError)
//     );
//   }
//
//   logout(): void {
//     localStorage.removeItem(this.tokenKey);
//     this.router.navigate(['/login']);
//   }
//
//   isAuthenticated(): boolean {
//     const token = localStorage.getItem(this.tokenKey);
//     return token ? !this.jwtHelper.isTokenExpired(token) : false;
//   }
//
//   getToken(): string | null {
//     return localStorage.getItem(this.tokenKey);
//   }
//
//   private handleError(error: HttpErrorResponse): Observable<never> {
//     let errorMessage = 'An unknown error occurred!';
//     if (error.error?.error) {
//       errorMessage = error.error.error;
//     } else if (error.status === 401) {
//       errorMessage = 'Invalid credentials';
//     } else if (error.status === 500) {
//       errorMessage = 'Server error. Please try again later.';
//     }
//     return throwError(() => new Error(errorMessage));
//   }
// }
