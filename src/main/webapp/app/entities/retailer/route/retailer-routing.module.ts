import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RetailerComponent } from '../list/retailer.component';
import { RetailerDetailComponent } from '../detail/retailer-detail.component';
import { RetailerUpdateComponent } from '../update/retailer-update.component';
import { RetailerRoutingResolveService } from './retailer-routing-resolve.service';

const retailerRoute: Routes = [
  {
    path: '',
    component: RetailerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RetailerDetailComponent,
    resolve: {
      retailer: RetailerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RetailerUpdateComponent,
    resolve: {
      retailer: RetailerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RetailerUpdateComponent,
    resolve: {
      retailer: RetailerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(retailerRoute)],
  exports: [RouterModule],
})
export class RetailerRoutingModule {}
