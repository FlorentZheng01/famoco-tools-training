import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDevice } from '../device.model';

import { ASC, DESC } from 'app/config/pagination.constants';
import { DeviceService } from '../service/device.service';
import { DeviceDeleteDialogComponent } from '../delete/device-delete-dialog.component';
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {debounceTime, delay, merge, startWith, switchMap} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {filter} from "rxjs/operators";

@Component({
  selector: 'jhi-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device-list.css']
})
export class DeviceComponent implements AfterViewInit{
  devices: IDevice[] = [];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = 10;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  searchForm = this.fb.group({
    searchFilter: "",
  })
  displayedColumns: string[] = ['id','famocoId','macAddress','dateCreation','view','edit','delete'];

  @ViewChild(MatPaginator) paginator?: MatPaginator;
  @ViewChild(MatSort) sort?: MatSort;





  constructor(
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    private fb: FormBuilder,
  ) {
  }


  loadPage(): void {
    this.isLoading = true;
    this.deviceService
      .query({
        page: this.paginator!.pageIndex,
        size: this.paginator!.pageSize,
        sort: this.sortTable(),
        search: this.searchForm.get('searchFilter')!.value ? this.searchForm.get('searchFilter')!.value: "",
      }).pipe(delay(800))
      .subscribe({
        next: (res: HttpResponse<IDevice[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body as IDevice[], res.headers);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  applyFilter(): void {
    this.sort!.sortChange.subscribe(() => (this.paginator!.pageIndex = 0));
    merge(this.sort!.sortChange, this.paginator!.page)
      .pipe(

        startWith({}),debounceTime(600),
        switchMap(() => {
          this.isLoading = true;
          return this.deviceService.query({
            page: this.paginator!.pageIndex,
            size: this.paginator!.pageSize,
            sort: this.sortTable(),
            search: this.searchForm.get('searchFilter')!.value ? this.searchForm.get('searchFilter')!.value: "",
          })

        }),
        delay(800)).subscribe({
      next: (res: HttpResponse<IDevice[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body as IDevice[], res.headers);
      },
    });

  }


  ngAfterViewInit(): void {

    this.sort!.sortChange.subscribe(() => (this.paginator!.pageIndex = 0));
    this.loadPage();
    merge(this.sort!.sortChange, this.paginator!.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoading = true;
          return this.deviceService.query({
            page: this.paginator!.pageIndex,
            size: this.paginator!.pageSize,
            sort: this.sortTable(),
            search: this.searchForm.get('searchFilter')!.value ? this.searchForm.get('searchFilter')!.value: "",
          })

        }),
      delay(800)).subscribe({
      next: (res: HttpResponse<IDevice[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body as IDevice[], res.headers);
      },
    });



  }


  trackId(_index: number, item: IDevice): number {
    return item.id!;
  }

  delete(device: IDevice): void {
    const modalRef = this.modalService.open(DeviceDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.device = device;
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


  protected onSuccess(devices: IDevice[] | undefined, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.devices = devices as IDevice[];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }



}