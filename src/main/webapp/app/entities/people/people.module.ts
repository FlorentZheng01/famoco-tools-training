import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PeopleComponent } from './list/people.component';
import { PeopleDetailComponent } from './detail/people-detail.component';
import { PeopleUpdateComponent } from './update/people-update.component';
import { PeopleDeleteDialogComponent } from './delete/people-delete-dialog.component';
import { PeopleRoutingModule } from './route/people-routing.module';

@NgModule({
  imports: [SharedModule, PeopleRoutingModule],
  declarations: [PeopleComponent, PeopleDetailComponent, PeopleUpdateComponent, PeopleDeleteDialogComponent],
  entryComponents: [PeopleDeleteDialogComponent],
})
export class PeopleModule {}
