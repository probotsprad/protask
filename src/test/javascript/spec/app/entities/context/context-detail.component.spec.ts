/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ContextDetailComponent } from '../../../../../../main/webapp/app/entities/context/context-detail.component';
import { ContextService } from '../../../../../../main/webapp/app/entities/context/context.service';
import { Context } from '../../../../../../main/webapp/app/entities/context/context.model';

describe('Component Tests', () => {

    describe('Context Management Detail Component', () => {
        let comp: ContextDetailComponent;
        let fixture: ComponentFixture<ContextDetailComponent>;
        let service: ContextService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [ContextDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ContextService,
                    JhiEventManager
                ]
            }).overrideTemplate(ContextDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContextDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContextService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Context(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.context).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
