import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeople, People } from '../people.model';
import { PeopleService } from '../service/people.service';

@Injectable({ providedIn: 'root' })
export class PeopleRoutingResolveService implements Resolve<IPeople> {
  constructor(protected service: PeopleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeople> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((people: HttpResponse<People>) => {
          if (people.body) {
            return of(people.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new People());
  }
}
