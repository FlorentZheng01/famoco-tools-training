import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonnes, Personnes } from '../personnes.model';

import { PersonnesService } from './personnes.service';

describe('Personnes Service', () => {
  let service: PersonnesService;
  let httpMock: HttpTestingController;
  let elemDefault: IPersonnes;
  let expectedResult: IPersonnes | IPersonnes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonnesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      test: 'AAAAAAA',
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

    it('should create a Personnes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Personnes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Personnes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          test: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Personnes', () => {
      const patchObject = Object.assign({}, new Personnes());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Personnes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          test: 'BBBBBB',
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

    it('should delete a Personnes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPersonnesToCollectionIfMissing', () => {
      it('should add a Personnes to an empty array', () => {
        const personnes: IPersonnes = { id: 123 };
        expectedResult = service.addPersonnesToCollectionIfMissing([], personnes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personnes);
      });

      it('should not add a Personnes to an array that contains it', () => {
        const personnes: IPersonnes = { id: 123 };
        const personnesCollection: IPersonnes[] = [
          {
            ...personnes,
          },
          { id: 456 },
        ];
        expectedResult = service.addPersonnesToCollectionIfMissing(personnesCollection, personnes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Personnes to an array that doesn't contain it", () => {
        const personnes: IPersonnes = { id: 123 };
        const personnesCollection: IPersonnes[] = [{ id: 456 }];
        expectedResult = service.addPersonnesToCollectionIfMissing(personnesCollection, personnes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personnes);
      });

      it('should add only unique Personnes to an array', () => {
        const personnesArray: IPersonnes[] = [{ id: 123 }, { id: 456 }, { id: 14924 }];
        const personnesCollection: IPersonnes[] = [{ id: 123 }];
        expectedResult = service.addPersonnesToCollectionIfMissing(personnesCollection, ...personnesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personnes: IPersonnes = { id: 123 };
        const personnes2: IPersonnes = { id: 456 };
        expectedResult = service.addPersonnesToCollectionIfMissing([], personnes, personnes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personnes);
        expect(expectedResult).toContain(personnes2);
      });

      it('should accept null and undefined values', () => {
        const personnes: IPersonnes = { id: 123 };
        expectedResult = service.addPersonnesToCollectionIfMissing([], null, personnes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personnes);
      });

      it('should return initial array if no Personnes is added', () => {
        const personnesCollection: IPersonnes[] = [{ id: 123 }];
        expectedResult = service.addPersonnesToCollectionIfMissing(personnesCollection, undefined, null);
        expect(expectedResult).toEqual(personnesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
