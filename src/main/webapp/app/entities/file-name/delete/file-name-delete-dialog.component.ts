import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileName } from '../file-name.model';
import { FileNameService } from '../service/file-name.service';

@Component({
  templateUrl: './file-name-delete-dialog.component.html',
})
export class FileNameDeleteDialogComponent {
  fileName?: IFileName;

  constructor(protected fileNameService: FileNameService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileNameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
