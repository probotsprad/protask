/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmployeeDetailComponent } from '../../../../../../main/webapp/app/entities/employee/employee-detail.component';
import { EmployeeService } from '../../../../../../main/webapp/app/entities/employee/employee.service';
import { Employee } from '../../../../../../main/webapp/app/entities/employee/employee.model';

describe('Component Tests', () => {

    describe('Employee Management Detail Component', () => {
        let comp: EmployeeDetailComponent;
        let fixture: ComponentFixture<EmployeeDetailComponent>;
        let service: EmployeeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [EmployeeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmployeeService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmployeeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployeeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Employee(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.employee).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
