import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { SubTask } from './sub-task.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SubTaskService {

    private resourceUrl = SERVER_API_URL + 'api/sub-tasks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sub-tasks';

    constructor(private http: Http) { }

    create(subTask: SubTask): Observable<SubTask> {
        const copy = this.convert(subTask);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(subTask: SubTask): Observable<SubTask> {
        const copy = this.convert(subTask);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SubTask> {
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
     * Convert a returned JSON object to SubTask.
     */
    private convertItemFromServer(json: any): SubTask {
        const entity: SubTask = Object.assign(new SubTask(), json);
        return entity;
    }

    /**
     * Convert a SubTask to a JSON which can be sent to the server.
     */
    private convert(subTask: SubTask): SubTask {
        const copy: SubTask = Object.assign({}, subTask);
        return copy;
    }
}
