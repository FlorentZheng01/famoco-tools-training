import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PeopleService } from '../service/people.service';

import { PeopleComponent } from './people.component';

describe('People Management Component', () => {
  let comp: PeopleComponent;
  let fixture: ComponentFixture<PeopleComponent>;
  let service: PeopleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PeopleComponent],
    })
      .overrideTemplate(PeopleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeopleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PeopleService);

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

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.people?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
