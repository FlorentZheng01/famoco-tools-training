import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonnesService } from '../service/personnes.service';
import { IPersonnes, Personnes } from '../personnes.model';

import { PersonnesUpdateComponent } from './personnes-update.component';

describe('Personnes Management Update Component', () => {
  let comp: PersonnesUpdateComponent;
  let fixture: ComponentFixture<PersonnesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personnesService: PersonnesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonnesUpdateComponent],
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
      .overrideTemplate(PersonnesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonnesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personnesService = TestBed.inject(PersonnesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const personnes: IPersonnes = { id: 456 };

      activatedRoute.data = of({ personnes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(personnes));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Personnes>>();
      const personnes = { id: 123 };
      jest.spyOn(personnesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personnes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(personnesService.update).toHaveBeenCalledWith(personnes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Personnes>>();
      const personnes = new Personnes();
      jest.spyOn(personnesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personnes }));
      saveSubject.complete();

      // THEN
      expect(personnesService.create).toHaveBeenCalledWith(personnes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Personnes>>();
      const personnes = { id: 123 };
      jest.spyOn(personnesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personnesService.update).toHaveBeenCalledWith(personnes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
