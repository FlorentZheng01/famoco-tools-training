import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ODFService } from '../service/odf.service';

import { ODFComponent } from './odf.component';

describe('ODF Management Component', () => {
  let comp: ODFComponent;
  let fixture: ComponentFixture<ODFComponent>;
  let service: ODFService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ODFComponent],
    })
      .overrideTemplate(ODFComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ODFComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ODFService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.oDFS?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
