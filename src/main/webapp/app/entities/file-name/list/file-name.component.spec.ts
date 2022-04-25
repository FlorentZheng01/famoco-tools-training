import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FileNameService } from '../service/file-name.service';

import { FileNameComponent } from './file-name.component';

describe('FileName Management Component', () => {
  let comp: FileNameComponent;
  let fixture: ComponentFixture<FileNameComponent>;
  let service: FileNameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FileNameComponent],
    })
      .overrideTemplate(FileNameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileNameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FileNameService);

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
    expect(comp.fileNames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
