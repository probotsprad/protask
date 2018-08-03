import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { PermissionMst } from './permission-mst.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PermissionMstService {

    private resourceUrl = SERVER_API_URL + 'api/permission-msts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/permission-msts';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(permissionMst: PermissionMst): Observable<PermissionMst> {
        const copy = this.convert(permissionMst);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(permissionMst: PermissionMst): Observable<PermissionMst> {
        const copy = this.convert(permissionMst);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<PermissionMst> {
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
     * Convert a returned JSON object to PermissionMst.
     */
    private convertItemFromServer(json: any): PermissionMst {
        const entity: PermissionMst = Object.assign(new PermissionMst(), json);
        entity.createdOn = this.dateUtils
            .convertLocalDateFromServer(json.createdOn);
        entity.updatedOn = this.dateUtils
            .convertLocalDateFromServer(json.updatedOn);
        return entity;
    }

    /**
     * Convert a PermissionMst to a JSON which can be sent to the server.
     */
    private convert(permissionMst: PermissionMst): PermissionMst {
        const copy: PermissionMst = Object.assign({}, permissionMst);
        copy.createdOn = this.dateUtils
            .convertLocalDateToServer(permissionMst.createdOn);
        copy.updatedOn = this.dateUtils
            .convertLocalDateToServer(permissionMst.updatedOn);
        return copy;
    }
}
