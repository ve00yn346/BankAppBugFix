import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'bankappclient';
   constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}


  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
