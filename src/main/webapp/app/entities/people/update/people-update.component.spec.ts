import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PeopleService } from '../service/people.service';
import { IPeople, People } from '../people.model';

import { PeopleUpdateComponent } from './people-update.component';

describe('People Management Update Component', () => {
  let comp: PeopleUpdateComponent;
  let fixture: ComponentFixture<PeopleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peopleService: PeopleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PeopleUpdateComponent],
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
      .overrideTemplate(PeopleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeopleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peopleService = TestBed.inject(PeopleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const people: IPeople = { id: 456 };

      activatedRoute.data = of({ people });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(people));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<People>>();
      const people = { id: 123 };
      jest.spyOn(peopleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ people });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: people }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(peopleService.update).toHaveBeenCalledWith(people);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<People>>();
      const people = new People();
      jest.spyOn(peopleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ people });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: people }));
      saveSubject.complete();

      // THEN
      expect(peopleService.create).toHaveBeenCalledWith(people);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<People>>();
      const people = { id: 123 };
      jest.spyOn(peopleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ people });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peopleService.update).toHaveBeenCalledWith(people);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
