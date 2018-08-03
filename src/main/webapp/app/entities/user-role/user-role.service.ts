import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { UserRole } from './user-role.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UserRoleService {

    private resourceUrl = SERVER_API_URL + 'api/user-roles';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-roles';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(userRole: UserRole): Observable<UserRole> {
        const copy = this.convert(userRole);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(userRole: UserRole): Observable<UserRole> {
        const copy = this.convert(userRole);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<UserRole> {
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
     * Convert a returned JSON object to UserRole.
     */
    private convertItemFromServer(json: any): UserRole {
        const entity: UserRole = Object.assign(new UserRole(), json);
        entity.createdOn = this.dateUtils
            .convertLocalDateFromServer(json.createdOn);
        entity.updatedOn = this.dateUtils
            .convertLocalDateFromServer(json.updatedOn);
        return entity;
    }

    /**
     * Convert a UserRole to a JSON which can be sent to the server.
     */
    private convert(userRole: UserRole): UserRole {
        const copy: UserRole = Object.assign({}, userRole);
        copy.createdOn = this.dateUtils
            .convertLocalDateToServer(userRole.createdOn);
        copy.updatedOn = this.dateUtils
            .convertLocalDateToServer(userRole.updatedOn);
        return copy;
    }
}
