import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {delay} from 'rxjs';

import {IFileName} from '../file-name.model';
import {FileNameService} from '../service/file-name.service';
import {FileNameDeleteDialogComponent} from '../delete/file-name-delete-dialog.component';
import {DataUtils} from 'app/core/util/data-util.service';
import {LoaderService} from "../../../loader/loader.service";
import {MatTableDataSource} from "@angular/material/table";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {MatSort, Sort} from "@angular/material/sort";

@Component({
  selector: 'jhi-file-name',
  templateUrl: './file-name.component.html',
})
export class FileNameComponent implements OnInit, AfterViewInit {
  fileNames?: IFileName[];
  isLoading = false;
  dataSource = new MatTableDataSource(this.fileNames);


  displayedColumns: string[] = ['id', 'name', 'description', 'look', 'edit', 'delete'];
  @ViewChild(MatSort) sort?: MatSort;


  constructor(
    protected fileNameService: FileNameService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    public loaderService: LoaderService,
    private _liveAnnouncer: LiveAnnouncer,
  ) {

  }


  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort as MatSort;
  }

  loadAll(): void {
    this.isLoading = true;
    this.fileNameService.query(
    ).pipe(delay(800)).subscribe({
      next: (res: HttpResponse<IFileName[]>) => {
        this.isLoading = false;
        this.fileNames = res.body ?? [];
        this.dataSource = new MatTableDataSource(this.fileNames)
        this.dataSource.sort = this.sort as MatSort;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IFileName): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(fileName: IFileName): void {
    const modalRef = this.modalService.open(FileNameDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.fileName = fileName;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  announceSortChange(sortState: Sort): void {
// eslint-disable-next-line no-console
    console.log(sortState);
    this.dataSource.data = this.dataSource.sortData(this.dataSource.data,this.sort as MatSort);
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }



  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

  }


}
