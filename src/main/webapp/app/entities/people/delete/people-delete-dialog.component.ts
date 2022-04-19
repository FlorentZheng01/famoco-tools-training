import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeople } from '../people.model';
import { PeopleService } from '../service/people.service';

@Component({
  templateUrl: './people-delete-dialog.component.html',
})
export class PeopleDeleteDialogComponent {
  people?: IPeople;

  constructor(protected peopleService: PeopleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.peopleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
