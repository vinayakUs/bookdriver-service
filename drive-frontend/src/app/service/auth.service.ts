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
      }
      )
    );
  }
// {
//   "username": "sa",
//   "email": "radhikaandrew12@gmail.com",
//   "password": "sa",
//   "registerAsAdmin": "true"
// }
  register(data:{username:string,email:string,password:string,registerAsAdmin:boolean}):Observable<any>{

    return this.http.post<{}>(
      `${this.apiUrl}/register`, data
    ).pipe(
      tap(value => {

      })
    )
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
  isAccessTokenExpired(): boolean {
    const token = localStorage.getItem(this.accessTokenKey);
    if (!token) return true; // No token found

    const expiration = this.getTokenExpiration(token);
    return !expiration || expiration <= new Date(); // Token is expired if expiration is in the past
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
  // Parse the token and check if it's expired
  isTokenExpired(token: string): boolean {
    try {
      // Split the token into its parts (header, payload, signature)
      const payloadBase64 = token.split('.')[1];
      if (!payloadBase64) {
        throw new Error('Invalid token format');
      }

      // Decode the base64 payload
      const payloadJson = atob(payloadBase64);
      const payload = JSON.parse(payloadJson);

      // Check the expiration time (exp is in seconds)
      const currentTime = Date.now() / 1000; // Convert to seconds
      return payload.exp < currentTime; // Token is expired if exp < current time
    } catch (error) {
      console.error('Error parsing token:', error);
      return true; // Assume token is invalid if parsing fails
    }
  }
  refreshToken(): Observable<any> {
    console.log(3)
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
    return this.http.post<{ refreshToken: string , accessToken:string }>(
      `${this.apiUrl}/refresh`, { refreshToken }
    ).pipe(
      tap(response => {
        this.storeTokens(response.accessToken,response.refreshToken);
        this.refreshTokenSubject.next(response.accessToken);
      }),
      catchError((error: HttpErrorResponse) => {
        this.logout();
        return throwError(() => new Error('Refresh token expired'));
      }),
      tap(() => this.isRefreshing = false)
    );
  }
}
