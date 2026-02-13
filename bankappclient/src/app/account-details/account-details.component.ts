import { Component } from '@angular/core';
import { Account } from '../model/account';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-account-details',
  standalone: true,
  imports: [],
  templateUrl: './account-details.component.html',
  styleUrl: './account-details.component.css'
})
export class AccountDetailsComponent {

  id: number = 0;
  account: Account = new Account();


  constructor(private route: ActivatedRoute,private accountService: AccountService) {}


  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.accountService.getAccountById(this.id).subscribe(data => {
      this.account = data;
    });
  }
}
