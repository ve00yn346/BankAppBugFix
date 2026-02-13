import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authUrl = 'http://localhost:8090/bankapp/authenticate';

  constructor(private http: HttpClient) {}

  authenticate(username: string, password: string): Observable<any> {
    this.clearSession();

    return this.http.post<any>(this.authUrl, { username, password }).pipe(
      tap({
        next: response => {
          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('authenticatedUser', username);
        },
        error: () => this.clearSession()
      })
    );
  }

  isUserLoggedIn(): boolean {
    return sessionStorage.getItem('token') !== null;
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  logout(): void {
    this.clearSession();
  }

  getUserRoles(): string[] {
    const token = this.getToken();
    if (!token) {
      return [];
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return Array.isArray(payload.roles) ? payload.roles : [];
    } catch {
      return [];
    }
  }

  isManager(): boolean {
    return this.getUserRoles().includes('ROLE_MGR');
  }

  private clearSession(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('authenticatedUser');
  }
}
