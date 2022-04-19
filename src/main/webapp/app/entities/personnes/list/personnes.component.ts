import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonnes } from '../personnes.model';
import { PersonnesService } from '../service/personnes.service';
import { PersonnesDeleteDialogComponent } from '../delete/personnes-delete-dialog.component';

@Component({
  selector: 'jhi-personnes',
  templateUrl: './personnes.component.html',
})
export class PersonnesComponent implements OnInit {
  personnes?: IPersonnes[];
  isLoading = false;

  constructor(protected personnesService: PersonnesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personnesService.query().subscribe({
      next: (res: HttpResponse<IPersonnes[]>) => {
        this.isLoading = false;
        this.personnes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPersonnes): number {
    return item.id!;
  }

  delete(personnes: IPersonnes): void {
    const modalRef = this.modalService.open(PersonnesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personnes = personnes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
