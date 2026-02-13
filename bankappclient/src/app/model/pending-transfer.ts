export interface PendingTransfer {
  requestId: number;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  initiatedBy: string;
}
