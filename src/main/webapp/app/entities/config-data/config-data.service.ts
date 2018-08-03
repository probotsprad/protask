import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ConfigData } from './config-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ConfigDataService {

    private resourceUrl = SERVER_API_URL + 'api/config-data';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/config-data';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(configData: ConfigData): Observable<ConfigData> {
        const copy = this.convert(configData);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(configData: ConfigData): Observable<ConfigData> {
        const copy = this.convert(configData);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ConfigData> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ConfigData.
     */
    private convertItemFromServer(json: any): ConfigData {
        const entity: ConfigData = Object.assign(new ConfigData(), json);
        entity.createdOn = this.dateUtils
            .convertDateTimeFromServer(json.createdOn);
        entity.updatedOn = this.dateUtils
            .convertDateTimeFromServer(json.updatedOn);
        return entity;
    }

    /**
     * Convert a ConfigData to a JSON which can be sent to the server.
     */
    private convert(configData: ConfigData): ConfigData {
        const copy: ConfigData = Object.assign({}, configData);

        copy.createdOn = this.dateUtils.toDate(configData.createdOn);

        copy.updatedOn = this.dateUtils.toDate(configData.updatedOn);
        return copy;
    }
}
