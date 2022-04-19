import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonnesDetailComponent } from './personnes-detail.component';

describe('Personnes Management Detail Component', () => {
  let comp: PersonnesDetailComponent;
  let fixture: ComponentFixture<PersonnesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonnesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personnes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonnesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonnesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personnes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personnes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
