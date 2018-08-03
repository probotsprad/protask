/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserMstDetailComponent } from '../../../../../../main/webapp/app/entities/user-mst/user-mst-detail.component';
import { UserMstService } from '../../../../../../main/webapp/app/entities/user-mst/user-mst.service';
import { UserMst } from '../../../../../../main/webapp/app/entities/user-mst/user-mst.model';

describe('Component Tests', () => {

    describe('UserMst Management Detail Component', () => {
        let comp: UserMstDetailComponent;
        let fixture: ComponentFixture<UserMstDetailComponent>;
        let service: UserMstService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [UserMstDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserMstService,
                    JhiEventManager
                ]
            }).overrideTemplate(UserMstDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserMstDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserMstService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserMst(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userMst).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
