import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonneComponent } from './list/personne.component';
import { PersonneDetailComponent } from './detail/personne-detail.component';
import { PersonneRoutingModule } from './route/personne-routing.module';

@NgModule({
  imports: [SharedModule, PersonneRoutingModule],
  declarations: [PersonneComponent, PersonneDetailComponent],
})
export class PersonneModule {}
