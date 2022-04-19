import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ODFComponent } from './list/odf.component';
import { ODFDetailComponent } from './detail/odf-detail.component';
import { ODFUpdateComponent } from './update/odf-update.component';
import { ODFDeleteDialogComponent } from './delete/odf-delete-dialog.component';
import { ODFRoutingModule } from './route/odf-routing.module';

@NgModule({
  imports: [SharedModule, ODFRoutingModule],
  declarations: [ODFComponent, ODFDetailComponent, ODFUpdateComponent, ODFDeleteDialogComponent],
  entryComponents: [ODFDeleteDialogComponent],
})
export class ODFModule {}
