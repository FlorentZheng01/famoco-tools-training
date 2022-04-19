import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonneComponent } from '../list/personne.component';
import { PersonneDetailComponent } from '../detail/personne-detail.component';
import { PersonneRoutingResolveService } from './personne-routing-resolve.service';

const personneRoute: Routes = [
  {
    path: '',
    component: PersonneComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonneDetailComponent,
    resolve: {
      personne: PersonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personneRoute)],
  exports: [RouterModule],
})
export class PersonneRoutingModule {}
