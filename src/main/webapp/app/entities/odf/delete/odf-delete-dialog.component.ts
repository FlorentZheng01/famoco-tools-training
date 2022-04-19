import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IODF } from '../odf.model';
import { ODFService } from '../service/odf.service';

@Component({
  templateUrl: './odf-delete-dialog.component.html',
})
export class ODFDeleteDialogComponent {
  oDF?: IODF;

  constructor(protected oDFService: ODFService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.oDFService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
