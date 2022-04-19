import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ODFDetailComponent } from './odf-detail.component';

describe('ODF Management Detail Component', () => {
  let comp: ODFDetailComponent;
  let fixture: ComponentFixture<ODFDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ODFDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ oDF: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ODFDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ODFDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load oDF on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.oDF).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
