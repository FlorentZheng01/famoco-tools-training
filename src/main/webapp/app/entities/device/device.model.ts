import dayjs from 'dayjs/esm';
import { IRetailer } from 'app/entities/retailer/retailer.model';

export interface IDevice {
  id?: number;
  famocoId?: number;
  macAddress?: string;
  dateCreation?: dayjs.Dayjs;
  retailer?: IRetailer;
}

export class Device implements IDevice {
  constructor(
    public id?: number,
    public famocoId?: number,
    public macAddress?: string,
    public dateCreation?: dayjs.Dayjs,
    public retailer?: IRetailer
  ) {}
}

export function getDeviceIdentifier(device: IDevice): number | undefined {
  return device.id;
}
