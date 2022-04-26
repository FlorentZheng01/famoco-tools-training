import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRetailer, getRetailerIdentifier } from '../retailer.model';

export type EntityResponseType = HttpResponse<IRetailer>;
export type EntityArrayResponseType = HttpResponse<IRetailer[]>;

@Injectable({ providedIn: 'root' })
export class RetailerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/retailers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(retailer: IRetailer): Observable<EntityResponseType> {
    return this.http.post<IRetailer>(this.resourceUrl, retailer, { observe: 'response' });
  }

  update(retailer: IRetailer): Observable<EntityResponseType> {
    return this.http.put<IRetailer>(`${this.resourceUrl}/${getRetailerIdentifier(retailer) as number}`, retailer, { observe: 'response' });
  }

  partialUpdate(retailer: IRetailer): Observable<EntityResponseType> {
    return this.http.patch<IRetailer>(`${this.resourceUrl}/${getRetailerIdentifier(retailer) as number}`, retailer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRetailer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRetailer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRetailerToCollectionIfMissing(retailerCollection: IRetailer[], ...retailersToCheck: (IRetailer | null | undefined)[]): IRetailer[] {
    const retailers: IRetailer[] = retailersToCheck.filter(isPresent);
    if (retailers.length > 0) {
      const retailerCollectionIdentifiers = retailerCollection.map(retailerItem => getRetailerIdentifier(retailerItem)!);
      const retailersToAdd = retailers.filter(retailerItem => {
        const retailerIdentifier = getRetailerIdentifier(retailerItem);
        if (retailerIdentifier == null || retailerCollectionIdentifiers.includes(retailerIdentifier)) {
          return false;
        }
        retailerCollectionIdentifiers.push(retailerIdentifier);
        return true;
      });
      return [...retailersToAdd, ...retailerCollection];
    }
    return retailerCollection;
  }
}
