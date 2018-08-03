/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SlackbotsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ConfigDataDetailComponent } from '../../../../../../main/webapp/app/entities/config-data/config-data-detail.component';
import { ConfigDataService } from '../../../../../../main/webapp/app/entities/config-data/config-data.service';
import { ConfigData } from '../../../../../../main/webapp/app/entities/config-data/config-data.model';

describe('Component Tests', () => {

    describe('ConfigData Management Detail Component', () => {
        let comp: ConfigDataDetailComponent;
        let fixture: ComponentFixture<ConfigDataDetailComponent>;
        let service: ConfigDataService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SlackbotsTestModule],
                declarations: [ConfigDataDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ConfigDataService,
                    JhiEventManager
                ]
            }).overrideTemplate(ConfigDataDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConfigDataDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigDataService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ConfigData(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.configData).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
