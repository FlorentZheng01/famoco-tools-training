import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDevice, Device } from '../device.model';
import { DeviceService } from '../service/device.service';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';

@Component({
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;

  retailersSharedCollection: IRetailer[] = [];

  editForm = this.fb.group({
    id: [],
    famocoId: [null, [Validators.required]],
    macAddress: [null, [Validators.required]],
    dateCreation: [null, [Validators.required]],
    retailer: [null, Validators.required],
  });

  constructor(
    protected deviceService: DeviceService,
    protected retailerService: RetailerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.updateForm(device);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.createFromForm();
    if (device.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  trackRetailerById(_index: number, item: IRetailer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(device: IDevice): void {
    this.editForm.patchValue({
      id: device.id,
      famocoId: device.famocoId,
      macAddress: device.macAddress,
      dateCreation: device.dateCreation,
      retailer: device.retailer,
    });

    this.retailersSharedCollection = this.retailerService.addRetailerToCollectionIfMissing(this.retailersSharedCollection, device.retailer);
  }

  protected loadRelationshipsOptions(): void {
    this.retailerService
      .query()
      .pipe(map((res: HttpResponse<IRetailer[]>) => res.body ?? []))
      .pipe(
        map((retailers: IRetailer[]) =>
          this.retailerService.addRetailerToCollectionIfMissing(retailers, this.editForm.get('retailer')!.value)
        )
      )
      .subscribe((retailers: IRetailer[]) => (this.retailersSharedCollection = retailers));
  }

  protected createFromForm(): IDevice {
    return {
      ...new Device(),
      id: this.editForm.get(['id'])!.value,
      famocoId: this.editForm.get(['famocoId'])!.value,
      macAddress: this.editForm.get(['macAddress'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      retailer: this.editForm.get(['retailer'])!.value,
    };
  }
}
