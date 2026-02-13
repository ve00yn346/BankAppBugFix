import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { WithdrawRequest } from '../model/withdraw-request';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-withdraw',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './withdraw.component.html',
  styles: ``
})
export class WithdrawComponent {
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

    const request: WithdrawRequest = {
      accountId: this.accountId,
      amount: this.amount
    };

    this.accountService.withdraw(request).subscribe({
      next: () => this.router.navigate(['/accounts']),
      error: (err) => {
        this.errorMessage = err?.error?.error || err?.error?.amount || err?.error?.accountId || 'Withdrawal failed';
      }
    });
  }
}
