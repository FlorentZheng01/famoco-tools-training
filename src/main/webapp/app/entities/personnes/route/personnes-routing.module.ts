import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonnesComponent } from '../list/personnes.component';
import { PersonnesDetailComponent } from '../detail/personnes-detail.component';
import { PersonnesUpdateComponent } from '../update/personnes-update.component';
import { PersonnesRoutingResolveService } from './personnes-routing-resolve.service';

const personnesRoute: Routes = [
  {
    path: '',
    component: PersonnesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonnesDetailComponent,
    resolve: {
      personnes: PersonnesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonnesUpdateComponent,
    resolve: {
      personnes: PersonnesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonnesUpdateComponent,
    resolve: {
      personnes: PersonnesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personnesRoute)],
  exports: [RouterModule],
})
export class PersonnesRoutingModule {}
