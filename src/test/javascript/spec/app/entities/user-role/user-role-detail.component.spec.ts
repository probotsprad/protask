/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserRoleDetailComponent } from '../../../../../../main/webapp/app/entities/user-role/user-role-detail.component';
import { UserRoleService } from '../../../../../../main/webapp/app/entities/user-role/user-role.service';
import { UserRole } from '../../../../../../main/webapp/app/entities/user-role/user-role.model';

describe('Component Tests', () => {

    describe('UserRole Management Detail Component', () => {
        let comp: UserRoleDetailComponent;
        let fixture: ComponentFixture<UserRoleDetailComponent>;
        let service: UserRoleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [UserRoleDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserRoleService,
                    JhiEventManager
                ]
            }).overrideTemplate(UserRoleDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserRoleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserRoleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserRole(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userRole).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
