import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { DepositRequest } from '../model/deposit-request';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './deposit.component.html',
  styles: ``
})
export class DepositComponent {
  accountId: number | null = null;
  amount: number | null = null;
  errorMessage = '';

  constructor(
    private accountService: AccountService,
    private router: Router
  ) {}

  onSubmit(form: NgForm): void {
    this.errorMessage = '';

    if (form.invalid || this.accountId === null || this.amount === null) {
      form.control.markAllAsTouched();
      return;
    }

    const request: DepositRequest = {
      accountId: this.accountId,
      amount: this.amount
    };

    this.accountService.deposit(request).subscribe({
      next: () => this.router.navigate(['/accounts']),
      error: (err) => {
        this.errorMessage = err?.error?.error || err?.error?.amount || err?.error?.accountId || 'Deposit failed';
      }
    });
  }
}
