import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPeople, People } from '../people.model';

import { PeopleService } from './people.service';

describe('People Service', () => {
  let service: PeopleService;
  let httpMock: HttpTestingController;
  let elemDefault: IPeople;
  let expectedResult: IPeople | IPeople[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PeopleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      email: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
      peoplename: 'AAAAAAA',
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

    it('should create a People', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new People()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a People', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          peoplename: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a People', () => {
      const patchObject = Object.assign(
        {
          phoneNumber: 'BBBBBB',
        },
        new People()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of People', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          peoplename: 'BBBBBB',
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

    it('should delete a People', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPeopleToCollectionIfMissing', () => {
      it('should add a People to an empty array', () => {
        const people: IPeople = { id: 123 };
        expectedResult = service.addPeopleToCollectionIfMissing([], people);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(people);
      });

      it('should not add a People to an array that contains it', () => {
        const people: IPeople = { id: 123 };
        const peopleCollection: IPeople[] = [
          {
            ...people,
          },
          { id: 456 },
        ];
        expectedResult = service.addPeopleToCollectionIfMissing(peopleCollection, people);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a People to an array that doesn't contain it", () => {
        const people: IPeople = { id: 123 };
        const peopleCollection: IPeople[] = [{ id: 456 }];
        expectedResult = service.addPeopleToCollectionIfMissing(peopleCollection, people);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(people);
      });

      it('should add only unique People to an array', () => {
        const peopleArray: IPeople[] = [{ id: 123 }, { id: 456 }, { id: 21249 }];
        const peopleCollection: IPeople[] = [{ id: 123 }];
        expectedResult = service.addPeopleToCollectionIfMissing(peopleCollection, ...peopleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const people: IPeople = { id: 123 };
        const people2: IPeople = { id: 456 };
        expectedResult = service.addPeopleToCollectionIfMissing([], people, people2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(people);
        expect(expectedResult).toContain(people2);
      });

      it('should accept null and undefined values', () => {
        const people: IPeople = { id: 123 };
        expectedResult = service.addPeopleToCollectionIfMissing([], null, people, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(people);
      });

      it('should return initial array if no People is added', () => {
        const peopleCollection: IPeople[] = [{ id: 123 }];
        expectedResult = service.addPeopleToCollectionIfMissing(peopleCollection, undefined, null);
        expect(expectedResult).toEqual(peopleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
