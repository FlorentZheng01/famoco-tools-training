import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IODF, ODF } from '../odf.model';
import { ODFService } from '../service/odf.service';

@Injectable({ providedIn: 'root' })
export class ODFRoutingResolveService implements Resolve<IODF> {
  constructor(protected service: ODFService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IODF> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((oDF: HttpResponse<ODF>) => {
          if (oDF.body) {
            return of(oDF.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ODF());
  }
}
