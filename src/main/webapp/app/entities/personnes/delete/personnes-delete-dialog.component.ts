import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonnes } from '../personnes.model';
import { PersonnesService } from '../service/personnes.service';

@Component({
  templateUrl: './personnes-delete-dialog.component.html',
})
export class PersonnesDeleteDialogComponent {
  personnes?: IPersonnes;

  constructor(protected personnesService: PersonnesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personnesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
