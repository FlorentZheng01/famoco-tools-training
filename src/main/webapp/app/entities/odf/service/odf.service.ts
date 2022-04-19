import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IODF, getODFIdentifier } from '../odf.model';

export type EntityResponseType = HttpResponse<IODF>;
export type EntityArrayResponseType = HttpResponse<IODF[]>;

@Injectable({ providedIn: 'root' })
export class ODFService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/odfs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(oDF: IODF): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oDF);
    return this.http
      .post<IODF>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(oDF: IODF): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oDF);
    return this.http
      .put<IODF>(`${this.resourceUrl}/${getODFIdentifier(oDF) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(oDF: IODF): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oDF);
    return this.http
      .patch<IODF>(`${this.resourceUrl}/${getODFIdentifier(oDF) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IODF>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IODF[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addODFToCollectionIfMissing(oDFCollection: IODF[], ...oDFSToCheck: (IODF | null | undefined)[]): IODF[] {
    const oDFS: IODF[] = oDFSToCheck.filter(isPresent);
    if (oDFS.length > 0) {
      const oDFCollectionIdentifiers = oDFCollection.map(oDFItem => getODFIdentifier(oDFItem)!);
      const oDFSToAdd = oDFS.filter(oDFItem => {
        const oDFIdentifier = getODFIdentifier(oDFItem);
        if (oDFIdentifier == null || oDFCollectionIdentifiers.includes(oDFIdentifier)) {
          return false;
        }
        oDFCollectionIdentifiers.push(oDFIdentifier);
        return true;
      });
      return [...oDFSToAdd, ...oDFCollection];
    }
    return oDFCollection;
  }

  protected convertDateFromClient(oDF: IODF): IODF {
    return Object.assign({}, oDF, {
      dateofcreation: oDF.dateofcreation?.isValid() ? oDF.dateofcreation.format(DATE_FORMAT) : undefined,
      dateofmodification: oDF.dateofmodification?.isValid() ? oDF.dateofmodification.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateofcreation = res.body.dateofcreation ? dayjs(res.body.dateofcreation) : undefined;
      res.body.dateofmodification = res.body.dateofmodification ? dayjs(res.body.dateofmodification) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((oDF: IODF) => {
        oDF.dateofcreation = oDF.dateofcreation ? dayjs(oDF.dateofcreation) : undefined;
        oDF.dateofmodification = oDF.dateofmodification ? dayjs(oDF.dateofmodification) : undefined;
      });
    }
    return res;
  }
}
