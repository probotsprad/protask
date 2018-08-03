import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { RolePermission } from './role-permission.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RolePermissionService {

    private resourceUrl = SERVER_API_URL + 'api/role-permissions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/role-permissions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(rolePermission: RolePermission): Observable<RolePermission> {
        const copy = this.convert(rolePermission);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(rolePermission: RolePermission): Observable<RolePermission> {
        const copy = this.convert(rolePermission);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<RolePermission> {
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
     * Convert a returned JSON object to RolePermission.
     */
    private convertItemFromServer(json: any): RolePermission {
        const entity: RolePermission = Object.assign(new RolePermission(), json);
        entity.createdOn = this.dateUtils
            .convertLocalDateFromServer(json.createdOn);
        entity.updatedOn = this.dateUtils
            .convertLocalDateFromServer(json.updatedOn);
        return entity;
    }

    /**
     * Convert a RolePermission to a JSON which can be sent to the server.
     */
    private convert(rolePermission: RolePermission): RolePermission {
        const copy: RolePermission = Object.assign({}, rolePermission);
        copy.createdOn = this.dateUtils
            .convertLocalDateToServer(rolePermission.createdOn);
        copy.updatedOn = this.dateUtils
            .convertLocalDateToServer(rolePermission.updatedOn);
        return copy;
    }
}
