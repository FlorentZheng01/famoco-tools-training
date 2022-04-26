import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetailer } from '../retailer.model';
import { RetailerService } from '../service/retailer.service';

@Component({
  templateUrl: './retailer-delete-dialog.component.html',
})
export class RetailerDeleteDialogComponent {
  retailer?: IRetailer;

  constructor(protected retailerService: RetailerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.retailerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
