import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRetailer, Retailer } from '../retailer.model';
import { RetailerService } from '../service/retailer.service';

@Component({
  selector: 'jhi-retailer-update',
  templateUrl: './retailer-update.component.html',
})
export class RetailerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    address: [null, [Validators.required]],
    mail: [null, [Validators.required]],
  });

  constructor(protected retailerService: RetailerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailer }) => {
      this.updateForm(retailer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retailer = this.createFromForm();
    if (retailer.id !== undefined) {
      this.subscribeToSaveResponse(this.retailerService.update(retailer));
    } else {
      this.subscribeToSaveResponse(this.retailerService.create(retailer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetailer>>): void {
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

  protected updateForm(retailer: IRetailer): void {
    this.editForm.patchValue({
      id: retailer.id,
      name: retailer.name,
      phoneNumber: retailer.phoneNumber,
      address: retailer.address,
      mail: retailer.mail,
    });
  }

  protected createFromForm(): IRetailer {
    return {
      ...new Retailer(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      mail: this.editForm.get(['mail'])!.value,
    };
  }
}
