import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { UserMst } from './user-mst.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UserMstService {

    private resourceUrl = SERVER_API_URL + 'api/user-msts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-msts';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(userMst: UserMst): Observable<UserMst> {
        const copy = this.convert(userMst);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(userMst: UserMst): Observable<UserMst> {
        const copy = this.convert(userMst);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<UserMst> {
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
     * Convert a returned JSON object to UserMst.
     */
    private convertItemFromServer(json: any): UserMst {
        const entity: UserMst = Object.assign(new UserMst(), json);
        entity.createdOn = this.dateUtils
            .convertLocalDateFromServer(json.createdOn);
        entity.updatedOn = this.dateUtils
            .convertLocalDateFromServer(json.updatedOn);
        return entity;
    }

    /**
     * Convert a UserMst to a JSON which can be sent to the server.
     */
    private convert(userMst: UserMst): UserMst {
        const copy: UserMst = Object.assign({}, userMst);
        copy.createdOn = this.dateUtils
            .convertLocalDateToServer(userMst.createdOn);
        copy.updatedOn = this.dateUtils
            .convertLocalDateToServer(userMst.updatedOn);
        return copy;
    }
}
