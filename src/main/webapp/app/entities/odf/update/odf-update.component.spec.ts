import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ODFService } from '../service/odf.service';
import { IODF, ODF } from '../odf.model';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { IJobHistory } from 'app/entities/job-history/job-history.model';
import { JobHistoryService } from 'app/entities/job-history/service/job-history.service';

import { ODFUpdateComponent } from './odf-update.component';

describe('ODF Management Update Component', () => {
  let comp: ODFUpdateComponent;
  let fixture: ComponentFixture<ODFUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let oDFService: ODFService;
  let peopleService: PeopleService;
  let jobHistoryService: JobHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ODFUpdateComponent],
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
      .overrideTemplate(ODFUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ODFUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    oDFService = TestBed.inject(ODFService);
    peopleService = TestBed.inject(PeopleService);
    jobHistoryService = TestBed.inject(JobHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call people query and add missing value', () => {
      const oDF: IODF = { id: 'CBA' };
      const people: IPeople = { id: 77431 };
      oDF.people = people;

      const peopleCollection: IPeople[] = [{ id: 21276 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const expectedCollection: IPeople[] = [people, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ oDF });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(peopleCollection, people);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call jobHistory query and add missing value', () => {
      const oDF: IODF = { id: 'CBA' };
      const jobHistory: IJobHistory = { id: 17507 };
      oDF.jobHistory = jobHistory;

      const jobHistoryCollection: IJobHistory[] = [{ id: 58804 }];
      jest.spyOn(jobHistoryService, 'query').mockReturnValue(of(new HttpResponse({ body: jobHistoryCollection })));
      const expectedCollection: IJobHistory[] = [jobHistory, ...jobHistoryCollection];
      jest.spyOn(jobHistoryService, 'addJobHistoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ oDF });
      comp.ngOnInit();

      expect(jobHistoryService.query).toHaveBeenCalled();
      expect(jobHistoryService.addJobHistoryToCollectionIfMissing).toHaveBeenCalledWith(jobHistoryCollection, jobHistory);
      expect(comp.jobHistoriesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const oDF: IODF = { id: 'CBA' };
      const people: IPeople = { id: 34177 };
      oDF.people = people;
      const jobHistory: IJobHistory = { id: 61145 };
      oDF.jobHistory = jobHistory;

      activatedRoute.data = of({ oDF });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(oDF));
      expect(comp.peopleCollection).toContain(people);
      expect(comp.jobHistoriesCollection).toContain(jobHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ODF>>();
      const oDF = { id: 'ABC' };
      jest.spyOn(oDFService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oDF });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: oDF }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(oDFService.update).toHaveBeenCalledWith(oDF);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ODF>>();
      const oDF = new ODF();
      jest.spyOn(oDFService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oDF });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: oDF }));
      saveSubject.complete();

      // THEN
      expect(oDFService.create).toHaveBeenCalledWith(oDF);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ODF>>();
      const oDF = { id: 'ABC' };
      jest.spyOn(oDFService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oDF });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(oDFService.update).toHaveBeenCalledWith(oDF);
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

    describe('trackJobHistoryById', () => {
      it('Should return tracked JobHistory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackJobHistoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
