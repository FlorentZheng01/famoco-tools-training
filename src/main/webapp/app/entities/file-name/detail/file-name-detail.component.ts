import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFileName } from '../file-name.model';
import { DataUtils } from 'app/core/util/data-util.service';
import {BreakpointObserver} from "@angular/cdk/layout";
import {LoaderService} from "../../../loader/loader.service";

@Component({
  selector: 'jhi-file-name-detail',
  templateUrl: './file-name-detail.component.html',
})
export class FileNameDetailComponent implements OnInit {
  fileName: IFileName | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute, private breakpointObserver : BreakpointObserver, public loaderService: LoaderService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileName }) => {
      this.fileName = fileName;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
