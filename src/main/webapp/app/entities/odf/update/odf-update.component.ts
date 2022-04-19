import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IODF, ODF } from '../odf.model';
import { ODFService } from '../service/odf.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { IJobHistory } from 'app/entities/job-history/job-history.model';
import { JobHistoryService } from 'app/entities/job-history/service/job-history.service';

@Component({
  selector: 'jhi-odf-update',
  templateUrl: './odf-update.component.html',
})
export class ODFUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPeople[] = [];
  jobHistoriesCollection: IJobHistory[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    operationName: [],
    dateofcreation: [],
    dateofmodification: [],
    lastmodificationuser: [],
    people: [],
    jobHistory: [],
  });

  constructor(
    protected oDFService: ODFService,
    protected peopleService: PeopleService,
    protected jobHistoryService: JobHistoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oDF }) => {
      this.updateForm(oDF);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const oDF = this.createFromForm();
    if (oDF.id !== undefined) {
      this.subscribeToSaveResponse(this.oDFService.update(oDF));
    } else {
      this.subscribeToSaveResponse(this.oDFService.create(oDF));
    }
  }

  trackPeopleById(_index: number, item: IPeople): number {
    return item.id!;
  }

  trackJobHistoryById(_index: number, item: IJobHistory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IODF>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(oDF: IODF): void {
    this.editForm.patchValue({
      id: oDF.id,
      status: oDF.status,
      operationName: oDF.operationName,
      dateofcreation: oDF.dateofcreation,
      dateofmodification: oDF.dateofmodification,
      lastmodificationuser: oDF.lastmodificationuser,
      people: oDF.people,
      jobHistory: oDF.jobHistory,
    });

    this.peopleCollection = this.peopleService.addPeopleToCollectionIfMissing(this.peopleCollection, oDF.people);
    this.jobHistoriesCollection = this.jobHistoryService.addJobHistoryToCollectionIfMissing(this.jobHistoriesCollection, oDF.jobHistory);
  }

  protected loadRelationshipsOptions(): void {
    this.peopleService
      .query({ filter: 'odf-is-null' })
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(map((people: IPeople[]) => this.peopleService.addPeopleToCollectionIfMissing(people, this.editForm.get('people')!.value)))
      .subscribe((people: IPeople[]) => (this.peopleCollection = people));

    this.jobHistoryService
      .query({ filter: 'odf-is-null' })
      .pipe(map((res: HttpResponse<IJobHistory[]>) => res.body ?? []))
      .pipe(
        map((jobHistories: IJobHistory[]) =>
          this.jobHistoryService.addJobHistoryToCollectionIfMissing(jobHistories, this.editForm.get('jobHistory')!.value)
        )
      )
      .subscribe((jobHistories: IJobHistory[]) => (this.jobHistoriesCollection = jobHistories));
  }

  protected createFromForm(): IODF {
    return {
      ...new ODF(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      operationName: this.editForm.get(['operationName'])!.value,
      dateofcreation: this.editForm.get(['dateofcreation'])!.value,
      dateofmodification: this.editForm.get(['dateofmodification'])!.value,
      lastmodificationuser: this.editForm.get(['lastmodificationuser'])!.value,
      people: this.editForm.get(['people'])!.value,
      jobHistory: this.editForm.get(['jobHistory'])!.value,
    };
  }
}
