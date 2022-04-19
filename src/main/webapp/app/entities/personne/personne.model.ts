export interface IPersonne {
  id?: number;
  personne_id?: string | null;
  name?: string | null;
}

export class Personne implements IPersonne {
  constructor(public id?: number, public personne_id?: string | null, public name?: string | null) {}
}

export function getPersonneIdentifier(personne: IPersonne): number | undefined {
  return personne.id;
}
