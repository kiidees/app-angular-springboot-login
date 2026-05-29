import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { StatusEnumModel } from './status-enum.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';

export interface Response {
    result: StatusEnumModel[];
    status: string;
    message: string;
}

@Injectable({
    providedIn: 'root'
})
export class StatusEnumService {

    constructor(private http: HttpClient) { }

    private apiUrl = 'http://' + window.location.hostname + ':8081/api/StatusEnum';

    // CORRECCIÓN 1: Método dinámico para inyectar el JWT en tiempo real
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

    /**
     * Handle Http operation that failed.
     * Let the app continue.
     *
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error); 
            this.log(`${operation} failed: ${error.message}`);
            return of(result as T);
        };
    }

    serviceError: Response = { result: [], status: "error - network", message: "" };

    // Create StatusEnum
    create(item: StatusEnumModel): Observable<Response> {
        // CORRECCIÓN 2: Uso de getHttpOptions()
        return this.http.post<Response>(this.apiUrl, item, this.getHttpOptions())
            .pipe(
                tap((response: Response) => this.log(`StatusEnum.create(${response.status})`)),
                catchError(this.handleError<Response>('StatusEnum.create()', this.serviceError))
            );
    }

    // Read StatusEnum collection
    read(): Observable<Response> {
        // CORRECCIÓN 3: Se añaden los encabezados dinámicos al GET masivo
        return this.http.get<Response>(this.apiUrl, this.getHttpOptions())
            .pipe(
                tap(_ => this.log('StatusEnum.read()')),
                catchError(this.handleError<Response>('StatusEnum.read()', this.serviceError))
            );
    }

    // Read StatusEnum by id
    find(status_enum_id: number): Observable<Response> {
        const url = `${this.apiUrl}/${status_enum_id}`;
        // CORRECCIÓN 4: Se añaden los encabezados dinámicos al GET por ID
        return this.http.get<Response>(url, this.getHttpOptions())
            .pipe(
                tap(_ => this.log(`StatusEnum.find(${status_enum_id})`)),
                catchError(this.handleError<Response>(`StatusEnum.find(${status_enum_id})`, this.serviceError))
            );
    }

    // Update StatusEnum
    update(item: StatusEnumModel): Observable<Response> {
        // CORRECCIÓN 5: Uso de getHttpOptions()
        return this.http.put<Response>(this.apiUrl, item, this.getHttpOptions())
            .pipe(
                tap(_ => this.log(`StatusEnum.update(${item.status_enum_id})`)),
                catchError(this.handleError<Response>(`StatusEnum.update(${item.status_enum_id})`, this.serviceError))
            );
    }

    // Delete StatusEnum
    delete(status_enum_id: number): Observable<Response> {
        const url = `${this.apiUrl}/${status_enum_id}`;
        // CORRECCIÓN 6: Uso de getHttpOptions()
        return this.http.delete<Response>(url, this.getHttpOptions()).pipe(
            tap(_ => this.log(`StatusEnum.delete(${status_enum_id})`)),
            catchError(this.handleError<Response>(`StatusEnum.delete(${status_enum_id})`, this.serviceError))
        );
    }
}
