// auth.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth/login';

  login(email: string,username:string,type:string, password: string): Observable<{ accessToken: string, expiryDuration: number,refreshToken:string,refreshExpiryDuration:number }> {
    return this.http.post<{  accessToken: string, expiryDuration: number,refreshToken:string,refreshExpiryDuration:number  }>(this.apiUrl, { email,username,type, password })
      .pipe(
        tap(response => {
          return this.setSession(response.accessToken, response.expiryDuration , response.refreshToken,response.refreshExpiryDuration)
          }

        )
      );
  }

  private setSession(token: string, expiresIn: number,refresh_token:string,refresh_exp:number): void {
    const expiresAt = Date.now() + (expiresIn * 1000);
    localStorage.setItem('jwt_token', token);
    localStorage.setItem('jwt_expires_at', expiresAt.toString());
    localStorage.setItem('jwt_refresh_token', refresh_token);
    localStorage.setItem('jwt_refresh_expiry_at' , refresh_exp.toString())

    console.log('Stored values:', {
      token: localStorage.getItem('jwt_token'),
      expiresAt: localStorage.getItem('jwt_expires_at'),
      jwt_refresh_token: localStorage.getItem('jwt_refresh_token'),
      jwt_refresh_expiry_at: localStorage.getItem('jwt_refresh_expiry_at')
    });

  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) {
      console.error("🚫 No token found. User is not logged in.");
      return false;
    }

    try {
      // Decode the token
      const payload = JSON.parse(atob(token.split('.')[1])); // Decode base64 payload
      const expiresAt = payload.exp * 1000; // Convert to milliseconds

      console.log(`🔍 Token Expiration Time: ${expiresAt}, Current Time: ${Date.now()}`);

      if (Date.now() < expiresAt) {
        console.log("✅ Token is valid. User is logged in.");
        return true;
      } else {
        console.error("🚫 Token has expired. User is logged out.");
        return false;
      }
    } catch (error) {
      console.error("❌ Error decoding token:", error);
      return false;
    }
  }

  // isLoggedIn(): boolean {
  //   const expiration = localStorage.getItem('jwt_expires_at');
  //   return expiration ? Date.now() < parseInt(expiration) : false;
  // }


  logout(): void {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('jwt_expires_at');
    localStorage.removeItem('jwt_refresh_expiry_at');
    localStorage.removeItem('jwt_refresh_token');
  }
}
