import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RetailerComponent } from './list/retailer.component';
import { RetailerDetailComponent } from './detail/retailer-detail.component';
import { RetailerUpdateComponent } from './update/retailer-update.component';
import { RetailerDeleteDialogComponent } from './delete/retailer-delete-dialog.component';
import { RetailerRoutingModule } from './route/retailer-routing.module';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";
import {MatButtonModule} from "@angular/material/button";
import {CdkTableModule} from "@angular/cdk/table";
import {MatPaginatorModule} from "@angular/material/paginator";

@NgModule({
  imports: [SharedModule, RetailerRoutingModule, MatProgressBarModule, MatTableModule, MatSortModule, MatButtonModule, CdkTableModule, MatPaginatorModule],
  declarations: [RetailerComponent, RetailerDetailComponent, RetailerUpdateComponent, RetailerDeleteDialogComponent],
  entryComponents: [RetailerDeleteDialogComponent],
})
export class RetailerModule {}
