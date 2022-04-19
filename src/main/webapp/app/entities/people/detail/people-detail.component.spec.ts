import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PeopleDetailComponent } from './people-detail.component';

describe('People Management Detail Component', () => {
  let comp: PeopleDetailComponent;
  let fixture: ComponentFixture<PeopleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PeopleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ people: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PeopleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PeopleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load people on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.people).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
