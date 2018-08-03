import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Employee } from './employee.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmployeeService {

    private resourceUrl = SERVER_API_URL + 'api/employees';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employees';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(employee: Employee): Observable<Employee> {
        const copy = this.convert(employee);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(employee: Employee): Observable<Employee> {
        const copy = this.convert(employee);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Employee> {
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
     * Convert a returned JSON object to Employee.
     */
    private convertItemFromServer(json: any): Employee {
        const entity: Employee = Object.assign(new Employee(), json);
        entity.dateOfBirth = this.dateUtils
            .convertLocalDateFromServer(json.dateOfBirth);
        return entity;
    }

    /**
     * Convert a Employee to a JSON which can be sent to the server.
     */
    private convert(employee: Employee): Employee {
        const copy: Employee = Object.assign({}, employee);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(employee.dateOfBirth);
        return copy;
    }
}
