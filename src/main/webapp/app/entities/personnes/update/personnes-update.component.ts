import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPersonnes, Personnes } from '../personnes.model';
import { PersonnesService } from '../service/personnes.service';

@Component({
  selector: 'jhi-personnes-update',
  templateUrl: './personnes-update.component.html',
})
export class PersonnesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    test: [],
  });

  constructor(protected personnesService: PersonnesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personnes }) => {
      this.updateForm(personnes);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personnes = this.createFromForm();
    if (personnes.id !== undefined) {
      this.subscribeToSaveResponse(this.personnesService.update(personnes));
    } else {
      this.subscribeToSaveResponse(this.personnesService.create(personnes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonnes>>): void {
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

  protected updateForm(personnes: IPersonnes): void {
    this.editForm.patchValue({
      id: personnes.id,
      test: personnes.test,
    });
  }

  protected createFromForm(): IPersonnes {
    return {
      ...new Personnes(),
      id: this.editForm.get(['id'])!.value,
      test: this.editForm.get(['test'])!.value,
    };
  }
}
