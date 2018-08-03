/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RoleMstDetailComponent } from '../../../../../../main/webapp/app/entities/role-mst/role-mst-detail.component';
import { RoleMstService } from '../../../../../../main/webapp/app/entities/role-mst/role-mst.service';
import { RoleMst } from '../../../../../../main/webapp/app/entities/role-mst/role-mst.model';

describe('Component Tests', () => {

    describe('RoleMst Management Detail Component', () => {
        let comp: RoleMstDetailComponent;
        let fixture: ComponentFixture<RoleMstDetailComponent>;
        let service: RoleMstService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [RoleMstDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RoleMstService,
                    JhiEventManager
                ]
            }).overrideTemplate(RoleMstDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RoleMstDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoleMstService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RoleMst(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.roleMst).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
