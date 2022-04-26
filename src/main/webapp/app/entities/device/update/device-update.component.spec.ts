import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeviceService } from '../service/device.service';
import { IDevice, Device } from '../device.model';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';

import { DeviceUpdateComponent } from './device-update.component';

describe('Device Management Update Component', () => {
  let comp: DeviceUpdateComponent;
  let fixture: ComponentFixture<DeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceService: DeviceService;
  let retailerService: RetailerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeviceUpdateComponent],
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
      .overrideTemplate(DeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceService = TestBed.inject(DeviceService);
    retailerService = TestBed.inject(RetailerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Retailer query and add missing value', () => {
      const device: IDevice = { id: 456 };
      const retailer: IRetailer = { id: 71780 };
      device.retailer = retailer;

      const retailerCollection: IRetailer[] = [{ id: 52956 }];
      jest.spyOn(retailerService, 'query').mockReturnValue(of(new HttpResponse({ body: retailerCollection })));
      const additionalRetailers = [retailer];
      const expectedCollection: IRetailer[] = [...additionalRetailers, ...retailerCollection];
      jest.spyOn(retailerService, 'addRetailerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(retailerService.query).toHaveBeenCalled();
      expect(retailerService.addRetailerToCollectionIfMissing).toHaveBeenCalledWith(retailerCollection, ...additionalRetailers);
      expect(comp.retailersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const device: IDevice = { id: 456 };
      const retailer: IRetailer = { id: 86357 };
      device.retailer = retailer;

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(device));
      expect(comp.retailersSharedCollection).toContain(retailer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Device>>();
      const device = { id: 123 };
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceService.update).toHaveBeenCalledWith(device);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Device>>();
      const device = new Device();
      jest.spyOn(deviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(deviceService.create).toHaveBeenCalledWith(device);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Device>>();
      const device = { id: 123 };
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceService.update).toHaveBeenCalledWith(device);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRetailerById', () => {
      it('Should return tracked Retailer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRetailerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
