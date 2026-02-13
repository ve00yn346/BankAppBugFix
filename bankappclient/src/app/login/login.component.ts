import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styles: ``
})
export class LoginComponent {

  username: string = '';
  password: string = '';
  error: string = '';

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

onSubmit(): void {
  this.authService
    .authenticate(this.username, this.password)
    .subscribe({
      next: () => this.router.navigate(['/accounts']),
      error: () => this.error = 'Invalid username or password'
    });
}
}
