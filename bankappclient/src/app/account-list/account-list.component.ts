import { Component } from '@angular/core';
import { Account } from '../model/account';
import { CommonModule } from '@angular/common';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.css'
})
export class AccountListComponent {

  constructor(private accountService: AccountService,
    private router: Router
  ) { }
  ngOnInit(): void {
    this.getAccounts();
  }
  accounts: Account[] = [];

  getAccounts(): void {
    this.accountService.getAllAccounts().subscribe(data => {
      this.accounts = data;
    });
  }

  accountDetails(id: number): void {
    this.router.navigate(['account-details', id]);
  }
  updateAccount(id: number): void {
    this.router.navigate(['update-account', id]);
  }
  deleteAccount(id: number): void {
    this.accountService.deleteAccount(id).subscribe(() => {
      this.getAccounts();
    });
  }
}
