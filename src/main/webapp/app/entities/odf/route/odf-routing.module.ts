import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ODFComponent } from '../list/odf.component';
import { ODFDetailComponent } from '../detail/odf-detail.component';
import { ODFUpdateComponent } from '../update/odf-update.component';
import { ODFRoutingResolveService } from './odf-routing-resolve.service';

const oDFRoute: Routes = [
  {
    path: '',
    component: ODFComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ODFDetailComponent,
    resolve: {
      oDF: ODFRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ODFUpdateComponent,
    resolve: {
      oDF: ODFRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ODFUpdateComponent,
    resolve: {
      oDF: ODFRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(oDFRoute)],
  exports: [RouterModule],
})
export class ODFRoutingModule {}
