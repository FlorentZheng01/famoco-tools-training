export interface IPersonnes {
  id?: number;
  test?: string | null;
}

export class Personnes implements IPersonnes {
  constructor(public id?: number, public test?: string | null) {}
}

export function getPersonnesIdentifier(personnes: IPersonnes): number | undefined {
  return personnes.id;
}
