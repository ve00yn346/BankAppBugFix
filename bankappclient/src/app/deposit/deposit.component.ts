import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { DepositRequest } from '../model/deposit-request';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './deposit.component.html',
  styles: ``
})
export class DepositComponent {
 accountId: number = 0;
  amount: number = 0;

  constructor(
    private accountService: AccountService,
    private router: Router
  ) {}

  onSubmit(): void {
    const request: DepositRequest = {
      accountId: this.accountId,
      amount: this.amount
    };

    this.accountService.deposit(request).subscribe(() => {
      this.router.navigate(['/accounts']);
    });
  }
  
}
