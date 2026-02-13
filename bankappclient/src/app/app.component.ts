import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'bankappclient';
   constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  get isManager(): boolean {
    return this.authService.isManager();
  }


  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
