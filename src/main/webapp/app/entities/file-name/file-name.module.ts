import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileNameComponent } from './list/file-name.component';
import { FileNameDetailComponent } from './detail/file-name-detail.component';
import { FileNameUpdateComponent } from './update/file-name-update.component';
import { FileNameDeleteDialogComponent } from './delete/file-name-delete-dialog.component';
import { FileNameRoutingModule } from './route/file-name-routing.module';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatTableModule} from "@angular/material/table";
import {CdkTableModule} from "@angular/cdk/table";
import {MatButtonModule} from "@angular/material/button";
import {MatSortModule} from "@angular/material/sort";
import {MatFormFieldModule} from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";

@NgModule({
  imports: [SharedModule, FileNameRoutingModule, MatProgressBarModule, MatTableModule, CdkTableModule, MatButtonModule, MatSortModule, MatFormFieldModule, FormsModule, ReactiveFormsModule, MatInputModule],
  declarations: [FileNameComponent, FileNameDetailComponent, FileNameUpdateComponent, FileNameDeleteDialogComponent],
  entryComponents: [FileNameDeleteDialogComponent],
})
export class FileNameModule {
}
