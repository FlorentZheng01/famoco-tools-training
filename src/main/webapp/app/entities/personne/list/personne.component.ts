import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { IPersonne } from '../personne.model';
import { PersonneService } from '../service/personne.service';

@Component({
  selector: 'jhi-personne',
  templateUrl: './personne.component.html',
})
export class PersonneComponent implements OnInit {
  personnes?: IPersonne[];
  isLoading = false;

  constructor(protected personneService: PersonneService) {}

  loadAll(): void {
    this.isLoading = true;

    this.personneService.query().subscribe({
      next: (res: HttpResponse<IPersonne[]>) => {
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

  trackId(_index: number, item: IPersonne): number {
    return item.id!;
  }
}
