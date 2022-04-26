export interface IRetailer {
  id?: number;
  name?: string;
  phoneNumber?: number;
  address?: string;
  mail?: string;
}

export class Retailer implements IRetailer {
  constructor(public id?: number, public name?: string, public phoneNumber?: number, public address?: string, public mail?: string) {}
}

export function getRetailerIdentifier(retailer: IRetailer): number | undefined {
  return retailer.id;
}
