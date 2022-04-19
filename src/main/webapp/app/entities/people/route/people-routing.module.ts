import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PeopleComponent } from '../list/people.component';
import { PeopleDetailComponent } from '../detail/people-detail.component';
import { PeopleUpdateComponent } from '../update/people-update.component';
import { PeopleRoutingResolveService } from './people-routing-resolve.service';

const peopleRoute: Routes = [
  {
    path: '',
    component: PeopleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeopleDetailComponent,
    resolve: {
      people: PeopleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeopleUpdateComponent,
    resolve: {
      people: PeopleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeopleUpdateComponent,
    resolve: {
      people: PeopleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(peopleRoute)],
  exports: [RouterModule],
})
export class PeopleRoutingModule {}
