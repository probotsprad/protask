import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Policies } from './policies.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PoliciesService {

    private resourceUrl = SERVER_API_URL + 'api/policies';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/policies';

    constructor(private http: Http) { }

    create(policies: Policies): Observable<Policies> {
        const copy = this.convert(policies);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(policies: Policies): Observable<Policies> {
        const copy = this.convert(policies);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Policies> {
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
     * Convert a returned JSON object to Policies.
     */
    private convertItemFromServer(json: any): Policies {
        const entity: Policies = Object.assign(new Policies(), json);
        return entity;
    }

    /**
     * Convert a Policies to a JSON which can be sent to the server.
     */
    private convert(policies: Policies): Policies {
        const copy: Policies = Object.assign({}, policies);
        return copy;
    }
}
