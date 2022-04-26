import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DeviceService } from '../service/device.service';

import { DeviceComponent } from './device.component';

describe('Device Management Component', () => {
  let comp: DeviceComponent;
  let fixture: ComponentFixture<DeviceComponent>;
  let service: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'device', component: DeviceComponent }]), HttpClientTestingModule],
      declarations: [DeviceComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
          },
        },
      ],
    })
      .overrideTemplate(DeviceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeviceService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });



});
