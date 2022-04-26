import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RetailerService } from '../service/retailer.service';
import { IRetailer, Retailer } from '../retailer.model';

import { RetailerUpdateComponent } from './retailer-update.component';

describe('Retailer Management Update Component', () => {
  let comp: RetailerUpdateComponent;
  let fixture: ComponentFixture<RetailerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let retailerService: RetailerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RetailerUpdateComponent],
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
      .overrideTemplate(RetailerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    retailerService = TestBed.inject(RetailerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const retailer: IRetailer = { id: 456 };

      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(retailer));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Retailer>>();
      const retailer = { id: 123 };
      jest.spyOn(retailerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(retailerService.update).toHaveBeenCalledWith(retailer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Retailer>>();
      const retailer = new Retailer();
      jest.spyOn(retailerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailer }));
      saveSubject.complete();

      // THEN
      expect(retailerService.create).toHaveBeenCalledWith(retailer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Retailer>>();
      const retailer = { id: 123 };
      jest.spyOn(retailerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(retailerService.update).toHaveBeenCalledWith(retailer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
