import { Component } from '@angular/core';
import { Account } from '../model/account';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-account',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-account.component.html',
  styles: ``
})
export class UpdateAccountComponent {

  id: number = 0;
  account: Account = new Account();


  constructor(private route: ActivatedRoute,private accountService: AccountService, private router: Router) {}


  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.accountService.getAccountById(this.id).subscribe(data => {
      this.account = data;
    });
  }

  onSubmit(): void {
    this.accountService.updateAccount(this.id, this.account).subscribe(() => {
      this.goToAccountList();
    });
  }

  goToAccountList(): void {
    this.router.navigate(['/accounts']);
  }
}
