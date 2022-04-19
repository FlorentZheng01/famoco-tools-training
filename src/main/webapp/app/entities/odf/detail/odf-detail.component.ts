import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IODF } from '../odf.model';

@Component({
  selector: 'jhi-odf-detail',
  templateUrl: './odf-detail.component.html',
})
export class ODFDetailComponent implements OnInit {
  oDF: IODF | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oDF }) => {
      this.oDF = oDF;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
