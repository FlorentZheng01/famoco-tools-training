import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDevice, getDeviceIdentifier } from '../device.model';

export type EntityResponseType = HttpResponse<IDevice>;
export type EntityArrayResponseType = HttpResponse<IDevice[]>;

@Injectable({ providedIn: 'root' })
export class DeviceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/devices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(device: IDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(device);
    return this.http
      .post<IDevice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(device: IDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(device);
    return this.http
      .put<IDevice>(`${this.resourceUrl}/${getDeviceIdentifier(device) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(device: IDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(device);
    return this.http
      .patch<IDevice>(`${this.resourceUrl}/${getDeviceIdentifier(device) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDevice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeviceToCollectionIfMissing(deviceCollection: IDevice[], ...devicesToCheck: (IDevice | null | undefined)[]): IDevice[] {
    const devices: IDevice[] = devicesToCheck.filter(isPresent);
    if (devices.length > 0) {
      const deviceCollectionIdentifiers = deviceCollection.map(deviceItem => getDeviceIdentifier(deviceItem)!);
      const devicesToAdd = devices.filter(deviceItem => {
        const deviceIdentifier = getDeviceIdentifier(deviceItem);
        if (deviceIdentifier == null || deviceCollectionIdentifiers.includes(deviceIdentifier)) {
          return false;
        }
        deviceCollectionIdentifiers.push(deviceIdentifier);
        return true;
      });
      return [...devicesToAdd, ...deviceCollection];
    }
    return deviceCollection;
  }

  protected convertDateFromClient(device: IDevice): IDevice {
    return Object.assign({}, device, {
      dateCreation: device.dateCreation?.isValid() ? device.dateCreation.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((device: IDevice) => {
        device.dateCreation = device.dateCreation ? dayjs(device.dateCreation) : undefined;
      });
    }
    return res;
  }
}
