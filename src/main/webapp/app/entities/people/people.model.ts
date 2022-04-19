import { ICountry } from 'app/entities/country/country.model';
import { IODF } from 'app/entities/odf/odf.model';

export interface IPeople {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  peoplename?: string | null;
  country?: ICountry | null;
  oDF?: IODF | null;
}

export class People implements IPeople {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public peoplename?: string | null,
    public country?: ICountry | null,
    public oDF?: IODF | null
  ) {}
}

export function getPeopleIdentifier(people: IPeople): number | undefined {
  return people.id;
}
