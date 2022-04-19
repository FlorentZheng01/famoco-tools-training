import dayjs from 'dayjs/esm';
import { IPeople } from 'app/entities/people/people.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';

export interface IODF {
  id?: string;
  status?: string | null;
  operationName?: string | null;
  dateofcreation?: dayjs.Dayjs | null;
  dateofmodification?: dayjs.Dayjs | null;
  lastmodificationuser?: string | null;
  people?: IPeople | null;
  jobHistory?: IJobHistory | null;
}

export class ODF implements IODF {
  constructor(
    public id?: string,
    public status?: string | null,
    public operationName?: string | null,
    public dateofcreation?: dayjs.Dayjs | null,
    public dateofmodification?: dayjs.Dayjs | null,
    public lastmodificationuser?: string | null,
    public people?: IPeople | null,
    public jobHistory?: IJobHistory | null
  ) {}
}

export function getODFIdentifier(oDF: IODF): string | undefined {
  return oDF.id;
}
