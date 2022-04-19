import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonnes } from '../personnes.model';

@Component({
  selector: 'jhi-personnes-detail',
  templateUrl: './personnes-detail.component.html',
})
export class PersonnesDetailComponent implements OnInit {
  personnes: IPersonnes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personnes }) => {
      this.personnes = personnes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
