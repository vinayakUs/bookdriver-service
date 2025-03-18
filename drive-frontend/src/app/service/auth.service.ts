import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {Observable, BehaviorSubject, throwError, switchMap, catchError} from 'rxjs';
import { tap } from 'rxjs/operators';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private accessTokenKey = 'access_token';
  private refreshTokenKey = 'refresh_token';
  private isRefreshing = false;

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();
  private refreshTokenSubject = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<{ accessToken: string; refreshToken: string }>(
      `${this.apiUrl}/login`, credentials
    ).pipe(
      tap(tokens => {
        this.storeTokens(tokens.accessToken, tokens.refreshToken);
      })
    );
  }

  private storeTokens(accessToken: string, refreshToken: string) {
    localStorage.setItem(this.accessTokenKey, accessToken);
    localStorage.setItem(this.refreshTokenKey, refreshToken);
    this.isAuthenticatedSubject.next(true);
  }

  logout() {
    localStorage.removeItem(this.accessTokenKey);
    localStorage.removeItem(this.refreshTokenKey);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.accessTokenKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshTokenKey);
  }
  isLoggedIn(): boolean {
    const token = localStorage.getItem(this.accessTokenKey); // Returns true if token exists
     // = this.getToken();
    if (!token) return false;

    const expiration = this.getTokenExpiration(token);
    return !!expiration && expiration > new Date();



  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.accessTokenKey);
  }
  // Get token expiration date
  private getTokenExpiration(token: string): Date | null {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload.exp) {
        return new Date(payload.exp * 1000); // Convert to milliseconds
      }
      return null;
    } catch (e) {
      return null;
    }
  }

  refreshToken(): Observable<any> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('No refresh token available'));
    }

    if (this.isRefreshing) {
      return this.refreshTokenSubject.pipe(
        switchMap((newToken) => {
          return newToken ? throwError(() => new Error('Token refreshed')) : throwError(() => new Error('Token refresh failed'));
        })
      );
    }

    this.isRefreshing = true;
    return this.http.post<{ accessToken: string; refreshToken: string }>(
      `${this.apiUrl}/refresh-token`, { refreshToken }
    ).pipe(
      tap(tokens => {
        this.storeTokens(tokens.accessToken, tokens.refreshToken);
        this.refreshTokenSubject.next(tokens.accessToken);
      }),
      catchError((error: HttpErrorResponse) => {
        this.logout();
        return throwError(() => new Error('Refresh token expired'));
      }),
      tap(() => this.isRefreshing = false)
    );
  }
}
