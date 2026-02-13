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
    const body = {
      username: username,
      password: password
    };

    return this.http.post<any>(this.authUrl, body).pipe(
      tap(response => {
        // assuming backend returns { token: "..." }
        sessionStorage.setItem('token', response.token);
        sessionStorage.setItem('authenticatedUser', username);
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
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('authenticatedUser');
  }
}
