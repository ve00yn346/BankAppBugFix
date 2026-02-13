import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AccountService } from '../services/account.service';
import { PendingTransfer } from '../model/pending-transfer';

@Component({
  selector: 'app-transfer-approvals',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './transfer-approvals.component.html',
  styles: ``
})
export class TransferApprovalsComponent implements OnInit {
  pendingTransfers: PendingTransfer[] = [];
  errorMessage = '';

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.loadPendingTransfers();
  }

  loadPendingTransfers(): void {
    this.accountService.getPendingTransfers().subscribe({
      next: (response) => {
        this.pendingTransfers = response;
      },
      error: () => {
        this.errorMessage = 'Unable to fetch pending transfers';
      }
    });
  }

  approve(requestId: number): void {
    this.accountService.approveTransfer(requestId).subscribe({
      next: () => this.loadPendingTransfers(),
      error: (err) => {
        this.errorMessage = err?.error?.error || 'Approval failed';
      }
    });
  }
}
