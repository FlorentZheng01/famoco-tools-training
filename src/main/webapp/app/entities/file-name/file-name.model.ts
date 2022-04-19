export interface IFileName {
  id?: number;
  name?: string | null;
  contentContentType?: string | null;
  content?: string | null;
  description?: string | null;
}

export class FileName implements IFileName {
  constructor(
    public id?: number,
    public name?: string | null,
    public contentContentType?: string | null,
    public content?: string | null,
    public description?: string | null
  ) {}
}

export function getFileNameIdentifier(fileName: IFileName): number | undefined {
  return fileName.id;
}
