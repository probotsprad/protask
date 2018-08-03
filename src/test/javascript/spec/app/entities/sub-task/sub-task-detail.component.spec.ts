/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SubTaskDetailComponent } from '../../../../../../main/webapp/app/entities/sub-task/sub-task-detail.component';
import { SubTaskService } from '../../../../../../main/webapp/app/entities/sub-task/sub-task.service';
import { SubTask } from '../../../../../../main/webapp/app/entities/sub-task/sub-task.model';

describe('Component Tests', () => {

    describe('SubTask Management Detail Component', () => {
        let comp: SubTaskDetailComponent;
        let fixture: ComponentFixture<SubTaskDetailComponent>;
        let service: SubTaskService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [SubTaskDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SubTaskService,
                    JhiEventManager
                ]
            }).overrideTemplate(SubTaskDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SubTaskDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubTaskService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SubTask(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.subTask).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
