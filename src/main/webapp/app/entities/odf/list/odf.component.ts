import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IODF } from '../odf.model';
import { ODFService } from '../service/odf.service';
import { ODFDeleteDialogComponent } from '../delete/odf-delete-dialog.component';

@Component({
  selector: 'jhi-odf',
  templateUrl: './odf.component.html',
})
export class ODFComponent implements OnInit {
  oDFS?: IODF[];
  isLoading = false;

  constructor(protected oDFService: ODFService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.oDFService.query().subscribe({
      next: (res: HttpResponse<IODF[]>) => {
        this.isLoading = false;
        this.oDFS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IODF): string {
    return item.id!;
  }

  delete(oDF: IODF): void {
    const modalRef = this.modalService.open(ODFDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.oDF = oDF;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
