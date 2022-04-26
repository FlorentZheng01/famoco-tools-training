import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRetailer, Retailer } from '../retailer.model';

import { RetailerService } from './retailer.service';

describe('Retailer Service', () => {
  let service: RetailerService;
  let httpMock: HttpTestingController;
  let elemDefault: IRetailer;
  let expectedResult: IRetailer | IRetailer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RetailerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      phoneNumber: 0,
      address: 'AAAAAAA',
      mail: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Retailer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Retailer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Retailer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          phoneNumber: 1,
          address: 'BBBBBB',
          mail: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Retailer', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          mail: 'BBBBBB',
        },
        new Retailer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Retailer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          phoneNumber: 1,
          address: 'BBBBBB',
          mail: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Retailer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRetailerToCollectionIfMissing', () => {
      it('should add a Retailer to an empty array', () => {
        const retailer: IRetailer = { id: 123 };
        expectedResult = service.addRetailerToCollectionIfMissing([], retailer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailer);
      });

      it('should not add a Retailer to an array that contains it', () => {
        const retailer: IRetailer = { id: 123 };
        const retailerCollection: IRetailer[] = [
          {
            ...retailer,
          },
          { id: 456 },
        ];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, retailer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Retailer to an array that doesn't contain it", () => {
        const retailer: IRetailer = { id: 123 };
        const retailerCollection: IRetailer[] = [{ id: 456 }];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, retailer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailer);
      });

      it('should add only unique Retailer to an array', () => {
        const retailerArray: IRetailer[] = [{ id: 123 }, { id: 456 }, { id: 25486 }];
        const retailerCollection: IRetailer[] = [{ id: 123 }];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, ...retailerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const retailer: IRetailer = { id: 123 };
        const retailer2: IRetailer = { id: 456 };
        expectedResult = service.addRetailerToCollectionIfMissing([], retailer, retailer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailer);
        expect(expectedResult).toContain(retailer2);
      });

      it('should accept null and undefined values', () => {
        const retailer: IRetailer = { id: 123 };
        expectedResult = service.addRetailerToCollectionIfMissing([], null, retailer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailer);
      });

      it('should return initial array if no Retailer is added', () => {
        const retailerCollection: IRetailer[] = [{ id: 123 }];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, undefined, null);
        expect(expectedResult).toEqual(retailerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
