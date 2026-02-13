import { Component } from '@angular/core';
import { Account } from '../model/account';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-account',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-account.component.html',
  styleUrl: './create-account.component.css'
})
export class CreateAccountComponent {

  account: Account = new Account();


  constructor(private accountService: AccountService,private router: Router) {}

  onSubmit(): void {
    this.saveAccount();
  }

  saveAccount(): void {
    this.accountService.createAccount(this.account).subscribe(() => {
      this.goToAccountList();
    });
  }

  goToAccountList(): void {
    this.router.navigate(['/accounts']);
  }
}
