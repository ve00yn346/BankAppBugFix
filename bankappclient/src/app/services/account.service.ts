import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Account } from '../model/account';
import { DepositRequest } from '../model/deposit-request';
import { WithdrawRequest } from '../model/withdraw-request';
import { TransferRequest } from '../model/transfer-request';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private baseUrl = 'http://localhost:8090/bankapp/v1/accounts';
   private baseUrl2 ='http://localhost:8090/bankapp/v1/transactions'

  constructor(private http: HttpClient) { }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.baseUrl);
  }

  createAccount(account: Account) {
    return this.http.post(this.baseUrl, account);
  }
  getAccountById(id: number) {
    return this.http.get<Account>(`${this.baseUrl}/${id}`);
  }
  updateAccount(id: number, account: Account) {
    return this.http.put(`${this.baseUrl}/${id}`, account);
  }
  deleteAccount(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  deposit(request: DepositRequest) {
    return this.http.put<void>(`${this.baseUrl2}/deposit`, request);
  }

  withdraw(request: WithdrawRequest) {
  return this.http.put<void>(`${this.baseUrl2}/withdraw`, request);
}

  transfer(request: TransferRequest) {
    return this.http.put<void>(`${this.baseUrl2}/transfer`, request);
  }

}
