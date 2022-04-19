import { IPeople } from 'app/entities/people/people.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface ICountry {
  id?: number;
  description?: string | null;
  people?: IPeople | null;
  personne?: IPersonne | null;
}

export class Country implements ICountry {
  constructor(public id?: number, public description?: string | null, public people?: IPeople | null, public personne?: IPersonne | null) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
