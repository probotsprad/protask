/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PermissionMstDetailComponent } from '../../../../../../main/webapp/app/entities/permission-mst/permission-mst-detail.component';
import { PermissionMstService } from '../../../../../../main/webapp/app/entities/permission-mst/permission-mst.service';
import { PermissionMst } from '../../../../../../main/webapp/app/entities/permission-mst/permission-mst.model';

describe('Component Tests', () => {

    describe('PermissionMst Management Detail Component', () => {
        let comp: PermissionMstDetailComponent;
        let fixture: ComponentFixture<PermissionMstDetailComponent>;
        let service: PermissionMstService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [PermissionMstDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PermissionMstService,
                    JhiEventManager
                ]
            }).overrideTemplate(PermissionMstDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PermissionMstDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PermissionMstService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PermissionMst(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.permissionMst).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
