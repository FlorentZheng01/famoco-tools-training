import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeople } from '../people.model';
import { PeopleService } from '../service/people.service';
import { PeopleDeleteDialogComponent } from '../delete/people-delete-dialog.component';

@Component({
  selector: 'jhi-people',
  templateUrl: './people.component.html',
})
export class PeopleComponent implements OnInit {
  people?: IPeople[];
  isLoading = false;

  constructor(protected peopleService: PeopleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.peopleService.query().subscribe({
      next: (res: HttpResponse<IPeople[]>) => {
        this.isLoading = false;
        this.people = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPeople): number {
    return item.id!;
  }

  delete(people: IPeople): void {
    const modalRef = this.modalService.open(PeopleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.people = people;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
