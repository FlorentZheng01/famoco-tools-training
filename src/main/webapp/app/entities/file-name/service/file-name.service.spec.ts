import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFileName, FileName } from '../file-name.model';

import { FileNameService } from './file-name.service';

describe('FileName Service', () => {
  let service: FileNameService;
  let httpMock: HttpTestingController;
  let elemDefault: IFileName;
  let expectedResult: IFileName | IFileName[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileNameService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      contentContentType: 'image/png',
      content: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a FileName', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FileName()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileName', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          content: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileName', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new FileName()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileName', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          content: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a FileName', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFileNameToCollectionIfMissing', () => {
      it('should add a FileName to an empty array', () => {
        const fileName: IFileName = { id: 123 };
        expectedResult = service.addFileNameToCollectionIfMissing([], fileName);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileName);
      });

      it('should not add a FileName to an array that contains it', () => {
        const fileName: IFileName = { id: 123 };
        const fileNameCollection: IFileName[] = [
          {
            ...fileName,
          },
          { id: 456 },
        ];
        expectedResult = service.addFileNameToCollectionIfMissing(fileNameCollection, fileName);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileName to an array that doesn't contain it", () => {
        const fileName: IFileName = { id: 123 };
        const fileNameCollection: IFileName[] = [{ id: 456 }];
        expectedResult = service.addFileNameToCollectionIfMissing(fileNameCollection, fileName);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileName);
      });

      it('should add only unique FileName to an array', () => {
        const fileNameArray: IFileName[] = [{ id: 123 }, { id: 456 }, { id: 85688 }];
        const fileNameCollection: IFileName[] = [{ id: 123 }];
        expectedResult = service.addFileNameToCollectionIfMissing(fileNameCollection, ...fileNameArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileName: IFileName = { id: 123 };
        const fileName2: IFileName = { id: 456 };
        expectedResult = service.addFileNameToCollectionIfMissing([], fileName, fileName2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileName);
        expect(expectedResult).toContain(fileName2);
      });

      it('should accept null and undefined values', () => {
        const fileName: IFileName = { id: 123 };
        expectedResult = service.addFileNameToCollectionIfMissing([], null, fileName, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileName);
      });

      it('should return initial array if no FileName is added', () => {
        const fileNameCollection: IFileName[] = [{ id: 123 }];
        expectedResult = service.addFileNameToCollectionIfMissing(fileNameCollection, undefined, null);
        expect(expectedResult).toEqual(fileNameCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
