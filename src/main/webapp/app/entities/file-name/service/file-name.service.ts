import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileName, getFileNameIdentifier } from '../file-name.model';

export type EntityResponseType = HttpResponse<IFileName>;
export type EntityArrayResponseType = HttpResponse<IFileName[]>;

@Injectable({ providedIn: 'root' })
export class FileNameService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-names');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileName: IFileName): Observable<EntityResponseType> {
    return this.http.post<IFileName>(this.resourceUrl, fileName, { observe: 'response' });
  }

  update(fileName: IFileName): Observable<EntityResponseType> {
    return this.http.put<IFileName>(`${this.resourceUrl}/${getFileNameIdentifier(fileName) as number}`, fileName, { observe: 'response' });
  }

  partialUpdate(fileName: IFileName): Observable<EntityResponseType> {
    return this.http.patch<IFileName>(`${this.resourceUrl}/${getFileNameIdentifier(fileName) as number}`, fileName, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileName>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileName[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFileNameToCollectionIfMissing(fileNameCollection: IFileName[], ...fileNamesToCheck: (IFileName | null | undefined)[]): IFileName[] {
    const fileNames: IFileName[] = fileNamesToCheck.filter(isPresent);
    if (fileNames.length > 0) {
      const fileNameCollectionIdentifiers = fileNameCollection.map(fileNameItem => getFileNameIdentifier(fileNameItem)!);
      const fileNamesToAdd = fileNames.filter(fileNameItem => {
        const fileNameIdentifier = getFileNameIdentifier(fileNameItem);
        if (fileNameIdentifier == null || fileNameCollectionIdentifiers.includes(fileNameIdentifier)) {
          return false;
        }
        fileNameCollectionIdentifiers.push(fileNameIdentifier);
        return true;
      });
      return [...fileNamesToAdd, ...fileNameCollection];
    }
    return fileNameCollection;
  }
}
