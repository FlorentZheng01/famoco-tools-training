import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileName, FileName } from '../file-name.model';
import { FileNameService } from '../service/file-name.service';

@Injectable({ providedIn: 'root' })
export class FileNameRoutingResolveService implements Resolve<IFileName> {
  constructor(protected service: FileNameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileName> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileName: HttpResponse<FileName>) => {
          if (fileName.body) {
            return of(fileName.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileName());
  }
}
