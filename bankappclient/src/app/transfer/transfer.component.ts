import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { TransferRequest } from '../model/transfer-request';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transfer.component.html',
  styles: ``
})
export class TransferComponent {
  fromAccountId: number | null = null;
  toAccountId: number | null = null;
  amount: number | null = null;
  errorMessage = '';
  infoMessage = '';

  constructor(
    private accountService: AccountService,
    private router: Router
  ) {}

  onSubmit(form: NgForm): void {
    this.errorMessage = '';
    this.infoMessage = '';

    if (form.invalid || this.fromAccountId === null || this.toAccountId === null || this.amount === null) {
      form.control.markAllAsTouched();
      return;
    }

    const request: TransferRequest = {
      fromAccountId: this.fromAccountId,
      toAccountId: this.toAccountId,
      amount: this.amount
    };

    this.accountService.transfer(request).subscribe({
      next: (response) => {
        if (response.status === 'PENDING_APPROVAL') {
          this.infoMessage = `${response.message} Request Id: ${response.requestId}`;
          form.resetForm();
          return;
        }
        this.router.navigate(['/accounts']);
      },
      error: (err) => {
        this.errorMessage = err?.error?.error || err?.error?.amount || err?.error?.fromAccountId || 'Transfer failed';
      }
    });
  }
}
