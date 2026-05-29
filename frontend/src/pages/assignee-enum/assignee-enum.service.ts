import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AssigneeEnumModel } from './assignee-enum.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';

export interface Response {
    result: AssigneeEnumModel[];
    status: string;
    message: string;
}

@Injectable({
    providedIn: 'root'
})
export class AssigneeEnumService {

    constructor(private http: HttpClient) { }

    private apiUrl = 'http://' + window.location.hostname + ':8081/api/AssigneeEnum';

    // CORRECCIÓN 1: Método para obtener encabezados frescos con el JWT en cada llamada
    private getHttpOptions() {
        const token = localStorage.getItem('app_token');
        let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return { headers };
    }

    private log(message: string) {
        console.log(message);
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error); 
            this.log(`${operation} failed: ${error.message}`);
            return of(result as T);
        };
    }

    serviceError: Response = { result: [], status: "error - network", message: "" };

    // Create AssigneeEnum
    create(item: AssigneeEnumModel): Observable<Response> {
        // CORRECCIÓN 2: Uso de getHttpOptions()
        return this.http.post<Response>(this.apiUrl, item, this.getHttpOptions())
            .pipe(
                tap((response: Response) => this.log(`AssigneeEnum.create(${response.status})`)),
                catchError(this.handleError<Response>('AssigneeEnum.create()', this.serviceError))
            );
    }

    // Read AssigneeEnum collection
    read(): Observable<Response> {
        // CORRECCIÓN 3: Se añade getHttpOptions() al método GET de lectura masiva
        return this.http.get<Response>(this.apiUrl, this.getHttpOptions())
            .pipe(
                tap(_ => this.log('AssigneeEnum.read()')),
                catchError(this.handleError<Response>('AssigneeEnum.read()', this.serviceError))
            );
    }

    // Read AssigneeEnum by id
    find(assignee_enum_id: number): Observable<Response> {
        const url = `${this.apiUrl}/${assignee_enum_id}`;
        // CORRECCIÓN 4: Se añade getHttpOptions() al método GET por ID
        return this.http.get<Response>(url, this.getHttpOptions())
            .pipe(
                tap(_ => this.log(`AssigneeEnum.find(${assignee_enum_id})`)),
                catchError(this.handleError<Response>(`AssigneeEnum.find(${assignee_enum_id})`, this.serviceError))
            );
    }

    // Update AssigneeEnum
    update(item: AssigneeEnumModel): Observable<Response> {
        return this.http.put<Response>(this.apiUrl, item, this.getHttpOptions())
            .pipe(
                tap(_ => this.log(`AssigneeEnum.update(${item.assignee_enum_id})`)),
                catchError(this.handleError<Response>(`AssigneeEnum.update(${item.assignee_enum_id})`, this.serviceError))
            );
    }

    // Delete AssigneeEnum
    delete(assignee_enum_id: number): Observable<Response> {
        const url = `${this.apiUrl}/${assignee_enum_id}`;
        return this.http.delete<Response>(url, this.getHttpOptions()).pipe(
            tap(_ => this.log(`AssigneeEnum.delete(${assignee_enum_id})`)),
            catchError(this.handleError<Response>(`AssigneeEnum.delete(${assignee_enum_id})`, this.serviceError))
        );
    }
}
