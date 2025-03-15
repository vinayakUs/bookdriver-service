// auth.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth/login';

  login(email: string,username:string,type:string, password: string): Observable<{ accessToken: string, expiryDuration: number }> {
    return this.http.post<{ accessToken: string, expiryDuration: number }>(this.apiUrl, { email,username,type, password })
      .pipe(
        tap(response => {
          console.log(response);
          return this.setSession(response.accessToken, response.expiryDuration)
          }

        )
      );
  }

  private setSession(token: string, expiresIn: number): void {
    const expiresAt = Date.now() + (expiresIn * 1000);
    localStorage.setItem('jwt_token', token);
    localStorage.setItem('jwt_expires_at', expiresAt.toString());
    console.log('Stored values:', {
      token: localStorage.getItem('jwt_token'),
      expiresAt: localStorage.getItem('jwt_expires_at')
    });

  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  isLoggedIn(): boolean {
    const expiration = localStorage.getItem('jwt_expires_at');
    return expiration ? Date.now() < parseInt(expiration) : false;
  }

  logout(): void {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('jwt_expires_at');
  }
}
