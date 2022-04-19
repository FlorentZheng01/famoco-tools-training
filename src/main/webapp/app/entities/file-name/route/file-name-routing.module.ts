import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileNameComponent } from '../list/file-name.component';
import { FileNameDetailComponent } from '../detail/file-name-detail.component';
import { FileNameUpdateComponent } from '../update/file-name-update.component';
import { FileNameRoutingResolveService } from './file-name-routing-resolve.service';

const fileNameRoute: Routes = [
  {
    path: '',
    component: FileNameComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileNameDetailComponent,
    resolve: {
      fileName: FileNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileNameUpdateComponent,
    resolve: {
      fileName: FileNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileNameUpdateComponent,
    resolve: {
      fileName: FileNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileNameRoute)],
  exports: [RouterModule],
})
export class FileNameRoutingModule {}
