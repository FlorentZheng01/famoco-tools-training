import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CountryService } from '../service/country.service';
import { ICountry, Country } from '../country.model';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { CountryUpdateComponent } from './country-update.component';

describe('Country Management Update Component', () => {
  let comp: CountryUpdateComponent;
  let fixture: ComponentFixture<CountryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countryService: CountryService;
  let peopleService: PeopleService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CountryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CountryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countryService = TestBed.inject(CountryService);
    peopleService = TestBed.inject(PeopleService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call people query and add missing value', () => {
      const country: ICountry = { id: 456 };
      const people: IPeople = { id: 94047 };
      country.people = people;

      const peopleCollection: IPeople[] = [{ id: 89261 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const expectedCollection: IPeople[] = [people, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(peopleCollection, people);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call Personne query and add missing value', () => {
      const country: ICountry = { id: 456 };
      const personne: IPersonne = { id: 60993 };
      country.personne = personne;

      const personneCollection: IPersonne[] = [{ id: 56511 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const country: ICountry = { id: 456 };
      const people: IPeople = { id: 43640 };
      country.people = people;
      const personne: IPersonne = { id: 32752 };
      country.personne = personne;

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(country));
      expect(comp.peopleCollection).toContain(people);
      expect(comp.personnesSharedCollection).toContain(personne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = { id: 123 };
      jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: country }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countryService.update).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = new Country();
      jest.spyOn(countryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: country }));
      saveSubject.complete();

      // THEN
      expect(countryService.create).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = { id: 123 };
      jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countryService.update).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPeopleById', () => {
      it('Should return tracked People primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeopleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPersonneById', () => {
      it('Should return tracked Personne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
