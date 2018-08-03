import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { SubTasks } from './sub-tasks.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SubTasksService {

    private resourceUrl = SERVER_API_URL + 'api/sub-tasks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sub-tasks';

    constructor(private http: Http) { }

    create(subTasks: SubTasks): Observable<SubTasks> {
        const copy = this.convert(subTasks);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(subTasks: SubTasks): Observable<SubTasks> {
        const copy = this.convert(subTasks);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SubTasks> {
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
     * Convert a returned JSON object to SubTasks.
     */
    private convertItemFromServer(json: any): SubTasks {
        const entity: SubTasks = Object.assign(new SubTasks(), json);
        return entity;
    }

    /**
     * Convert a SubTasks to a JSON which can be sent to the server.
     */
    private convert(subTasks: SubTasks): SubTasks {
        const copy: SubTasks = Object.assign({}, subTasks);
        return copy;
    }
}
