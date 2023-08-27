import {Injectable} from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Messages} from '../model/messages';
import {MessageService} from "primeng/api";
import {AuthService} from "./auth.service";
import {GlobalLoadingService} from "./global-loading.service";

@Injectable()
export class ResponseInterceptor implements HttpInterceptor {

  constructor(private messagesObj: Messages, private messageService:MessageService, private authService:AuthService, private globalLoadingService:GlobalLoadingService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.globalLoadingService.show();
    return next.handle(req).pipe(
      map((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          this.messagesObj.addMessages(event.body.messages);
          if (event.status === 200 || event.status === 201){
            this.globalLoadingService.hide();
            event = event.clone({body: event.body.body});
          }
          return event;
        }
        return event;
      }),
      catchError((error: HttpErrorResponse)=>{
        this.globalLoadingService.hide();
        if(error.status===401) {
          this.messageService.add({
            severity: 'error',
            detail: 'Session Timeout!'
          });
          this.authService.logOut();
          return throwError(()=>error);
        }
        if(error.status===500){
          this.messageService.add({
            severity: 'error',
            detail: 'Internal Server Error!'
          });
        }
        return throwError(()=>error);
      })
    );
  }
}
