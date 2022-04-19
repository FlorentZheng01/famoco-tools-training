import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonnesComponent } from './list/personnes.component';
import { PersonnesDetailComponent } from './detail/personnes-detail.component';
import { PersonnesUpdateComponent } from './update/personnes-update.component';
import { PersonnesDeleteDialogComponent } from './delete/personnes-delete-dialog.component';
import { PersonnesRoutingModule } from './route/personnes-routing.module';

@NgModule({
  imports: [SharedModule, PersonnesRoutingModule],
  declarations: [PersonnesComponent, PersonnesDetailComponent, PersonnesUpdateComponent, PersonnesDeleteDialogComponent],
  entryComponents: [PersonnesDeleteDialogComponent],
})
export class PersonnesModule {}
