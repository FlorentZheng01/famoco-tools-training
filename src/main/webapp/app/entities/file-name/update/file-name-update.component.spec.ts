import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileNameService } from '../service/file-name.service';
import { IFileName, FileName } from '../file-name.model';

import { FileNameUpdateComponent } from './file-name-update.component';

describe('FileName Management Update Component', () => {
  let comp: FileNameUpdateComponent;
  let fixture: ComponentFixture<FileNameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileNameService: FileNameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FileNameUpdateComponent],
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
      .overrideTemplate(FileNameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileNameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileNameService = TestBed.inject(FileNameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fileName: IFileName = { id: 456 };

      activatedRoute.data = of({ fileName });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fileName));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileName>>();
      const fileName = { id: 123 };
      jest.spyOn(fileNameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileName });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileName }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileNameService.update).toHaveBeenCalledWith(fileName);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileName>>();
      const fileName = new FileName();
      jest.spyOn(fileNameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileName });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileName }));
      saveSubject.complete();

      // THEN
      expect(fileNameService.create).toHaveBeenCalledWith(fileName);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FileName>>();
      const fileName = { id: 123 };
      jest.spyOn(fileNameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileName });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileNameService.update).toHaveBeenCalledWith(fileName);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
