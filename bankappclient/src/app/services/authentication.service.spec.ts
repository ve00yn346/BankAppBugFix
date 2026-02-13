import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AuthenticationService } from './authentication.service';
import { provideHttpClient } from '@angular/common/http';

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    sessionStorage.clear();

    TestBed.configureTestingModule({
      providers: [
        AuthenticationService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(AuthenticationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    sessionStorage.clear();
  });

  it('should replace old session token on re-login', () => {
    sessionStorage.setItem('token', 'stale-token');
    sessionStorage.setItem('authenticatedUser', 'old-user');

    service.authenticate('raj', 'raj123').subscribe();

    const request = httpMock.expectOne('http://localhost:8090/bankapp/authenticate');
    expect(request.request.method).toBe('POST');
    expect(sessionStorage.getItem('token')).toBeNull();

    request.flush({ token: 'fresh-token' });

    expect(sessionStorage.getItem('token')).toBe('fresh-token');
    expect(sessionStorage.getItem('authenticatedUser')).toBe('raj');
  });

  it('should clear session after failed login', () => {
    sessionStorage.setItem('token', 'stale-token');
    sessionStorage.setItem('authenticatedUser', 'old-user');

    service.authenticate('ekta', 'wrong-pass').subscribe({
      error: () => undefined
    });

    const request = httpMock.expectOne('http://localhost:8090/bankapp/authenticate');
    request.flush({ message: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });

    expect(sessionStorage.getItem('token')).toBeNull();
    expect(sessionStorage.getItem('authenticatedUser')).toBeNull();
  });
});
