import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RetailerComponent } from './list/retailer.component';
import { RetailerDetailComponent } from './detail/retailer-detail.component';
import { RetailerUpdateComponent } from './update/retailer-update.component';
import { RetailerDeleteDialogComponent } from './delete/retailer-delete-dialog.component';
import { RetailerRoutingModule } from './route/retailer-routing.module';

@NgModule({
  imports: [SharedModule, RetailerRoutingModule],
  declarations: [RetailerComponent, RetailerDetailComponent, RetailerUpdateComponent, RetailerDeleteDialogComponent],
  entryComponents: [RetailerDeleteDialogComponent],
})
export class RetailerModule {}
