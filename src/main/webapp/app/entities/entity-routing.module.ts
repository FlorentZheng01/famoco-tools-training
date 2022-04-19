import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'odf',
        data: { pageTitle: 'myFirstJhipsterApp.oDF.home.title' },
        loadChildren: () => import('./odf/odf.module').then(m => m.ODFModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'myFirstJhipsterApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'people',
        data: { pageTitle: 'myFirstJhipsterApp.people.home.title' },
        loadChildren: () => import('./people/people.module').then(m => m.PeopleModule),
      },
      {
        path: 'job-history',
        data: { pageTitle: 'myFirstJhipsterApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history/job-history.module').then(m => m.JobHistoryModule),
      },
      {
        path: 'personne',
        data: { pageTitle: 'myFirstJhipsterApp.personne.home.title' },
        loadChildren: () => import('./personne/personne.module').then(m => m.PersonneModule),
      },
      {
        path: 'personnes',
        data: { pageTitle: 'myFirstJhipsterApp.personnes.home.title' },
        loadChildren: () => import('./personnes/personnes.module').then(m => m.PersonnesModule),
      },
      {
        path: 'file-name',
        data: { pageTitle: 'myFirstJhipsterApp.fileName.home.title' },
        loadChildren: () => import('./file-name/file-name.module').then(m => m.FileNameModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
