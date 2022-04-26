import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceComponent } from './list/device.component';
import { DeviceDetailComponent } from './detail/device-detail.component';
import { DeviceUpdateComponent } from './update/device-update.component';
import { DeviceDeleteDialogComponent } from './delete/device-delete-dialog.component';
import { DeviceRoutingModule } from './route/device-routing.module';
import {MatTableModule} from "@angular/material/table";
import {MatButtonModule} from "@angular/material/button";
import {CdkTableModule} from "@angular/cdk/table";
import {MatSortModule} from "@angular/material/sort";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";

@NgModule({
    imports: [SharedModule, DeviceRoutingModule, MatTableModule, MatButtonModule, CdkTableModule, MatSortModule, MatPaginatorModule, MatProgressBarModule, MatFormFieldModule, MatInputModule, MatSelectModule],
  declarations: [DeviceComponent, DeviceDetailComponent, DeviceUpdateComponent, DeviceDeleteDialogComponent],
  entryComponents: [DeviceDeleteDialogComponent],
})
export class DeviceModule {}
