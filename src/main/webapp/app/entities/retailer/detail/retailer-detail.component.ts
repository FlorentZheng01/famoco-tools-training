import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRetailer } from '../retailer.model';

@Component({
  selector: 'jhi-retailer-detail',
  templateUrl: './retailer-detail.component.html',
})
export class RetailerDetailComponent implements OnInit {
  retailer: IRetailer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailer }) => {
      this.retailer = retailer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
