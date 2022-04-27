import {AfterViewInit, Component, ViewChild} from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import {debounceTime, delay, merge, startWith, switchMap} from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetailer } from '../retailer.model';

import { ASC, DESC} from 'app/config/pagination.constants';
import { RetailerService } from '../service/retailer.service';
import { RetailerDeleteDialogComponent } from '../delete/retailer-delete-dialog.component';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'jhi-retailer',
  templateUrl: './retailer.component.html',
})
export class RetailerComponent implements AfterViewInit {
  retailers: IRetailer[] = [];
  isLoading = false;
  totalItems = 0;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  displayedColumns: string[] = ['id','name','address','mail','view','edit','delete'];

  @ViewChild(MatPaginator) paginator?: MatPaginator;
  @ViewChild(MatSort) sort?: MatSort;


  constructor(
    protected retailerService: RetailerService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngAfterViewInit(): void {
    this.loadPage();
    this.sort!.sortChange.subscribe(() => (this.paginator!.pageIndex = 0));
    merge(this.sort!.sortChange, this.paginator!.page)
      .pipe(
        startWith({}),debounceTime(600),
        switchMap(() => {
          this.isLoading = true;
          return this.retailerService.query({
            page: this.paginator!.pageIndex,
            size: this.paginator!.pageSize,
            sort: this.sortTable(),
          })
        }),
        delay(800)).subscribe({
      next: (res: HttpResponse<IRetailer[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body as IRetailer[], res.headers);
      },
    });
  }

  loadPage(): void {
    this.isLoading = true;
    this.retailerService
      .query({
        page: this.paginator!.pageIndex,
        size: this.paginator!.pageSize,
        sort: this.sortTable(),
      }).pipe(delay(800))
      .subscribe({
        next: (res: HttpResponse<IRetailer[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body as IRetailer[], res.headers);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  trackId(_index: number, item: IRetailer): number {
    return item.id!;
  }

  delete(retailer: IRetailer): void {
    const modalRef = this.modalService.open(RetailerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.retailer = retailer;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sortTable(): string[] {
    const result = [this.sort!.active + ',' + (this.sort!.direction ? ASC : DESC)];
    if (this.sort!.active !== 'id') {
      result.push('id');
    }
    return result;
  }



  protected onSuccess(retailer: IRetailer[] | undefined, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.retailers = retailer as IRetailer[];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
