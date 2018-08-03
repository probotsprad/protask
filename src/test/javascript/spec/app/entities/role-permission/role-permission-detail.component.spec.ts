/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RolePermissionDetailComponent } from '../../../../../../main/webapp/app/entities/role-permission/role-permission-detail.component';
import { RolePermissionService } from '../../../../../../main/webapp/app/entities/role-permission/role-permission.service';
import { RolePermission } from '../../../../../../main/webapp/app/entities/role-permission/role-permission.model';

describe('Component Tests', () => {

    describe('RolePermission Management Detail Component', () => {
        let comp: RolePermissionDetailComponent;
        let fixture: ComponentFixture<RolePermissionDetailComponent>;
        let service: RolePermissionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [RolePermissionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RolePermissionService,
                    JhiEventManager
                ]
            }).overrideTemplate(RolePermissionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RolePermissionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RolePermissionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RolePermission(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rolePermission).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
