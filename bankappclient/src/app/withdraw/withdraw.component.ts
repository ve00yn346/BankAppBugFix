import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { WithdrawRequest } from '../model/withdraw-request';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-withdraw',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './withdraw.component.html',
  styles: ``
})
export class WithdrawComponent {

  accountId: number = 0;
  amount: number = 0;


  constructor(
    private accountService: AccountService,
    private router: Router
  ) {}


  onSubmit(form: NgForm): void {
    if (form.invalid) {
      return;
    }

    const request: WithdrawRequest = {
      accountId: this.accountId,
      amount: this.amount
    };


    this.accountService.withdraw(request).subscribe(() => {
      this.router.navigate(['/accounts']);
    });
  }
}
