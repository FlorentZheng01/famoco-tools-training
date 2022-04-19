import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonnes, Personnes } from '../personnes.model';
import { PersonnesService } from '../service/personnes.service';

@Injectable({ providedIn: 'root' })
export class PersonnesRoutingResolveService implements Resolve<IPersonnes> {
  constructor(protected service: PersonnesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonnes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personnes: HttpResponse<Personnes>) => {
          if (personnes.body) {
            return of(personnes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Personnes());
  }
}
