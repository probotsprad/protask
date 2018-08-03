/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PoliciesDetailComponent } from '../../../../../../main/webapp/app/entities/policies/policies-detail.component';
import { PoliciesService } from '../../../../../../main/webapp/app/entities/policies/policies.service';
import { Policies } from '../../../../../../main/webapp/app/entities/policies/policies.model';

describe('Component Tests', () => {

    describe('Policies Management Detail Component', () => {
        let comp: PoliciesDetailComponent;
        let fixture: ComponentFixture<PoliciesDetailComponent>;
        let service: PoliciesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [PoliciesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PoliciesService,
                    JhiEventManager
                ]
            }).overrideTemplate(PoliciesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PoliciesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PoliciesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Policies(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.policies).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
