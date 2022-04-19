import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IODF, ODF } from '../odf.model';

import { ODFService } from './odf.service';

describe('ODF Service', () => {
  let service: ODFService;
  let httpMock: HttpTestingController;
  let elemDefault: IODF;
  let expectedResult: IODF | IODF[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ODFService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      status: 'AAAAAAA',
      operationName: 'AAAAAAA',
      dateofcreation: currentDate,
      dateofmodification: currentDate,
      lastmodificationuser: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateofcreation: currentDate.format(DATE_FORMAT),
          dateofmodification: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ODF', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          dateofcreation: currentDate.format(DATE_FORMAT),
          dateofmodification: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateofcreation: currentDate,
          dateofmodification: currentDate,
        },
        returnedFromService
      );

      service.create(new ODF()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ODF', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          status: 'BBBBBB',
          operationName: 'BBBBBB',
          dateofcreation: currentDate.format(DATE_FORMAT),
          dateofmodification: currentDate.format(DATE_FORMAT),
          lastmodificationuser: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateofcreation: currentDate,
          dateofmodification: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ODF', () => {
      const patchObject = Object.assign(
        {
          dateofcreation: currentDate.format(DATE_FORMAT),
          lastmodificationuser: 'BBBBBB',
        },
        new ODF()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateofcreation: currentDate,
          dateofmodification: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ODF', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          status: 'BBBBBB',
          operationName: 'BBBBBB',
          dateofcreation: currentDate.format(DATE_FORMAT),
          dateofmodification: currentDate.format(DATE_FORMAT),
          lastmodificationuser: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateofcreation: currentDate,
          dateofmodification: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ODF', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addODFToCollectionIfMissing', () => {
      it('should add a ODF to an empty array', () => {
        const oDF: IODF = { id: 'ABC' };
        expectedResult = service.addODFToCollectionIfMissing([], oDF);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oDF);
      });

      it('should not add a ODF to an array that contains it', () => {
        const oDF: IODF = { id: 'ABC' };
        const oDFCollection: IODF[] = [
          {
            ...oDF,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addODFToCollectionIfMissing(oDFCollection, oDF);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ODF to an array that doesn't contain it", () => {
        const oDF: IODF = { id: 'ABC' };
        const oDFCollection: IODF[] = [{ id: 'CBA' }];
        expectedResult = service.addODFToCollectionIfMissing(oDFCollection, oDF);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oDF);
      });

      it('should add only unique ODF to an array', () => {
        const oDFArray: IODF[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '3fc5911d-0bdd-471a-85b5-8995ecd1c584' }];
        const oDFCollection: IODF[] = [{ id: 'ABC' }];
        expectedResult = service.addODFToCollectionIfMissing(oDFCollection, ...oDFArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const oDF: IODF = { id: 'ABC' };
        const oDF2: IODF = { id: 'CBA' };
        expectedResult = service.addODFToCollectionIfMissing([], oDF, oDF2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oDF);
        expect(expectedResult).toContain(oDF2);
      });

      it('should accept null and undefined values', () => {
        const oDF: IODF = { id: 'ABC' };
        expectedResult = service.addODFToCollectionIfMissing([], null, oDF, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oDF);
      });

      it('should return initial array if no ODF is added', () => {
        const oDFCollection: IODF[] = [{ id: 'ABC' }];
        expectedResult = service.addODFToCollectionIfMissing(oDFCollection, undefined, null);
        expect(expectedResult).toEqual(oDFCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
