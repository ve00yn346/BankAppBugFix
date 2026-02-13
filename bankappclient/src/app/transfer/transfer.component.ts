import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { TransferRequest } from '../model/transfer-request';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './transfer.component.html',
  styles: ``
})
export class TransferComponent {
 fromAccountId: number = 0;
  toAccountId: number = 0;
  amount: number = 0;


  constructor(
    private accountService: AccountService,
    private router: Router
  ) {}


  onSubmit(): void {
    const request: TransferRequest = {
      fromAccountId: this.fromAccountId,
      toAccountId: this.toAccountId,
      amount: this.amount
    };

  this.accountService.transfer(request).subscribe({
    next: () => this.router.navigate(['/accounts']),
    error: (err) => {
      if (err.status === 403) {
        alert(err.error?.error || 'Access denied');
      }
    }
  });
  
    // this.accountService.transfer(request).subscribe(() => {
    //   this.router.navigate(['/accounts']);
    // });


  }
}
