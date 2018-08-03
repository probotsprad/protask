/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SubTasksDetailComponent } from '../../../../../../main/webapp/app/entities/sub-tasks/sub-tasks-detail.component';
import { SubTasksService } from '../../../../../../main/webapp/app/entities/sub-tasks/sub-tasks.service';
import { SubTasks } from '../../../../../../main/webapp/app/entities/sub-tasks/sub-tasks.model';

describe('Component Tests', () => {

    describe('SubTasks Management Detail Component', () => {
        let comp: SubTasksDetailComponent;
        let fixture: ComponentFixture<SubTasksDetailComponent>;
        let service: SubTasksService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [SubTasksDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SubTasksService,
                    JhiEventManager
                ]
            }).overrideTemplate(SubTasksDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SubTasksDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubTasksService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SubTasks(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.subTasks).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
