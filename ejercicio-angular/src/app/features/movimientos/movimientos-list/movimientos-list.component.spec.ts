import 'zone.js';
import { TestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { ComponentFixture } from '@angular/core/testing';
import { MovimientosListComponent } from './movimientos-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';


beforeAll(() => {
  TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
});

describe('MovimientosList', () => {
  let component: MovimientosListComponent;
  let fixture: ComponentFixture<MovimientosListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovimientosListComponent,
        HttpClientTestingModule, RouterTestingModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovimientosListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
