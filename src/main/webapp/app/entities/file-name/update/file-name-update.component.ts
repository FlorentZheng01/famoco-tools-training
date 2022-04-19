import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import {delay, Observable} from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFileName, FileName } from '../file-name.model';
import { FileNameService } from '../service/file-name.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import {LoaderService} from "../../../loader/loader.service";

@Component({
  selector: 'jhi-file-name-update',
  templateUrl: './file-name-update.component.html',
})
export class FileNameUpdateComponent implements OnInit {
  isSaving = false;
  loading = false;
  isAnError = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    content: [],
    contentContentType: [],
    description: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileNameService: FileNameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    public loaderService: LoaderService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileName }) => {
      this.updateForm(fileName);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('myFirstJhipsterApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.loading = true;
    this.isAnError = false;
    const fileName = this.createFromForm();
    if (fileName.id !== undefined) {
      this.subscribeToSaveResponse(this.fileNameService.update(fileName));
    } else {
      this.subscribeToSaveResponse(this.fileNameService.create(fileName));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileName>>): void {
    result.pipe(delay(800),finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.loading = false;
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
    this.loading = false;
    this.isAnError = true;

  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fileName: IFileName): void {
    this.editForm.patchValue({
      id: fileName.id,
      name: fileName.name,
      content: fileName.content,
      contentContentType: fileName.contentContentType,
      description: fileName.description,
    });
  }

  protected createFromForm(): IFileName {
    return {
      ...new FileName(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contentContentType: this.editForm.get(['contentContentType'])!.value,
      content: this.editForm.get(['content'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
