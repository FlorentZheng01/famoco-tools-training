import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonnes, getPersonnesIdentifier } from '../personnes.model';

export type EntityResponseType = HttpResponse<IPersonnes>;
export type EntityArrayResponseType = HttpResponse<IPersonnes[]>;

@Injectable({ providedIn: 'root' })
export class PersonnesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personnes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personnes: IPersonnes): Observable<EntityResponseType> {
    return this.http.post<IPersonnes>(this.resourceUrl, personnes, { observe: 'response' });
  }

  update(personnes: IPersonnes): Observable<EntityResponseType> {
    return this.http.put<IPersonnes>(`${this.resourceUrl}/${getPersonnesIdentifier(personnes) as number}`, personnes, {
      observe: 'response',
    });
  }

  partialUpdate(personnes: IPersonnes): Observable<EntityResponseType> {
    return this.http.patch<IPersonnes>(`${this.resourceUrl}/${getPersonnesIdentifier(personnes) as number}`, personnes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonnes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonnes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonnesToCollectionIfMissing(
    personnesCollection: IPersonnes[],
    ...personnesToCheck: (IPersonnes | null | undefined)[]
  ): IPersonnes[] {
    const personnes: IPersonnes[] = personnesToCheck.filter(isPresent);
    if (personnes.length > 0) {
      const personnesCollectionIdentifiers = personnesCollection.map(personnesItem => getPersonnesIdentifier(personnesItem)!);
      const personnesToAdd = personnes.filter(personnesItem => {
        const personnesIdentifier = getPersonnesIdentifier(personnesItem);
        if (personnesIdentifier == null || personnesCollectionIdentifiers.includes(personnesIdentifier)) {
          return false;
        }
        personnesCollectionIdentifiers.push(personnesIdentifier);
        return true;
      });
      return [...personnesToAdd, ...personnesCollection];
    }
    return personnesCollection;
  }
}
