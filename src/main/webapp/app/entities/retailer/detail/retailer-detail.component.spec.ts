import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RetailerDetailComponent } from './retailer-detail.component';

describe('Retailer Management Detail Component', () => {
  let comp: RetailerDetailComponent;
  let fixture: ComponentFixture<RetailerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RetailerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ retailer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RetailerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RetailerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load retailer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.retailer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
