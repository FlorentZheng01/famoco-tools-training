import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountry, Country } from '../country.model';
import { CountryService } from '../service/country.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import {LoaderService} from "../../../loader/loader.service";

@Component({
  selector: 'jhi-country-update',
  templateUrl: './country-update.component.html',
})
export class CountryUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPeople[] = [];
  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    people: [],
    personne: [],
  });

  constructor(
    protected countryService: CountryService,
    protected peopleService: PeopleService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    public loaderService: LoaderService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ country }) => {
      this.updateForm(country);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const country = this.createFromForm();
    if (country.id !== undefined) {
      this.subscribeToSaveResponse(this.countryService.update(country));
    } else {
      this.subscribeToSaveResponse(this.countryService.create(country));
    }
  }

  trackPeopleById(_index: number, item: IPeople): number {
    return item.id!;
  }

  trackPersonneById(_index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountry>>): void {
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

  protected updateForm(country: ICountry): void {
    this.editForm.patchValue({
      id: country.id,
      description: country.description,
      people: country.people,
      personne: country.personne,
    });

    this.peopleCollection = this.peopleService.addPeopleToCollectionIfMissing(this.peopleCollection, country.people);
    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      country.personne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.peopleService
      .query({ filter: 'country-is-null' })
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(map((people: IPeople[]) => this.peopleService.addPeopleToCollectionIfMissing(people, this.editForm.get('people')!.value)))
      .subscribe((people: IPeople[]) => (this.peopleCollection = people));

    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing(personnes, this.editForm.get('personne')!.value)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }

  protected createFromForm(): ICountry {
    return {
      ...new Country(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      people: this.editForm.get(['people'])!.value,
      personne: this.editForm.get(['personne'])!.value,
    };
  }
}
