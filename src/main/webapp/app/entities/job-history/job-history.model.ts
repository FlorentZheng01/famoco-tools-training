import dayjs from 'dayjs/esm';
import { IODF } from 'app/entities/odf/odf.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IJobHistory {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  language?: Language | null;
  oDF?: IODF | null;
}

export class JobHistory implements IJobHistory {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public language?: Language | null,
    public oDF?: IODF | null
  ) {}
}

export function getJobHistoryIdentifier(jobHistory: IJobHistory): number | undefined {
  return jobHistory.id;
}
